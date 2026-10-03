/*
 * Copyright 2026 Yggdracity projects
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.yggdracity.context.runtime.service.resolver;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.yggdracity.context.cache.CacheHolder;
import org.yggdracity.context.cache.CachedResource;
import org.yggdracity.context.runtime.resolver.AbstractResolver;
import org.yggdracity.context.runtime.resolver.exception.ServiceCreationException;
import org.yggdracity.context.runtime.service.ServiceContext;
import org.yggdracity.context.runtime.service.ServiceDefinitionConfig;
import org.yggdracity.context.runtime.service.ServiceDefinitionProperties;
import org.yggdracity.context.runtime.service.annotation.ServiceDefinition;
import org.yggdracity.context.runtime.service.factory.ServiceFactory;
import org.yggdracity.context.validator.certificate.CertificateValidator;
import org.yggdracity.context.validator.checksum.ChecksumValidator;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * サービスを解決する標準リゾルバーです。
 *
 * <p>設定されたサービスJARを検索し、バージョンの決定、
 * セキュリティ検証、{@link ServiceLoader} によるサービスプロバイダの
 * 解決およびサービスコンテキストの生成を行います。</p>
 *
 * <p>解決したサービスはファイルの更新日時を使用してキャッシュし、
 * JARが変更されていない場合はキャッシュされたサービスコンテキストを返します。</p>
 *
 * @since 1.0
 */
@EnableConfigurationProperties(ServiceDefinitionProperties.class)
public class StandardServiceResolver extends AbstractResolver<Set<ServiceContext>> implements ServiceResolver {

    private static final Logger logger = LoggerFactory.getLogger(StandardServiceResolver.class);

    /**
     * ロード対象となるサービスインターフェースです。
     */
    private final List<Class<?>> services;

    /**
     * サービスの定義情報です。
     */
    private final ServiceDefinitionProperties properties;

    /**
     * サービスインスタンスの生成に使用するファクトリです。
     */
    private final ServiceFactory factory;

    /**
     * サービスリゾルバーを生成します。
     *
     * <p>証明書バリデーターまたはチェックサムバリデーターが
     * {@code null} の場合は、それぞれ検証を行わないバリデーターを使用します。</p>
     *
     * <p>キャッシュホルダーが {@code null} の場合は、
     * デフォルトのキャッシュホルダーを使用します。</p>
     *
     * @param services             ロード対象となるサービスインターフェース
     * @param properties           サービスの定義情報
     * @param certificateValidator 証明書を検証するバリデーター
     * @param checksumValidator    チェックサムを検証するバリデーター
     * @param holder               解決結果を保持するキャッシュ
     * @param factory              サービスインスタンスの生成に使用するファクトリ
     * @since 1.0
     */
    public StandardServiceResolver(final List<Class<?>> services, final ServiceDefinitionProperties properties, final CertificateValidator certificateValidator,
                                   final ChecksumValidator checksumValidator, final CacheHolder<File, CachedResource<Set<ServiceContext>>> holder, final ServiceFactory factory) {

        super(certificateValidator, checksumValidator, holder);
        this.services = services;
        this.properties = properties;
        this.factory = factory;
    }

    /**
     * 指定された識別子に対応するサービスを解決します。
     *
     * @param id サービスの識別子
     * @return 解決されたサービスコンテキストの集合
     * @throws Exception サービスの解決に失敗した場合
     * @since 1.0
     */
    @Override
    public Set<ServiceContext> resolve(@NonNull final String id) throws Exception {

        final String base = this.properties.service().base();
        final ServiceDefinitionConfig definition = this.properties.service().definition().get(id);

        final File path = this.getPath(id, base);
        final File target = this.getTarget(path, definition);

        final Optional<File> fileOptional =
                Arrays.stream(Objects.requireNonNull(
                        target.listFiles((_, name) -> name.endsWith(".jar")))).findFirst();

        if (fileOptional.isEmpty()) {
            logger.warn("Service JAR not found: {}", target);
            return Collections.emptySet();
        }

        final File file = fileOptional.get();

        final CachedResource<Set<ServiceContext>> cached = this.holder.get(file);

        if (cached != null && file.lastModified() == cached.lastModified()) {
            return cached.value();
        }

        if (!this.validate(file, id, definition)) {
            return Collections.emptySet();
        }

        final Set<ServiceContext> contexts = new HashSet<>();

        final URL url = file.toPath().toUri().toURL();

        for (final Class<?> service : this.services) {

            final URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, service.getClassLoader());

            final ServiceLoader<?> serviceLoader = ServiceLoader.load(service, classLoader);

            for (final ServiceLoader.Provider<?> provider : serviceLoader.stream().toList()) {
                final ServiceContext context = this.createContext(id, service, provider, classLoader, definition);
                contexts.add(context);
            }
        }

        this.holder.put(file, new CachedResource<>(file.lastModified(), contexts));
        return contexts;
    }

    /**
     * サービスプロバイダからサービスコンテキストを生成します。
     *
     * <p>サービス実装に付与された {@link ServiceDefinition} から
     * サービスのメタデータを取得し、サービスを生成する
     * {@link ServiceFactory} とともにコンテキストを生成します。</p>
     *
     * <p>{@link ServiceDefinition} が付与されていない場合は、
     * 指定されたサービス定義を使用してサービスコンテキストを生成します。</p>
     *
     * @param id          サービスの識別子
     * @param serviceType サービスインターフェース
     * @param provider    サービスプロバイダ
     * @param classLoader サービスのロードに使用するクラスローダー
     * @param definition  アノテーションが付与されていない場合に使用するサービス定義
     * @return 生成されたサービスコンテキスト
     * @since 1.0
     */
    private ServiceContext createContext(final String id, final Class<?> serviceType, final ServiceLoader.Provider<?> provider, final URLClassLoader classLoader, final ServiceDefinitionConfig definition) {

        final Class<?> providerType = provider.type();
        final ServiceDefinition annotation = providerType.getAnnotation(ServiceDefinition.class);

        final ServiceFactory factory = this.createFactory(serviceType, providerType, classLoader);

        if (annotation == null) {
            return new ServiceContext(factory, id, definition.tag(), definition.name(), definition.version(), definition.scope(), classLoader);
        } else {
            return new ServiceContext(factory, id, annotation.tag(), annotation.name(), annotation.version(), annotation.scope(), classLoader);
        }
    }

    /**
     * サービスインスタンスの生成に使用するファクトリを生成します。
     *
     * <p>外部からファクトリが指定されている場合は、そのファクトリを使用します。
     * 指定されていない場合は、指定されたサービスプロバイダを
     * {@link ServiceLoader} から生成するファクトリを生成します。</p>
     *
     * @param serviceType  サービスインターフェース
     * @param providerType サービスプロバイダの実装クラス
     * @param classLoader  サービスのロードに使用するクラスローダー
     * @return サービスインスタンスを生成するファクトリ
     * @since 1.0
     */
    private ServiceFactory createFactory(final Class<?> serviceType, final Class<?> providerType, final URLClassLoader classLoader) {

        if (this.factory != null) {
            return this.factory;
        }

        return () -> {
            final ServiceLoader<?> serviceLoader = ServiceLoader.load(serviceType, classLoader);

            for (final ServiceLoader.Provider<?> candidate : serviceLoader.stream().toList()) {

                if (candidate.type().equals(providerType)) {
                    return candidate.get();
                }
            }

            throw new ServiceCreationException("Service implementation not found: " + providerType.getName());
        };
    }
}
