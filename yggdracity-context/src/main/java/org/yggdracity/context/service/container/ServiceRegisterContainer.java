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

package org.yggdracity.context.service.container;

import org.yggdracity.context.service.ServiceContext;

/**
 * 動的にロードするサービスの登録および管理を行うコンテナです。
 *
 * <p>サービスの自動ロードの有効化および無効化、
 * 登録されているサービスおよびサービスコンテキストの取得を提供します。</p>
 *
 * <p>{@link #start()}および{@link #pause()}は、
 * 登録されているサービス自体のライフサイクルを管理するものではなく、
 * サービスの自動ロード処理を有効化または無効化します。</p>
 *
 * @since 1.0
 */
public interface ServiceRegisterContainer {
    /**
     * サービスの自動ロードを開始します。
     *
     * <p>自動ロードを開始した後、スケジュールに従って
     * サービスのロード処理が実行されます。</p>
     *
     * @since 1.0
     */
    void start();

    /**
     * サービスの自動ロードを一時停止します。
     *
     * <p>すでに登録されているサービスは維持されます。</p>
     *
     * @since 1.0
     */
    void pause();

    /**
     * 指定されたサービス名のサービスを取得します。
     *
     * @param name サービスの名前
     * @param <S>  サービスの型
     * @return 指定されたサービス名のサービスインスタンス
     * @since 1.0
     */
    <S> S getService(String name);

    /**
     * 指定されたサービス名のサービスコンテキストを取得します。
     *
     * @param name サービスの名前
     * @return 指定されたサービス名のサービスコンテキスト
     * @since 1.0
     */
    ServiceContext getContext(String name);
}
