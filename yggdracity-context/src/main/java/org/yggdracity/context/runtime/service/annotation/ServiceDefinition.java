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

package org.yggdracity.context.runtime.service.annotation;

import org.yggdracity.context.runtime.service.resolver.scope.ServiceScope;

import java.lang.annotation.*;

/**
 * 動的にロードするサービスのメタデータを定義するアノテーションです。
 *
 * <p>サービス名、タグ、バージョンおよびインスタンススコープを指定します。
 * このアノテーションは、動的にロードするサービスの実装クラスに付与します。</p>
 *
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceDefinition {

    /**
     * サービス名を取得します。
     *
     * @return サービス名
     * @since 1.0
     */
    String name();

    /**
     * サービスのタグを取得します。
     *
     * @return サービスのタグ
     * @since 1.0
     */
    String tag() default "";

    /**
     * サービスのバージョンを取得します。
     *
     * @return サービスのバージョン
     * @since 1.0
     */
    String version() default "";

    /**
     * サービスのインスタンススコープを取得します。
     *
     * @return サービスのインスタンススコープ
     * @since 1.0
     */
    ServiceScope scope() default ServiceScope.SINGLETON;
}