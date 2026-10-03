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

package org.yggdracity.context.runtime.model;

import org.yggdracity.context.runtime.resolver.Definition;
import org.yggdracity.context.validator.Security;

/**
 * モデルの解決に使用する定義情報を保持します。
 *
 * <p>モデルのバージョンおよびセキュリティ検証に関する設定を定義します。</p>
 *
 * @param version  モデルのバージョン
 * @param security セキュリティ検証の設定
 * @since 1.0
 */
public record ModelDefinitionConfig(String version, Security security) implements Definition {
}
