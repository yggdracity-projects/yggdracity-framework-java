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

package org.yggdracity.context.validator;

import java.util.Map;


/**
 * サービスのセキュリティ検証に関する設定を保持します。
 *
 * <p>証明書やチェックサムなど、サービスを検証するために使用する
 * 検証方式とそのパラメータを管理します。</p>
 *
 * @param validator 検証方式と検証パラメータのマップ
 * @since 1.0
 */
public record Security(Map<Validator, ? extends Parameter> validator) {

    /**
     * サービスの検証方式を定義します。
     *
     * <p>各検証方式は、対応する検証パラメータの型を保持します。</p>
     *
     * @since 1.0
     */
    public enum Validator {
        /**
         * 証明書による検証を行います。
         */
        CERTIFICATE(CertificateParameter.class),
        /**
         * チェックサムによる検証を行います。
         */
        CHECKSUM(ChecksumParameter.class);

        private final Class<? extends Parameter> type;


        Validator(final Class<? extends Parameter> type) {
            this.type = type;
        }

        /**
         * この検証方式で使用するパラメータの型を取得します。
         *
         * @return 検証パラメータの型
         * @since 1.0
         */
        public Class<? extends Parameter> type() {
            return type;
        }
    }

    /**
     * サービスの検証に使用するパラメータを表します。
     *
     * @since 1.0
     */
    public sealed interface Parameter permits CertificateParameter, ChecksumParameter {
    }

    /**
     * 証明書による検証に使用するパラメータを保持します。
     *
     * @param validator 検証方法
     * @param expected  期待する検証値
     * @param algorithm 証明書の検証に使用するアルゴリズム
     * @param publicKey 公開鍵
     * @since 1.0
     */
    public record CertificateParameter(String validator, String expected, String algorithm,
                                       String publicKey) implements Parameter {
    }

    /**
     * チェックサムによる検証に使用するパラメータを保持します。
     *
     * @param type      チェックサムの種別
     * @param expected  期待するチェックサム
     * @param algorithm チェックサムの生成に使用するアルゴリズム
     * @since 1.0
     */
    public record ChecksumParameter(String type, String expected, String algorithm) implements Parameter {
    }
}
