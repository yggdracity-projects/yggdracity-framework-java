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

/**
 * バージョンを操作するためのユーティリティクラスです。
 *
 * <p>メジャー、マイナーおよびパッチバージョンの更新を提供します。</p>
 *
 * @since 1.0
 */
public final class VersionUtils {
    private VersionUtils() {

    }

    /**
     * メジャーバージョンを更新します。
     *
     * <p>メジャーバージョンを1増加し、マイナーバージョンを0にします。
     * パッチバージョンが指定されている場合は0に設定し、
     * 指定されていない場合は指定されていない状態を維持します。</p>
     *
     * <p>{@code latest} の場合は、元のバージョンをそのまま返します。</p>
     *
     * @param version 更新対象のバージョン
     * @return 更新されたバージョン
     * @since 1.0
     */
    public static Version updateMajor(final Version version) {
        if (version.isLatest()) {
            return version;
        }
        if (version.patch() == null) {
            return new Version(version.major() + 1, 0, null);
        }
        return new Version(version.major() + 1, 0, 0L);
    }

    /**
     * マイナーバージョンを更新します。
     *
     * <p>マイナーバージョンを1増加し、パッチバージョンを0にします。
     * パッチバージョンが指定されていない場合は、
     * 指定されていない状態を維持します。</p>
     *
     * <p>{@code latest} の場合は、元のバージョンをそのまま返します。</p>
     *
     * @param version 更新対象のバージョン
     * @return 更新されたバージョン
     * @since 1.0
     */
    public static Version updateMinor(final Version version) {
        if (version.isLatest()) {
            return version;
        }
        if (version.patch() == null) {
            return new Version(version.major(), version.minor() + 1, null);
        }
        return new Version(version.major(), version.minor() + 1, 0L);
    }

    /**
     * パッチバージョンを更新します。
     *
     * <p>パッチバージョンが指定されている場合は1増加します。
     * パッチバージョンが指定されていない場合は、
     * 指定されていない状態を維持します。</p>
     *
     * <p>{@code latest} の場合は、元のバージョンをそのまま返します。</p>
     *
     * @param version 更新対象のバージョン
     * @return 更新されたバージョン
     * @since 1.0
     */
    public static Version updatePatch(final Version version) {
        if (version.isLatest()) {
            return version;
        }
        if (version.patch() == null) {
            return new Version(version.major(), version.minor(), null);
        }
        return new Version(version.major(), version.minor(), version.patch() + 1);
    }

}
