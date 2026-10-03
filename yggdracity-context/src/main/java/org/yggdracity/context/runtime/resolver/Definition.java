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

import org.yggdracity.context.validator.Security;

/**
 * リソースの解決に使用する定義情報を提供します。
 *
 * <p>リソースのバージョンおよびセキュリティ検証に関する設定を定義します。</p>
 *
 * @since 1.0
 */
public interface Definition {
    /**
     * 解決対象のバージョンを取得します。
     *
     * @return 解決対象のバージョン
     * @since 1.0
     */
    String version();

    /**
     * セキュリティ検証の設定を取得します。
     *
     * @return セキュリティ検証の設定
     * @since 1.0
     */
    Security security();
}
