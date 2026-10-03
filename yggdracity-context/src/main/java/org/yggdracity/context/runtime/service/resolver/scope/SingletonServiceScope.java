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

import org.yggdracity.context.runtime.service.factory.ServiceFactory;

/**
 * サービスのシングルトンスコープを管理します。
 * <p>
 * サービスのインスタンスを1度だけ生成し、
 * 以降の取得では同じインスタンスを返します。
 *
 * @since 1.0
 */
public final class SingletonServiceScope implements ServiceScopeManager {

    /**
     * シングルトンとして保持するサービスです。
     */
    private final Object service;

    /**
     * 指定されたファクトリからサービスを生成します。
     *
     * @param factory サービスを生成するファクトリ
     * @throws Exception サービスの生成に失敗した場合
     * @since 1.0
     */
    public SingletonServiceScope(final ServiceFactory factory) throws Exception {
        this.service = factory.create();
    }

    /**
     * シングルトンとして保持しているサービスを取得します。
     *
     * @return サービスインスタンス
     * @since 1.0
     */
    @Override
    public Object get() throws Exception {
        return service;
    }

}
