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
 * サービスのプロトタイプスコープを管理します。
 *
 * <p>サービスを取得するたびに新しいインスタンスを生成します。</p>
 *
 * @since 1.0
 */
public final class PrototypeServiceScope implements ServiceScopeManager {

    private final ServiceFactory factory;

    /**
     * 指定されたファクトリからサービスを生成するスコープを生成します。
     *
     * @param factory サービスを生成するファクトリ
     * @since 1.0
     */
    public PrototypeServiceScope(final ServiceFactory factory) {
        this.factory = factory;
    }

    /**
     * 新しいサービスインスタンスを取得します。
     *
     * <p>呼び出すたびにファクトリから新しいインスタンスを生成します。</p>
     *
     * @return 新しいサービスインスタンス
     * @throws Exception サービスの生成に失敗した場合
     * @since 1.0
     */
    @Override
    public Object get() throws Exception {
        return this.factory.create();
    }

}
