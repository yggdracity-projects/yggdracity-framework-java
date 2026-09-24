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

package org.yggdracity.context.service.factory;

import org.yggdracity.context.service.ServiceContext;

import java.util.Set;

/**
 * サービスコンテキストを生成するファクトリです。
 *
 * <p>サービス登録コンテナの識別子を受け取り、
 * 対応するサービスコンテキストを生成します。</p>
 *
 * <p>1つのサービス登録コンテナから複数のサービスコンテキストを
 * 登録できるよう、生成したサービスコンテキストを集合として返します。</p>
 *
 * @see ServiceContext
 * @since 1.0
 */
public interface ServiceFactory {

    /**
     * 指定されたサービス登録コンテナに対応するサービスコンテキストを生成します。
     *
     * <p>複数のサービスコンテキストを生成する場合は、
     * 生成したすべてのサービスコンテキストを集合として返します。</p>
     *
     * @param id サービス登録コンテナの識別子
     * @return 生成されたサービスコンテキストの集合
     * @throws Exception サービスコンテキストの生成に失敗した場合
     * @since 1.0
     */
    Set<ServiceContext> make(String id) throws Exception;
}
