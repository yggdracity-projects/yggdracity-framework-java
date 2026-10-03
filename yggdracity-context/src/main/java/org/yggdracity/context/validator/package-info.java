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

/**
 * サービスのセキュリティ検証を提供します。
 *
 * <p>サービスJARなどのリソースに対して、証明書やチェックサムを使用した
 * セキュリティ検証を行うためのAPIを提供します。</p>
 *
 * <p>証明書による検証では、証明書のフィンガープリントや証明書チェーンなどを
 * 使用して、信頼できる証明書であることを検証します。</p>
 *
 * <p>チェックサムによる検証では、指定されたアルゴリズムで生成した
 * チェックサムと期待値を比較して、リソースの完全性を検証します。</p>
 *
 * <p>検証方式および検証に必要なパラメータは
 * {@link Security} によって定義します。</p>
 *
 * @see Security
 * @since 1.0
 */
package org.yggdracity.context.validator;

