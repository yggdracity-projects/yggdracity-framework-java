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

package org.yggdracity.context.validator.checksum;

import org.yggdracity.context.validator.Security;

import java.nio.file.Path;
import java.util.Properties;

/**
 * チェックサムを利用してファイルの完全性を検証するためのインターフェースです。
 *
 * <p>実装クラスは、検証対象のファイルからチェックサムを生成し、
 * 設定されたチェックサムと比較することでファイルの完全性を検証します。</p>
 *
 * @since 1.0
 */
public interface ChecksumValidator {
    /**
     * 指定されたファイルの完全性を検証します。
     *
     * @param file 検証対象のファイル
     * @return チェックサムが一致する場合は {@code true}
     * @throws Exception ファイルの読み込みまたはチェックサムの生成に失敗した場合
     * @since 1.0
     */
    boolean isValid(Path file) throws Exception;

    /**
     * チェックサムによる検証に使用するパラメータを設定します。
     *
     * @param parameter チェックサムによる検証に使用するパラメータ
     * @since 1.0
     */
    void setParameter(final Security.ChecksumParameter parameter);

    /**
     * 検証に使用するプロパティを設定します。
     *
     * <p>実装ごとに追加の設定が必要な場合に使用します。
     * 設定が不要な実装では何も行いません。</p>
     *
     * @param properties 検証に使用するプロパティ
     * @since 1.0
     */
    default void setProperties(Properties properties) {

    }
}
