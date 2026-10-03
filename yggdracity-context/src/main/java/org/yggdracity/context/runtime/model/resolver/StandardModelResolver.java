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

package org.yggdracity.context.runtime.model.resolver;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.yggdracity.context.cache.CacheHolder;
import org.yggdracity.context.cache.CachedResource;
import org.yggdracity.context.runtime.model.ModelContext;
import org.yggdracity.context.runtime.model.ModelDefinitionConfig;
import org.yggdracity.context.runtime.model.ModelDefinitionProperties;
import org.yggdracity.context.runtime.resolver.AbstractResolver;
import org.yggdracity.context.validator.certificate.CertificateValidator;
import org.yggdracity.context.validator.checksum.ChecksumValidator;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * モデルを解決する標準リゾルバーです。
 *
 * <p>設定されたモデルJARを検索し、セキュリティ検証および
 * キャッシュを利用してモデルコンテキストを解決します。</p>
 *
 * @since 1.0
 */
@EnableConfigurationProperties(ModelDefinitionProperties.class)
public class StandardModelResolver extends AbstractResolver<Set<ModelContext>> implements ModelResolver {

    private static final Logger logger = LoggerFactory.getLogger(StandardModelResolver.class);
    private final ModelDefinitionProperties properties;

    /**
     * モデルリゾルバーを生成します。
     *
     * <p>証明書バリデーターまたはチェックサムバリデーターが
     * {@code null} の場合は、それぞれ検証を行わないバリデーターを使用します。</p>
     *
     * <p>キャッシュホルダーが {@code null} の場合は、
     * デフォルトのキャッシュホルダーを使用します。</p>
     *
     * @param properties           モデルの定義情報
     * @param certificateValidator 証明書を検証するバリデーター
     * @param checksumValidator    チェックサムを検証するバリデーター
     * @param holder               解決結果を保持するキャッシュホルダー
     * @since 1.0
     */
    public StandardModelResolver(final ModelDefinitionProperties properties,
                                 final CertificateValidator certificateValidator,
                                 final ChecksumValidator checksumValidator,
                                 final CacheHolder<File, CachedResource<Set<ModelContext>>> holder) {
        super(certificateValidator, checksumValidator, holder);
        this.properties = properties;
    }

    @Override
    public Set<ModelContext> resolve(@NonNull final String id) throws Exception {
        final String base = this.properties.model().base();
        final ModelDefinitionConfig definition = this.properties.model().definition().get(id);

        final File path = this.getPath(id, base);
        final File target = this.getTarget(path, definition);
        final List<File> files = Arrays.stream(Objects.requireNonNull(
                target.listFiles((_, name) -> name.endsWith(".jar")))).toList();

        if (files.isEmpty()) {
            logger.warn("Model JAR not found: {}", target);
            return Collections.emptySet();
        }

        final Set<ModelContext> contexts = new HashSet<>();
        for (final File file : files) {

            final CachedResource<Set<ModelContext>> cached = this.holder.get(file);
            if (cached != null && file.lastModified() == cached.lastModified()) {
                contexts.addAll(cached.value());
                continue;
            }

            if (this.validate(file, id, definition)) {
                final ModelContext context = load(id, file, definition);
                contexts.add(context);
                this.holder.put(file, new CachedResource<>(file.lastModified(), Set.of(context)));
            }
        }
        return contexts;
    }

    /**
     * モデルJARをロードしてモデルコンテキストを生成します。
     *
     * <p>モデルJARをロードするクラスローダーを生成し、
     * モデルコンテキストを生成します。</p>
     *
     * @param id         モデルの識別子
     * @param file       モデルJARのファイル
     * @param definition モデルの定義情報
     * @return ロードされたモデルコンテキスト
     * @throws Exception モデルJARのロードに失敗した場合
     * @since 1.0
     */
    private ModelContext load(final String id, final File file, final ModelDefinitionConfig definition) throws Exception {
        final URL url = file.toPath().toUri().toURL();
        final URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
        return new ModelContext(id, file.getName(), definition.version(), classLoader);
    }
}
