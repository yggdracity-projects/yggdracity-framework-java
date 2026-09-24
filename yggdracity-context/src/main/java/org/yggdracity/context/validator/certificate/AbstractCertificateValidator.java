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

import java.util.Properties;

/**
 * 証明書の信頼性を検証する実装の基底クラスです。
 *
 * <p>証明書の検証に使用する設定およびプロパティを保持し、
 * 各検証方式で共通して利用する機能を提供します。</p>
 *
 * <p>具体的な証明書の検証処理はサブクラスで実装します。</p>
 *
 * @see CertificateValidator
 * @since 1.0
 */
public abstract class AbstractCertificateValidator implements CertificateValidator {
    /**
     * 証明書の検証に使用する設定です。
     */
    protected Security.CertificateParameter parameter;

    /**
     * 証明書の検証に使用するプロパティです。
     */
    protected Properties properties;


    /**
     * 証明書の検証に使用するパラメータを設定します。
     *
     * @param parameter 証明書の検証に使用するパラメータ
     * @since 1.0
     */
    @Override
    public final void setParameter(final Security.CertificateParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * 証明書の検証に使用するプロパティを設定します。
     *
     * @param properties 証明書の検証に使用するプロパティ
     * @since 1.0
     */
    @Override
    public final void setProperties(final Properties properties) {
        this.properties = properties;
    }

}
