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

import org.jspecify.annotations.NonNull;

/**
 * 指定された識別子から対象のリソースを解決するリゾルバーです。
 *
 * @param <T> 解決するリソースの型
 * @since 1.0
 */
public interface Resolver<T> {

    /**
     * 指定された識別子に対応するリソースを解決します。
     *
     * @param id リソースの識別子
     * @return 解決されたリソース
     * @throws Exception リソースの解決に失敗した場合
     * @since 1.0
     */
    T resolve(@NonNull String id) throws Exception;
}
