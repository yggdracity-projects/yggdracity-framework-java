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

package org.yggdracity.context.validator.certificate;

import org.yggdracity.context.validator.Security;

import java.security.cert.Certificate;
import java.util.List;
import java.util.Properties;

/**
 * 証明書が信頼できるものであるかを検証するためのインターフェースです。
 *
 * <p>検証対象の証明書を本インターフェースの実装に渡し、
 * 証明書が信頼できるものであるかを判定します。</p>
 *
 * <p>証明書の取得および検証対象となるデータの読み込みは、
 * 本インターフェースの責務には含まれません。</p>
 *
 * @see Certificate
 * @since 1.0
 */
public interface CertificateValidator {

    /**
     * 指定された証明書が信頼できるものであるかを検証します。
     *
     * @param certificates 検証対象の証明書
     * @return 証明書が信頼できるものである場合は {@code true}、それ以外の場合は {@code false}
     * @since 1.0
     */
    boolean isValid(List<Certificate> certificates);


    /**
     * 証明書の検証に使用するパラメータを設定します。
     *
     * @param parameter 証明書の検証に使用するパラメータ
     * @since 1.0
     */
    void setParameter(final Security.CertificateParameter parameter);

    /**
     * 証明書の信頼性検証に使用するプロパティを設定します。
     *
     * <p>実装ごとに追加の設定が必要な場合に使用します。
     * 設定が不要な実装では何も行いません。</p>
     *
     * @param props 検証に使用するプロパティ
     * @since 1.0
     */
    default void setProperties(Properties props) {

    }
}
