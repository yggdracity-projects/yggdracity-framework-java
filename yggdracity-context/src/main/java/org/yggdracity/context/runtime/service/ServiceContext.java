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

package org.yggdracity.context.runtime.service;

import org.yggdracity.context.runtime.service.annotation.ServiceDefinition;
import org.yggdracity.context.runtime.service.resolver.scope.ServiceScope;
import org.yggdracity.context.runtime.service.factory.ServiceFactory;

import java.io.IOException;
import java.net.URLClassLoader;

/**
 * サービスと、そのサービスに関連するメタデータおよびクラスローダーを保持します。
 *
 * <p>動的にロードされたサービスの生成に使用するファクトリと、
 * そのサービスに関連するメタデータおよびクラスローダーを
 * 一つのコンテキストとして管理します。</p>
 *
 * <p>サービスを更新または破棄する場合は、{@link #close()} を呼び出して
 * 使用しているクラスローダーを解放してください。</p>
 *
 * @param factory     サービスインスタンスを生成するファクトリ
 * @param id          アプリケーション識別子
 * @param tag         サービスのタグ
 * @param name        サービス名
 * @param version     サービスのバージョン
 * @param classLoader サービスのロードに使用するクラスローダー
 * @param scope       サービスのスコープ
 * @see ServiceDefinition
 * @see ServiceDefinitionProperties
 * @since 1.0
 */
public record ServiceContext(
        ServiceFactory factory,
        String id,
        String tag,
        String name,
        String version,
        ServiceScope scope,
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

    /**
     * サービスを一意に識別するキーを取得します。
     *
     * <p>アプリケーション識別子、サービス名、タグ、バージョンを
     * 組み合わせてキーを生成します。</p>
     *
     * @return サービスを識別するキー
     * @since 1.0
     */
    public String key() {
        return this.id + "." + this.name + "." + this.tag + "." + this.version;
    }
}
