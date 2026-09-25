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

import java.io.IOException;
import java.net.URLClassLoader;
import java.time.LocalDateTime;

/**
 * サービスインスタンスと、そのサービスに関連するメタデータおよびクラスローダーを保持します。
 *
 * <p>動的にロードされたサービスのインスタンスと、
 * そのサービスをロードするために使用した {@link URLClassLoader} を
 * 一つのコンテキストとして管理します。</p>
 *
 * <p>サービスを更新または破棄する場合は、{@link #close()} を呼び出して
 * 使用しているクラスローダーを解放してください。</p>
 *
 * @param service        サービスインスタンス
 * @param id             アプリケーション識別子
 * @param name           サービス名
 * @param group          サービスが所属するグループ
 * @param version        サービスのバージョン
 * @param classLoader    サービスのロードに使用するクラスローダー
 * @see ServiceDefinition
 * @see ServiceDefinitionProperties
 * @since 1.0
 */
public record ServiceContext(
        Object service,
        String id,
        String name,
        String group,
        String version,
        URLClassLoader classLoader) {

    /**
     * サービスのロードに使用したクラスローダーを閉じます。
     *
     * <p>クラスローダーが保持しているリソースを解放します。
     * 動的にロードされたサービスを破棄する場合や、
     * 新しいサービスへ置き換える場合に呼び出します。</p>
     *
     * @throws IOException クラスローダーのクローズに失敗した場合
     * @since 1.0
     */
    public void close() throws IOException {
        this.classLoader.close();
    }
}
