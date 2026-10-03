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

package org.yggdracity.context.runtime.resolver;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yggdracity.context.cache.CacheHolder;
import org.yggdracity.context.cache.CachedResource;
import org.yggdracity.context.cache.StandardCacheHolder;
import org.yggdracity.context.validator.Security;
import org.yggdracity.context.validator.certificate.CertificateValidator;
import org.yggdracity.context.validator.certificate.none.NoOpCertificateValidator;
import org.yggdracity.context.validator.checksum.ChecksumValidator;
import org.yggdracity.context.validator.checksum.none.NoOpChecksumValidator;
import org.yggdracity.context.version.Version;
import org.yggdracity.util.Jar;

import java.io.File;
import java.io.IOException;
import java.security.cert.Certificate;
import java.util.*;

/**
 * リソースを解決するための基底実装です。
 *
 * <p>リソースの配置パスの取得、ロード対象となるバージョンの決定、
 * セキュリティ検証および解決結果のキャッシュなど、
 * リゾルバーで共通して使用する処理を提供します。</p>
 *
 * <p>具体的なリソースのロード処理は、サブクラスで実装します。</p>
 *
 * @param <T> 解決するリソースの型
 * @since 1.0
 */
public abstract class AbstractResolver<T> implements Resolver<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractResolver.class);

    /**
     * 証明書を検証するバリデーターです。
     */
    protected final CertificateValidator certificateValidator;

    /**
     * チェックサムを検証するバリデーターです。
     */
    protected final ChecksumValidator checksumValidator;

    /**
     * 解決したリソースを保持するキャッシュです。
     */
    protected final CacheHolder<File, CachedResource<T>> holder;

    /**
     * リソースリゾルバーを生成します。
     *
     * <p>証明書バリデーターまたはチェックサムバリデーターが
     * {@code null} の場合は、それぞれ検証を行わないバリデーターを使用します。</p>
     *
     * <p>キャッシュホルダーが {@code null} の場合は、
     * デフォルトのキャッシュホルダーを使用します。</p>
     *
     * @param certificateValidator 証明書を検証するバリデーター
     * @param checksumValidator    チェックサムを検証するバリデーター
     * @param holder               解決結果を保持するキャッシュホルダー
     * @since 1.0
     */
    protected AbstractResolver(final CertificateValidator certificateValidator, final ChecksumValidator checksumValidator, final CacheHolder<File, CachedResource<T>> holder) {
        this.certificateValidator = certificateValidator != null ? certificateValidator : new NoOpCertificateValidator();
        this.checksumValidator = checksumValidator != null ? checksumValidator : new NoOpChecksumValidator();
        this.holder = holder != null ? holder : new StandardCacheHolder<>();
    }

    /**
     * 指定されたリソース識別子からリソースの配置パスを取得します。
     *
     * <p>リソース識別子をベースディレクトリに対する相対パスとして扱い、
     * 正規化したパスがベースディレクトリ配下に存在することを検証します。</p>
     *
     * <p>絶対パス、ベースディレクトリ外を指すパス、または
     * ファイルを指定した場合は例外をスローします。</p>
     *
     * @param id   リソースの識別子
     * @param base リソースのベースディレクトリ
     * @return 検証済みのリソース配置パス
     * @throws IOException              パスの正規化に失敗した場合
     * @throws IllegalArgumentException リソースパスが不正な場合
     * @since 1.0
     */
    protected @NonNull File getPath(final String id, final String base) throws IOException {
        final File basePath = new File(base).getCanonicalFile();
        final File path = new File(basePath, id).getCanonicalFile();

        if (new File(id).isAbsolute()) {
            throw new IllegalArgumentException("Resource path must be relative: " + id);
        }

        if (!path.toPath().startsWith(basePath.toPath())) {
            throw new IllegalArgumentException("Resource path must be within the resource base directory: " + id);
        }

        if (path.isFile()) {
            throw new IllegalArgumentException("Resource path must be a directory: " + path);
        }

        return path;
    }

    /**
     * 指定されたリソース配置パスからロード対象となるバージョンのパスを取得します。
     *
     * <p>バージョンが明示的に指定されている場合は、指定されたバージョンを使用します。
     * {@code latest} または未指定の場合は、リソース配置パスに存在する
     * バージョンの中から最新のバージョンを選択します。</p>
     *
     * @param path       リソースの配置パス
     * @param definition リソースの定義
     * @return ロード対象となるリソースバージョンのパス
     * @throws IllegalStateException ロード可能なリソースバージョンが存在しない場合
     * @since 1.0
     */
    protected File getTarget(final File path, final Definition definition) {

        final String version = definition.version();

        if (StringUtils.isNotBlank(version) && !"latest".equalsIgnoreCase(version)) {
            return new File(path, version);
        }

        return Arrays.stream(Objects.requireNonNull(path.listFiles(File::isDirectory)))
                .max(Comparator.comparing(file -> new Version(file.getName())))
                .orElseThrow(() -> new IllegalStateException("No resource version found: " + path.getName()));
    }


    /**
     * 設定されたセキュリティ検証を実行します。
     *
     * <p>証明書およびチェックサムの検証が設定されている場合は、
     * 設定された検証方式に従って検証します。</p>
     *
     * <p>セキュリティ検証が設定されていない場合は、
     * 検証を行わず {@code true} を返します。</p>
     *
     * @param file       検証対象のファイル
     * @param id         リソースの識別子
     * @param definition リソースの定義
     * @return すべてのセキュリティ検証に成功した場合は {@code true}
     * @throws Exception セキュリティ検証の実行中にエラーが発生した場合
     * @since 1.0
     */
    protected boolean validate(final File file, final String id, final Definition definition) throws Exception {

        if (definition.security() == null) {
            return true;
        }

        final Map<Security.Validator, ? extends Security.Parameter> validators =
                definition.security().validator();

        if (validators.containsKey(Security.Validator.CERTIFICATE) && !this.validateCertificate(file, id, validators)) {
            return false;
        }

        return !validators.containsKey(Security.Validator.CHECKSUM) || this.validateChecksum(file, id, validators);
    }

    /**
     * 設定された証明書検証を実行します。
     *
     * @param file       検証対象のファイル
     * @param id         リソースの識別子
     * @param validators セキュリティ検証の設定
     * @return 証明書の検証に成功した場合は {@code true}、それ以外の場合は {@code false}
     * @throws Exception セキュリティ検証の実行中にエラーが発生した場合
     * @since 1.0
     */
    protected boolean validateCertificate(final File file, final String id, final Map<Security.Validator, ? extends Security.Parameter> validators) throws Exception {

        final Security.CertificateParameter parameter = (Security.CertificateParameter) validators.get(Security.Validator.CERTIFICATE);
        this.certificateValidator.setParameter(parameter);
        final List<Certificate> certificates = Jar.getCertificates(file.toPath());

        if (!this.certificateValidator.isValid(certificates)) {
            logger.error("Certificate validation failed for resource '{}': {}", id, file.getAbsolutePath());
            return false;
        }

        return true;
    }


    /**
     * 設定されたチェックサム検証を実行します。
     *
     * @param file       検証対象のファイル
     * @param id         リソースの識別子
     * @param validators セキュリティ検証の設定
     * @return チェックサムの検証に成功した場合は {@code true}、それ以外の場合は {@code false}
     * @throws Exception セキュリティ検証の実行中にエラーが発生した場合
     * @since 1.0
     */
    protected boolean validateChecksum(final File file, final String id, final Map<Security.Validator, ? extends Security.Parameter> validators) throws Exception {

        final Security.ChecksumParameter parameter = (Security.ChecksumParameter) validators.get(Security.Validator.CHECKSUM);
        this.checksumValidator.setParameter(parameter);

        if (!this.checksumValidator.isValid(file.toPath())) {
            logger.error("Checksum validation failed for resource '{}': {}", id, file.getAbsolutePath());
            return false;
        }

        return true;
    }

}
