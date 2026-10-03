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

package org.yggdracity.context.cache;

/**
 * キーと値の組み合わせをキャッシュとして保持するためのインターフェースです。
 *
 * <p>キャッシュされた値の取得および登録を提供します。</p>
 *
 * @param <K> キャッシュのキーの型
 * @param <V> キャッシュする値の型
 * @since 1.0
 */
public interface CacheHolder<K, V> {

    /**
     * 指定されたキーに対応するキャッシュ値を取得します。
     *
     * @param key キャッシュのキー
     * @return キーに対応するキャッシュ値。
     * キャッシュが存在しない場合は {@code null}
     * @since 1.0
     */
    V get(K key);

    /**
     * 指定されたキーに値をキャッシュします。
     *
     * <p>同じキーにすでに値が存在する場合は、その値を置き換えます。</p>
     *
     * @param key   キャッシュのキー
     * @param value キャッシュする値
     * @since 1.0
     */
    void put(K key, V value);

}
