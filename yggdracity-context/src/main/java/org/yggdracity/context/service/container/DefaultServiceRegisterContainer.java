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

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.yggdracity.context.service.ServiceContext;
import org.yggdracity.context.service.factory.ServiceFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * サービスの登録および自動ロードを管理するデフォルト実装です。
 *
 * <p>Spring Bootのスケジューラを使用して{@link ServiceFactory}から
 * サービスコンテキストを取得し、サービス名をキーとして管理します。</p>
 *
 * <p>{@link #start()}により自動ロードを開始し、
 * {@link #pause()}により自動ロードを一時停止します。</p>
 *
 * <p>サービスコンテキストが再ロードされた場合は、
 * 新しいサービスコンテキストに置き換え、
 * 置き換え前の{@link ServiceContext}をクローズします。</p>
 *
 * @see ServiceRegisterContainer
 * @see ServiceFactory
 * @see ServiceContext
 * @since 1.0
 */
public class DefaultServiceRegisterContainer implements ServiceRegisterContainer {

    private volatile boolean started = true;
    private final String id;
    private final ServiceFactory factory;
    private final Map<String, ServiceContext> services = new ConcurrentHashMap<>();

    /**
     * アプリケーション識別子およびサービスファクトリを指定して
     * サービス登録コンテナを生成します。
     *
     * @param id アプリケーションを識別するID
     * @param factory サービスコンテキストの生成に使用するサービスファクトリ
     * @since 1.0
     */
    public DefaultServiceRegisterContainer(final String id, final ServiceFactory factory) {
        this.id = id;
        this.factory = factory;
    }

    /**
     * アプリケーションの起動完了時にサービスの初回ロードを実行します。
     *
     * <p>{@link ApplicationReadyEvent}を契機として実行されるため、
     * {@link Scheduled}で指定されたスケジュール時刻を待たずに
     * 初回のサービスロードを実行します。</p>
     *
     * <p>初回ロード以降のサービスロードは、
     * {@link #schedule()}によって指定されたスケジュールに従って実行されます。</p>
     *
     * @throws Exception サービスのロードに失敗した場合
     * @since 1.0
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initialize() throws Exception {
        this.schedule();
    }


    /**
     * サービスの自動ロード処理を実行します。
     *
     * <p>自動ロードが有効な場合、{@link ServiceFactory}から
     * サービスコンテキストの集合を取得し、
     * 各サービスコンテキストをサービス名をキーとして登録します。</p>
     *
     * <p>すでに同一のサービス名のサービスコンテキストが登録されている場合は、
     * 新しく取得したサービスコンテキストに置き換え、
     * 以前のサービスコンテキストをクローズします。</p>
     *
     * @throws Exception サービスコンテキストの生成またはクローズに失敗した場合
     * @since 1.0
     */
    @Scheduled(cron = "${yggdracity.auto-loader.schedule}")
    public final void schedule() throws Exception {
        if (this.started) {
            final Set<ServiceContext> contexts = this.factory.make(this.id);
            for (final ServiceContext context : contexts) {

                final String name = context.name();
                final ServiceContext oldContext = this.services.put(name, context);
                if (oldContext != null) {
                    oldContext.close();
                }
            }
        }
    }

    /**
     * サービスの自動ロードを開始します。
     *
     * <p>本メソッドはサービス自体のライフサイクルを開始するものではなく、
     * 自動ロード処理を実行するスケジューラを有効にします。</p>
     *
     * @since 1.0
     */
    @Override
    public void start() {
        this.started = true;
    }

    /**
     * サービスの自動ロードを一時停止します。
     *
     * <p>本メソッドはサービス自体のライフサイクルを停止するものではなく、
     * 自動ロード処理を実行するスケジューラを無効にします。</p>
     *
     * <p>すでに登録されているサービスは維持されます。</p>
     *
     * @since 1.0
     */
    @Override
    public void pause() {
        this.started = false;
    }

    /**
     * 指定されたサービス名のサービスを取得します。
     *
     * @param name 取得するサービスの名前
     * @return 指定されたサービス名のサービスインスタンス
     * @since 1.0
     */
    @Override
    @SuppressWarnings("unchecked")
    public <S> S getService(final String name) {
        return (S) this.services.get(name).service();
    }

    /**
     * 指定されたサービス名のサービスコンテキストを取得します。
     *
     * @param name 取得するサービスの名前
     * @return 指定されたサービス名のサービスコンテキスト
     * @since 1.0
     */
    @Override
    public ServiceContext getContext(final String name) {
        return this.services.get(name);
    }
}
