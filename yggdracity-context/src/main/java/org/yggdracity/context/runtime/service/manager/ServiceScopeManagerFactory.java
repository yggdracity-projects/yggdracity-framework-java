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

package org.yggdracity.context.runtime.service.manager;

import org.yggdracity.context.runtime.service.ServiceContext;
import org.yggdracity.context.runtime.service.resolver.scope.*;

/**
 * サービスのスコープに応じた {@link ServiceScopeManager} を生成するファクトリです。
 *
 * <p>{@link ServiceContext} に定義されたスコープに基づいて、
 * シングルトン、プロトタイプ、スレッド、リクエストの各スコープ管理を生成します。</p>
 *
 * @since 1.0
 */
public final class ServiceScopeManagerFactory {

    /**
     * サービスコンテキストに対応するスコープ管理を生成します。
     *
     * @param context サービスコンテキスト
     * @return サービスのスコープに対応するスコープ管理
     * @throws Exception スコープ管理の生成に失敗した場合
     * @since 1.0
     */
    public ServiceScopeManager create(final ServiceContext context) throws Exception {
        return switch (context.scope()) {
            case SINGLETON -> new SingletonServiceScope(context.factory());
            case PROTOTYPE -> new PrototypeServiceScope(context.factory());
            case THREAD -> new ThreadServiceScope(context.factory());
            case REQUEST -> new RequestServiceScope(context.key(), context.factory());
        };
    }
}
