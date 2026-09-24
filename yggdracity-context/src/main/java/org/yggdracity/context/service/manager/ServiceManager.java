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

package org.yggdracity.context.service.manager;

import org.yggdracity.context.service.container.ServiceRegisterContainer;

/**
 * サービスの自動ロードを管理するインターフェースです。
 *
 * <p>サービスの自動ロード処理の開始および一時停止、
 * サービス登録コンテナの取得を提供します。</p>
 *
 * <p>{@link #start()} および {@link #pause()} は、
 * サービス自体のライフサイクルではなく、
 * サービスを自動的にロードするスケジューラの
 * 開始および一時停止を管理します。</p>
 *
 * @see ServiceRegisterContainer
 * @since 1.0
 */
public interface ServiceManager {
    /**
     * サービスの自動ロードを開始します。
     *
     * @since 1.0
     */
    void start();

    /**
     * サービスの自動ロードを一時停止します。
     *
     * <p>ロード済みのサービスは維持されます。</p>
     *
     * @since 1.0
     */
    void pause();

    /**
     * サービス登録コンテナを取得します。
     *
     * @return サービス登録コンテナ
     * @since 1.0
     */
    ServiceRegisterContainer getContainer();
}
