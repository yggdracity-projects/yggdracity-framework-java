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

package org.yggdracity.context.runtime.properties.resolver;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.yggdracity.context.cache.CacheHolder;
import org.yggdracity.context.cache.CachedResource;
import org.yggdracity.context.runtime.properties.PropertiesContext;
import org.yggdracity.context.runtime.properties.PropertiesDefinitionConfig;
import org.yggdracity.context.runtime.properties.SettingsDefinitionProperties;
import org.yggdracity.context.runtime.resolver.AbstractResolver;
import org.yggdracity.context.validator.certificate.CertificateValidator;
import org.yggdracity.context.validator.checksum.ChecksumValidator;

import java.io.File;
import java.util.*;

/**
 * 動的プロパティを解決し、Spring {@link org.springframework.core.env.Environment} に
 * 動的に反映する標準プロパティリゾルバーです。
 *
 * <p>YAMLまたはプロパティ形式の設定ファイルを読み込み、
 * 設定された除外対象を除いたプロパティを {@link ConfigurableEnvironment} に登録します。</p>
 *
 * <p>設定ファイルの変更を検出した場合は、キャッシュを更新するとともに
 * {@link RefreshScope} を更新し、{@code @RefreshScope} が付与された
 * Beanを再生成可能な状態にします。</p>
 *
 * @since 1.0
 */
@EnableConfigurationProperties(SettingsDefinitionProperties.class)
public class StandardPropertiesResolver extends AbstractResolver<Set<PropertiesContext>> implements PropertiesResolver {

    private static final Logger logger = LoggerFactory.getLogger(StandardPropertiesResolver.class);

    private final SettingsDefinitionProperties properties;

    private final ConfigurableEnvironment environment;

    private final RefreshScope refreshScope;

    /**
     * プロパティリゾルバーを生成します。
     *
     * @param properties           設定ファイルの読み込み設定
     * @param certificateValidator 証明書を検証するバリデーター
     * @param checksumValidator    チェックサムを検証するバリデーター
     * @param holder               解決結果を保持するキャッシュホルダー
     * @param environment          プロパティを登録するSpring環境
     * @param refreshScope         {@code @RefreshScope} のBeanを更新するためのスコープ
     * @since 1.0
     */
    public StandardPropertiesResolver(final SettingsDefinitionProperties properties,
                                      final CertificateValidator certificateValidator,
                                      final ChecksumValidator checksumValidator,
                                      final CacheHolder<File, CachedResource<Set<PropertiesContext>>> holder,
                                      final ConfigurableEnvironment environment,
                                      final RefreshScope refreshScope) {
        super(certificateValidator, checksumValidator, holder);
        this.properties = properties;
        this.environment = environment;
        this.refreshScope = refreshScope;
    }

    @Override
    public Set<PropertiesContext> resolve(@NonNull final String id) throws Exception {
        final String base = this.properties.setting().base();
        final PropertiesDefinitionConfig definition = this.properties.setting().definition().get(id);

        final File path = this.getPath(id, base);
        final File target = this.getTarget(path, definition);
        final File file = this.resolveFile(target, definition);

        if (file == null) {
            return Collections.emptySet();
        }

        final CachedResource<Set<PropertiesContext>> cached = this.holder.get(file);

        if (cached != null && file.lastModified() == cached.lastModified()) {
            return cached.value();
        }

        if (!this.validate(file, id, definition)) {
            return Collections.emptySet();
        }

        final PropertySourceLoader loader = this.resolveLoader(definition.extension());

        final List<PropertySource<?>> sources =
                loader.load(definition.name(), new FileSystemResource(file));

        final String protectedExclusion = "yggdracity.runtime.properties.%s.exclusions";
        this.updateEnvironment(sources, definition, String.format(protectedExclusion, id));

        this.refreshScope.refreshAll();

        final Set<PropertiesContext> contexts = new HashSet<>();
        contexts.add(new PropertiesContext(id, definition.name(), definition.version()));
        this.holder.put(file, new CachedResource<>(file.lastModified(), contexts));
        return contexts;
    }

    /**
     * 指定された設定定義に対応する設定ファイルを解決します。
     *
     * <p>設定された拡張子を順番に確認し、存在するファイルを返します。</p>
     *
     * @param target     設定ファイルを検索するディレクトリ
     * @param definition 設定ファイルの定義
     * @return 解決された設定ファイル。存在しない場合は {@code null}
     */
    private File resolveFile(final File target, final PropertiesDefinitionConfig definition) {

        final String name = definition.name();
        final String extensions = String.join("|", definition.extension().getValues());

        for (final String extension : definition.extension().getValues()) {
            final File file = new File(target, name + "." + extension);

            if (file.exists()) {
                return file;
            }
        }

        logger.warn("Setting file not found: {}.[{}]", new File(target, name).getAbsoluteFile(), extensions);
        return null;
    }

    /**
     * 指定された拡張子に対応するプロパティソースローダーを解決します。
     *
     * @param extension 設定ファイルの拡張子種別
     * @return プロパティソースローダー
     */
    private PropertySourceLoader resolveLoader(final PropertiesDefinitionConfig.Extension extension) {
        return switch (extension) {
            case YAML -> new YamlPropertySourceLoader();
            case PROPERTY -> new PropertiesPropertySourceLoader();
        };
    }

    /**
     * 読み込んだプロパティをSpring環境に反映します。
     *
     * <p>除外対象のプロパティを除外した上で、
     * 既存のプロパティソースを置き換えるか、新しいプロパティソースとして追加します。</p>
     *
     * @param sources    読み込んだプロパティソース
     * @param definition 設定ファイルの定義
     * @param exclusion  追加で除外するプロパティ
     */
    private void updateEnvironment(final List<PropertySource<?>> sources, final PropertiesDefinitionConfig definition, String... exclusion) {

        final MutablePropertySources propertySources = this.environment.getPropertySources();

        for (final PropertySource<?> source : sources) {
            if (!(source.getSource() instanceof Map<?, ?> map)) {
                continue;
            }

            final Map<String, Object> properties = this.filterProperties(map, definition, exclusion);

            final MapPropertySource filteredSource = new MapPropertySource(source.getName(), properties);

            if (propertySources.contains(source.getName())) {
                propertySources.replace(source.getName(), filteredSource);
            } else {
                propertySources.addFirst(filteredSource);
            }
        }
    }

    /**
     * 指定されたプロパティから除外対象を除外します。
     *
     * <p>設定定義による除外対象に加えて、
     * 指定された除外対象も適用します。</p>
     *
     * @param source     読み込んだプロパティ
     * @param definition 設定ファイルの定義
     * @param exclusion  追加で除外するプロパティ
     * @return 除外対象を除いたプロパティ
     */
    private Map<String, Object> filterProperties(final Map<?, ?> source, final PropertiesDefinitionConfig definition, final String... exclusion) {

        final Map<String, Object> properties = new HashMap<>();

        for (final Map.Entry<?, ?> entry : source.entrySet()) {
            final String key = String.valueOf(entry.getKey());

            if (this.isExcluded(key, definition.exclusions().toArray(new String[0]))) {
                continue;
            }

            if (this.isExcluded(key, exclusion)) {
                continue;
            }

            properties.put(key, entry.getValue());
        }

        return properties;
    }

    /**
     * 指定されたキーが除外対象に該当するか判定します。
     *
     * <p>除外対象が {@code *} で終わる場合は、
     * その文字列をプレフィックスとして判定します。</p>
     *
     * @param key        判定するプロパティキー
     * @param exclusions 除外対象
     * @return 除外対象に該当する場合は {@code true}
     */
    private boolean isExcluded(final String key, final String... exclusions) {

        for (final String exclusion : exclusions) {
            if (exclusion.endsWith("*")) {
                final String prefix = exclusion.substring(0, exclusion.length() - 1);
                if (key.startsWith(prefix)) {
                    return true;
                }
            } else if (exclusion.equals(key)) {
                return true;
            }
        }

        return false;
    }
}
