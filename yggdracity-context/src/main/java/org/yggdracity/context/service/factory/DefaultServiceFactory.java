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

package org.yggdracity.context.service.factory;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.yggdracity.context.cache.CacheHolder;
import org.yggdracity.context.service.ServiceCacheHolder;
import org.yggdracity.context.service.*;
import org.yggdracity.context.service.ServiceDefinitionProperties.Service.Definition;
import org.yggdracity.context.service.container.ServiceRegisterContainer;
import org.yggdracity.context.validator.Security;
import org.yggdracity.context.validator.certificate.CertificateValidator;
import org.yggdracity.context.validator.certificate.none.NoOpCertificateValidator;
import org.yggdracity.context.validator.checksum.ChecksumValidator;
import org.yggdracity.context.validator.checksum.none.NoOpChecksumValidator;
import org.yggdracity.context.version.Version;
import org.yggdracity.util.Jar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.cert.Certificate;
import java.util.*;

/**
 * サービスコンテキストを生成するデフォルト実装です。
 *
 * <p>設定されたサービスのベースディレクトリからロード対象となるサービスJARを検索し、
 * {@link URLClassLoader} および {@link ServiceLoader} を使用して
 * サービスを動的にロードします。</p>
 *
 * <p>複数のサービス型を指定でき、1つのサービスJARから各サービス型に対応する
 * 複数のサービス実装をロードできます。ロードされた各サービス実装から
 * {@link ServiceDefinition} を取得し、{@link ServiceContext} を生成します。</p>
 *
 * <p>サービスJARに対して証明書およびチェックサムの検証が設定されている場合は、
 * サービスのロード前に指定されたValidatorによる検証を行います。</p>
 *
 * <p>サービスの登録、既存サービスとの置換および
 * {@link ServiceContext} のクローズは、本クラスではなく
 * {@link ServiceRegisterContainer} が管理します。</p>
 *
 * @see ServiceFactory
 * @see ServiceContext
 * @see ServiceDefinition
 * @see ServiceDefinitionProperties
 * @see CertificateValidator
 * @see ChecksumValidator
 * @see CacheHolder
 * @since 1.0
 */
public class DefaultServiceFactory implements ServiceFactory {

    private final List<Class<?>> services;
    private final ApplicationContext context;
    private final ServiceDefinitionProperties properties;
    private final CertificateValidator certificateValidator;
    private final ChecksumValidator checksumValidator;
    private final CacheHolder<File, CachedService> holder;

    /**
     * 指定された設定、サービス型、検証機能およびキャッシュを使用して
     * サービスファクトリを生成します。
     *
     * <p>指定されたサービス型は、サービスJARからサービス実装をロードするために
     * {@link ServiceLoader} で使用されます。</p>
     *
     * <p>証明書およびチェックサムのValidatorが指定されていない場合は、
     * それぞれ検証を行わないValidatorが使用されます。</p>
     *
     * <p>サービスのロード結果は、指定されたキャッシュを使用して管理され、
     * サービスJARの更新状態に応じて再ロードが行われます。</p>
     *
     * @param context              Springのアプリケーションコンテキスト
     * @param services             {@link ServiceLoader} でロードするサービス型の一覧
     * @param properties           サービスのロードに使用する設定
     * @param certificateValidator 証明書検証に使用するValidator
     * @param checksumValidator    チェックサム検証に使用するValidator
     * @param holder               サービスのロード結果を保持するキャッシュ
     * @since 1.0
     */
    public DefaultServiceFactory(final ApplicationContext context, final List<Class<?>> services, final ServiceDefinitionProperties properties,
                                 final CertificateValidator certificateValidator, final ChecksumValidator checksumValidator, final CacheHolder<File, CachedService> holder) {
        this.context = context;
        this.services = services;
        this.properties = properties;
        this.certificateValidator = certificateValidator != null ? certificateValidator : new NoOpCertificateValidator();
        this.checksumValidator = checksumValidator != null ? checksumValidator : new NoOpChecksumValidator();
        this.holder = holder != null ? holder : new ServiceCacheHolder();
    }

    /**
     * 指定されたアプリケーション識別子に対応するサービスコンテキストを生成します。
     *
     * <p>アプリケーション識別子からサービスの配置場所を特定し、設定されたバージョンまたは
     * 最新バージョンのサービスJARをロードします。</p>
     *
     * <p>サービスJARがキャッシュされており、JARの更新日時に変更がない場合は、
     * キャッシュされたサービスコンテキストを返します。</p>
     *
     * <p>JARが更新されている場合は、必要な検証を行った後、
     * 指定された各サービス型について {@link ServiceLoader} を使用して
     * サービス実装をロードします。</p>
     *
     * <p>1つのサービスJARから複数のサービス実装がロードされる場合があり、
     * ロードされた各サービス実装から {@link ServiceContext} を生成します。</p>
     *
     * @param id アプリケーション識別子
     * @return 生成されたサービスコンテキストの集合
     * @throws Exception サービスの検証、ロードまたはコンテキストの生成に失敗した場合
     * @since 1.0
     */
    @Override
    public Set<ServiceContext> make(final String id) throws Exception {
        final String base = this.properties.service().base();
        final Definition definition = this.properties.service().definition().get(id);

        final File path = this.getPath(id, base);
        final File target = this.getTarget(path, definition);
        final File jar = Arrays.stream(Objects.requireNonNull(
                        target.listFiles((_, name) -> name.endsWith(".jar"))))
                .findFirst().orElseThrow(() -> new FileNotFoundException("Service JAR not found: " + target));

        final CachedService cached = this.holder.get(jar);
        if (cached != null && jar.lastModified() == cached.lastModified()) {
            return cached.contexts();
        }

        this.validate(jar, id, definition);

        final URL url = jar.toPath().toUri().toURL();
        final Set<ServiceContext> contexts = new HashSet<>();
        for (Class<?> service : this.services) {
            final URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, service.getClassLoader());
            final ServiceLoader<?> serviceLoader = ServiceLoader.load(service, classLoader);
            final DefaultListableBeanFactory factory = (DefaultListableBeanFactory) this.context.getAutowireCapableBeanFactory();
            for (final Object object : serviceLoader) {
                if (factory.containsSingleton(id)) {
                    factory.destroySingleton(id);
                }

                contexts.add(this.createContext(id, object, classLoader));
            }
        }
        this.holder.put(jar, new CachedService(jar.lastModified(), contexts));
        return contexts;
    }

    /**
     * 指定されたサービスJARの検証を行います。
     *
     * <p>サービスのセキュリティ設定に従って、証明書およびチェックサムを検証します。</p>
     *
     * @param jar        検証対象のサービスJAR
     * @param id         アプリケーション識別子
     * @param definition サービスの定義
     * @throws Exception サービスの検証に失敗した場合
     * @since 1.0
     */
    private void validate(final File jar, final String id, final Definition definition) throws Exception {

        if (definition.security() == null) {
            return;
        }

        final Map<Security.Validator, ? extends Security.Parameter> validators = definition.security().validator();

        if (validators.containsKey(Security.Validator.CERTIFICATE)) {

            final Security.CertificateParameter parameter = (Security.CertificateParameter) validators.get(Security.Validator.CERTIFICATE);
            this.certificateValidator.setParameter(parameter);

            final List<Certificate> certificates = Jar.getCertificates(jar.toPath());
            if (!this.certificateValidator.isValid(certificates)) {
                throw new IllegalStateException("Certificate validation failed for service '" + id + "': " + jar.getAbsolutePath());
            }
        }

        if (validators.containsKey(Security.Validator.CHECKSUM)) {
            final Security.ChecksumParameter parameter = (Security.ChecksumParameter) validators.get(Security.Validator.CHECKSUM);
            this.checksumValidator.setParameter(parameter);

            if (!this.checksumValidator.isValid(jar.toPath())) {
                throw new IllegalStateException("Checksum validation failed for service '" + id + "': " + jar.getAbsolutePath());
            }
        }
    }

    /**
     * 指定されたアプリケーション識別子からサービスの配置パスを取得します。
     *
     * <p>アプリケーション識別子をサービスのベースディレクトリに対する相対パスとして扱い、
     * 正規化したパスがベースディレクトリ配下に存在することを検証します。</p>
     *
     * <p>絶対パス、ベースディレクトリ外を指すパス、またはファイルを指定した場合は
     * 例外をスローします。</p>
     *
     * @param id   アプリケーション識別子
     * @param base サービスのベースディレクトリ
     * @return 検証済みのサービス配置パス
     * @throws IOException              パスの正規化に失敗した場合
     * @throws IllegalArgumentException サービスパスが不正な場合
     * @since 1.0
     */
    private @NonNull File getPath(final String id, final String base) throws IOException {
        final File basePath = new File(base).getCanonicalFile();
        final File path = new File(basePath, id).getCanonicalFile();

        if (new File(id).isAbsolute()) {
            throw new IllegalArgumentException("Service path must be relative: " + id);
        }

        if (!path.toPath().startsWith(basePath.toPath())) {
            throw new IllegalArgumentException("Service path must be within the service base directory: " + id);
        }

        if (path.isFile()) {
            throw new IllegalArgumentException("Service path must be a directory: " + path);
        }

        return path;
    }

    /**
     * 指定されたサービス配置パスからロード対象となるバージョンのパスを取得します。
     *
     * <p>バージョンが明示的に指定されている場合は指定されたバージョンを使用します。
     * {@code latest} または未指定の場合は、サービス配置パスに存在するバージョンの中から
     * 最新のバージョンを選択します。</p>
     *
     * @param path       サービスの配置パス
     * @param definition サービスの定義
     * @return ロード対象となるサービスバージョンのパス
     * @throws IllegalStateException ロード可能なサービスバージョンが存在しない場合
     * @since 1.0
     */
    private File getTarget(final File path, final Definition definition) {

        final String version = definition.version();

        if (StringUtils.isNotBlank(version) && !"latest".equalsIgnoreCase(version)) {
            return new File(path, version);
        }

        return Arrays.stream(Objects.requireNonNull(path.listFiles(File::isDirectory)))
                .max(Comparator.comparing(file -> new Version(file.getName())))
                .orElseThrow(() -> new IllegalStateException("No service version found: " + path.getName()));
    }

    /**
     * ロードされたサービスからサービスコンテキストを生成します。
     *
     * <p>サービス実装に付与された {@link ServiceDefinition} から
     * サービスの名前、グループおよびバージョンを取得し、
     * {@link ServiceContext} を生成します。</p>
     *
     * <p>{@link ServiceDefinition} が付与されていないサービスは、
     * サービス定義が不正なものとして扱います。</p>
     *
     * @param id          アプリケーション識別子
     * @param service     ロードされたサービス
     * @param classLoader サービスのロードに使用したクラスローダー
     * @return 生成されたサービスコンテキスト
     * @throws InvalidServiceDefinitionException サービス実装に {@link ServiceDefinition} が付与されていない場合
     * @since 1.0
     */
    private ServiceContext createContext(String id, Object service, URLClassLoader classLoader) {
        final ServiceDefinition annotation = service.getClass().getAnnotation(ServiceDefinition.class);
        if (annotation == null) {
            // TODO 2026/09/23 警告ログでスキップするかは検討中
            throw new InvalidServiceDefinitionException(service);
        } else {
            final String name = annotation.name();
            final String group = annotation.group();
            final String version = annotation.version();
            return new ServiceContext(service, id, name, group, version, classLoader);
        }
    }

}
