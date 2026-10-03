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

import org.yggdracity.context.runtime.resolver.exception.ServiceCreationException;
import org.yggdracity.context.runtime.resolver.exception.ServiceCreationRuntimeException;
import org.yggdracity.context.runtime.service.ServiceContext;
import org.yggdracity.context.runtime.service.resolver.ServiceResolver;
import org.yggdracity.context.runtime.service.resolver.scope.ServiceScopeManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * サービスの取得を管理するマネージャーです。
 *
 * <p>サービスリゾルバーからサービスコンテキストを取得し、
 * サービスのスコープに応じた {@link ServiceScopeManager} を使用して
 * サービスインスタンスを取得します。</p>
 *
 * <p>利用者はスコープやサービスリゾルバーを意識することなく、
 * サービスの識別子を指定してサービスを取得できます。</p>
 *
 * @since 1.0
 */
public final class ServiceManager {

    private final ServiceResolver resolver;

    private final ServiceScopeManagerFactory scopeManagerFactory;

    private final Map<String, ServiceScopeManager> scopes = new ConcurrentHashMap<>();

    /**
     * サービスマネージャーを生成します。
     *
     * @param resolver            サービスを解決するリゾルバー
     * @param scopeManagerFactory サービスのスコープに応じた
     *                            {@link ServiceScopeManager} を生成するファクトリ
     * @since 1.0
     */
    public ServiceManager(final ServiceResolver resolver, final ServiceScopeManagerFactory scopeManagerFactory) {
        this.resolver = resolver;
        this.scopeManagerFactory = scopeManagerFactory;
    }

    /**
     * 指定された識別子のサービスを取得します。
     *
     * <p>サービスのスコープに応じた
     * {@link ServiceScopeManager} を使用してサービスインスタンスを取得します。</p>
     *
     * @param id サービスの識別子
     * @return サービスインスタンス
     * @throws ServiceCreationException 指定されたサービスが見つからない場合
     * @throws Exception                サービスの取得に失敗した場合
     * @since 1.0
     */
    public Object get(final String id) throws Exception {
        final ServiceContext context = this.resolver.resolve(id).stream()
                .findFirst().orElseThrow(() -> new ServiceCreationException("Service not found: " + id));
        final ServiceScopeManager manager = this.scopes.computeIfAbsent(id, key -> this.createScopeManager(context));
        return manager.get();
    }

    /**
     * サービスコンテキストに対応するスコープ管理を生成します。
     *
     * @param context サービスコンテキスト
     * @return 生成されたスコープ管理
     * @throws ServiceCreationRuntimeException スコープ管理の生成に失敗した場合
     */
    private ServiceScopeManager createScopeManager(final ServiceContext context) {

        try {
            return this.scopeManagerFactory.create(context);
        } catch (final Exception exception) {
            throw new ServiceCreationRuntimeException(exception);
        }
    }

}
