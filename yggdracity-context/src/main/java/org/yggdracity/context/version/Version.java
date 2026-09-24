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

package org.yggdracity.context.version;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;

/**
 * バージョンを表す値オブジェクトです。
 *
 * <p>{@code major.minor} または {@code major.minor.patch} の形式の
 * バージョンを扱います。{@code major.minor} 形式の場合、
 * パッチバージョンは {@code 0} として扱います。</p>
 *
 * <p>また、特別なバージョンとして {@code latest} を扱います。
 * {@code latest} は通常のバージョンより大きいバージョンとして比較されます。</p>
 *
 * @since 1.0
 */
public class Version implements Comparable<Version> {

    private final long major;
    private final long minor;
    private final long patch;
    private final boolean latest;

    /**
     * 指定されたバージョン文字列からバージョンを生成します。
     *
     * <p>バージョン文字列は {@code major.minor.patch} の形式で指定します。
     * {@code major.minor} の形式で指定した場合、パッチバージョンは
     * {@code 0} として扱います。</p>
     *
     * @param version バージョン文字列
     * @throws IllegalArgumentException バージョン形式が不正な場合
     * @since 1.0
     */
    public Version(final String version) {
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Version must not be null or blank.");
        }

        this.latest = "latest".equalsIgnoreCase(version);
        if (this.latest) {
            this.major = 0L;
            this.minor = 0L;
            this.patch = 0L;
            return;
        }

        final String[] values = version.split("\\.");

        if (values.length < 2 || values.length > 3) {
            throw new IllegalArgumentException("Invalid version: " + version);
        }

        try {
            this.major = Long.parseLong(values[0]);
            this.minor = Long.parseLong(values[1]);
            this.patch = values.length == 3 ? Long.parseLong(values[2]) : 0L;
        } catch (final NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid version: " + version, exception);
        }
    }

    /**
     * メジャーバージョンを取得します。
     *
     * @return メジャーバージョン
     * @since 1.0
     */
    public long major() {
        return this.major;
    }

    /**
     * マイナーバージョンを取得します。
     *
     * @return マイナーバージョン
     * @since 1.0
     */
    public long minor() {
        return this.minor;
    }

    /**
     * パッチバージョンを取得します。
     *
     * @return パッチバージョン
     * @since 1.0
     */
    public long patch() {
        return this.patch;
    }

    /**
     * このバージョンが {@code latest} かどうかを判定します。
     *
     * @return {@code latest} の場合は {@code true}、それ以外の場合は {@code false}
     * @since 1.0
     */
    public boolean isLatest() {
        return latest;
    }

    /**
     * 指定されたバージョンと比較します。
     *
     * <p>メジャー、マイナー、パッチの順に比較します。</p>
     *
     * @param version 比較対象のバージョン
     * @return このバージョンが小さい場合は負の値、
     * 等しい場合は0、大きい場合は正の値
     * @since 1.0
     */
    @Override
    public int compareTo(final @NonNull Version version) {
        if (this.latest && version.latest) {
            return 0;
        }

        if (this.latest) {
            return 1;
        }

        if (version.latest) {
            return -1;
        }

        int result = Long.compare(this.major, version.major);

        if (result == 0) {
            result = Long.compare(this.minor, version.minor);
        }

        if (result == 0) {
            result = Long.compare(this.patch, version.patch);
        }

        return result;
    }

    /**
     * バージョン文字列を返します。
     *
     * @return {@code major.minor.patch} 形式のバージョン文字列
     * @since 1.0
     */
    @Override
    public String toString() {
        return this.latest ? "latest" : this.major + "." + this.minor + "." + this.patch;
    }
}

