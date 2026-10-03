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

package org.yggdracity.context.cache;

/**
 * キャッシュされたリソースを保持します。
 *
 * <p>リソースの最終更新日時とキャッシュ値を保持し、
 * リソースが更新されたかどうかを判定するために使用します。</p>
 *
 * @param <T>          キャッシュする値の型
 * @param lastModified リソースの最終更新日時
 * @param value        キャッシュされた値
 * @since 1.0
 */
public record CachedResource<T>(long lastModified, T value) {
}
