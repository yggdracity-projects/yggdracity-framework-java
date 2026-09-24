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

package org.yggdracity.context.service;

import java.lang.annotation.*;

/**
 * 動的にロードするサービスの定義を指定するアノテーションです。
 *
 * <p>サービスを一意に識別するID、サービス名、所属するグループ、
 * バージョンを指定します。</p>
 *
 * <p>このアノテーションは、動的にロードするサービスの実装クラスに
 * 付与することを想定しています。</p>
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
     * サービスが所属するグループを取得します。
     *
     * @return サービスグループ
     * @since 1.0
     */
    String group() default "";

    /**
     * サービスのバージョンを取得します。
     *
     * @return サービスバージョン
     * @since 1.0
     */
    String version() default "";
}
