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

import org.yggdracity.context.runtime.resolver.exception.ServiceCreationException;
import org.yggdracity.context.runtime.service.factory.ServiceFactory;

/**
 * サービスのスレッドスコープを管理します。
 *
 * <p>スレッドごとにサービスインスタンスを生成し、
 * 同一スレッドからの取得では同じインスタンスを返します。</p>
 *
 * @since 1.0
 */
public final class ThreadServiceScope implements ServiceScopeManager {

    /**
     * サービスを生成するファクトリです。
     */
    private final ServiceFactory factory;

    /**
     * スレッドごとのサービスインスタンスを保持します。
     */
    private final ThreadLocal<Object> service;


    /**
     * 指定されたファクトリからスレッドスコープを生成します。
     *
     * @param factory サービスを生成するファクトリ
     * @since 1.0
     */
    public ThreadServiceScope(final ServiceFactory factory) {
        this.factory = factory;
        this.service = ThreadLocal.withInitial(() -> {
            try {
                return this.factory.create();
            } catch (final Exception exception) {
                throw new ServiceCreationException(exception);
            }
        });
    }

    /**
     * 現在のスレッドに対応するサービスインスタンスを取得します。
     *
     * <p>現在のスレッドにサービスインスタンスが存在しない場合は、
     * 新しいインスタンスを生成します。</p>
     *
     * @return 現在のスレッドに対応するサービスインスタンス
     * @since 1.0
     */
    @Override
    public Object get() {
        return this.service.get();
    }

    /**
     * 現在のスレッドに保持されているサービスインスタンスを破棄します。
     *
     * @since 1.0
     */
    @Override
    public void close() {
        this.service.remove();
    }
}
