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

package org.yggdracity.context.runtime.model;

import java.io.IOException;
import java.net.URLClassLoader;


/**
 * 動的にロードされたモデルと、そのロードに必要な情報を保持するコンテキストです。
 *
 * <p>モデルの識別子、モデルJARの名前、バージョンおよびロードに使用した
 * クラスローダーを保持します。</p>
 *
 * @param id          アプリケーション識別子
 * @param name        モデルJARの名前
 * @param version     モデルのバージョン
 * @param classLoader モデルのロードに使用したクラスローダー
 * @see ModelDefinitionProperties
 * @since 1.0
 */
public record ModelContext(String id, String name, String version, URLClassLoader classLoader) {
    /**
     * モデルのロードに使用したクラスローダーを閉じます。
     *
     * <p>クラスローダーが保持しているリソースを解放します。
     * 動的にロードされたモデルを破棄する場合や、
     * 新しいモデルへ置き換える場合に呼び出します。</p>
     *
     * @throws IOException クラスローダーのクローズに失敗した場合
     * @since 1.0
     */
    public void close() throws IOException {
        this.classLoader.close();
    }
}
