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

package org.yggdracity.context.runtime.service.resolver.scope;

/**
 * サービスインスタンスのスコープを管理します。
 *
 * <p>サービスのスコープに応じて、サービスインスタンスの生成および
 * 取得方法を管理します。</p>
 *
 * @since 1.0
 */
public interface ServiceScopeManager extends AutoCloseable {

    /**
     * サービスインスタンスを取得します。
     *
     * @return サービスインスタンス
     * @throws Exception サービスインスタンスの取得に失敗した場合
     * @since 1.0
     */
    Object get() throws Exception;

    /**
     * スコープ管理を終了します。
     *
     * <p>リソースの解放が不要な場合、このメソッドは何も行いません。</p>
     *
     * @throws Exception スコープ管理の終了に失敗した場合
     * @since 1.0
     */
    @Override
    default void close() throws Exception {
    }
}
