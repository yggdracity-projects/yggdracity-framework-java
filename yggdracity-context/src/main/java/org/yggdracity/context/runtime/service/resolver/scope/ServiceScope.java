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
 * サービスのインスタンススコープを定義します。
 *
 * <p>サービスのスコープに応じて、サービスインスタンスの生成および
 * ライフサイクルが決まります。</p>
 *
 * @since 1.0
 */
public enum ServiceScope {

    /**
     * サービスごとに単一のインスタンスを使用します。
     *
     * <p>スコープの生成時に生成されたインスタンスを保持し、
     * 以降の利用で同じインスタンスを使用します。</p>
     *
     * @since 1.0
     */
    SINGLETON,

    /**
     * サービスを利用するたびに新しいインスタンスを生成します。
     *
     * @since 1.0
     */
    PROTOTYPE,

    /**
     * スレッドごとにサービスインスタンスを生成します。
     *
     * <p>同一スレッドからの利用では同じインスタンスを使用し、
     * 異なるスレッドでは異なるインスタンスを使用します。</p>
     *
     * @since 1.0
     */
    THREAD,

    /**
     * リクエストごとにサービスインスタンスを生成します。
     *
     * <p>1つのリクエストの処理中は同じインスタンスを使用し、
     * リクエストが変わると新しいインスタンスを使用します。</p>
     *
     * @since 1.0
     */
    REQUEST
}