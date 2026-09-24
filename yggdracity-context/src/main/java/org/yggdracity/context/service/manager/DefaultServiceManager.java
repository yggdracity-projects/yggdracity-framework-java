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

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.yggdracity.context.service.ServiceContext;
import org.yggdracity.context.service.ServiceDefinitionProperties;
import org.yggdracity.context.service.container.ServiceRegisterContainer;


/**
 * サービスの自動ロードを管理するデフォルト実装です。
 *
 * <p>{@link ServiceRegisterContainer} を使用して、
 * サービスの自動ロード処理の開始および一時停止、
 * サービス登録コンテナの取得を行います。</p>
 *
 * <p>{@link #start()} および {@link #pause()} は、
 * サービス自体のライフサイクルではなく、
 * サービスを自動的にロードするスケジューラの
 * 開始および一時停止を管理します。</p>
 *
 * @see ServiceManager
 * @see ServiceRegisterContainer
 * @since 1.0
 */
@EnableConfigurationProperties(ServiceDefinitionProperties.class)
public class DefaultServiceManager implements ServiceManager {

    private final ServiceRegisterContainer container;

    /**
     * サービス登録コンテナを指定してサービスマネージャーを生成します。
     *
     * @param container サービスの登録および自動ロードを管理するコンテナ
     * @since 1.0
     */
    public DefaultServiceManager(final ServiceRegisterContainer container) {
        this.container = container;
    }

    @Override
    public void start() {
        this.container.start();
    }

    @Override
    public void pause() {
        this.container.pause();
    }

    @Override
    public ServiceRegisterContainer getContainer() {
        return this.container;
    }

}
