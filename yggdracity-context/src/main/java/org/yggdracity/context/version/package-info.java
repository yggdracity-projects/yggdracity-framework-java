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
 * バージョンを扱うための値オブジェクトを提供します。
 *
 * <p>バージョン番号の解析、取得および比較を提供し、
 * {@code major.minor} および {@code major.minor.patch} 形式の
 * バージョンを扱います。</p>
 *
 * <p>また、特別なバージョンとして {@code latest} をサポートし、
 * 通常のバージョンより新しいバージョンとして比較できます。</p>
 *
 * @see Version
 */
package org.yggdracity.context.version;