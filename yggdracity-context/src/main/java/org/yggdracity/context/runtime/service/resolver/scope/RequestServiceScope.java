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

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.yggdracity.context.runtime.service.factory.ServiceFactory;

/**
 * サービスのリクエストスコープを管理します。
 *
 * <p>リクエストごとにサービスインスタンスを生成し、
 * 同一リクエスト中の取得では同じインスタンスを返します。</p>
 *
 * @since 1.0
 */
public final class RequestServiceScope implements ServiceScopeManager {

    /**
     * サービスを生成するファクトリです。
     */
    private final ServiceFactory factory;

    /**
     * リクエスト属性にサービスを格納するための属性名です。
     */
    private final String attribute;


    /**
     * 指定されたサービス名とファクトリからリクエストスコープを生成します。
     *
     * @param name    サービス名
     * @param factory サービスを生成するファクトリ
     * @since 1.0
     */
    public RequestServiceScope(final String name, final ServiceFactory factory) {
        this.attribute = RequestServiceScope.class.getName() + "." + name;
        this.factory = factory;
    }

    /**
     * 現在のリクエストに対応するサービスインスタンスを取得します。
     *
     * <p>現在のリクエストにサービスインスタンスが存在しない場合は、
     * ファクトリから新しいインスタンスを生成してリクエスト属性に格納します。</p>
     *
     * <p>同一リクエスト中に複数回取得した場合は、同じインスタンスを返します。</p>
     *
     * @return 現在のリクエストに対応するサービスインスタンス
     * @throws Exception サービスの生成に失敗した場合
     * @since 1.0
     */
    @Override
    public Object get() throws Exception {
        final RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();

        Object service = attributes.getAttribute(this.attribute, RequestAttributes.SCOPE_REQUEST);

        if (service == null) {
            service = this.factory.create();
            attributes.setAttribute(this.attribute, service, RequestAttributes.SCOPE_REQUEST);
        }

        return service;
    }
}
