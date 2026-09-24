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
 * 証明書の信頼性を検証するための機能を提供します。
 *
 * <p>証明書を検証し、信頼できる証明書であることを確認するための
 * {@link CertificateValidator} インターフェースと、
 * その共通処理および検証方式の実装を提供します。</p>
 *
 * <p>本パッケージの検証機能は、証明書を利用する処理から分離されており、
 * 証明書のフィンガープリントや証明書チェーンなど、
 * 検証方式に応じた信頼性の検証に使用します。</p>
 *
 * @since 1.0
 */
package org.yggdracity.context.validator.certificate;

