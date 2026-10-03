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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * キャッシュを保持する標準実装です。
 *
 * <p>{@link ConcurrentHashMap} を使用してキャッシュを保持し、
 * 複数のスレッドから安全にアクセスできます。</p>
 *
 * @param <K> キャッシュキーの型
 * @param <V> キャッシュ値の型
 * @since 1.0
 */
public final class StandardCacheHolder<K, V> implements CacheHolder<K, V> {

    private final Map<K, V> cached;

    /**
     * キャッシュホルダーを生成します。
     *
     * @since 1.0
     */
    public StandardCacheHolder() {
        this.cached = new ConcurrentHashMap<>();
    }

    /**
     * 指定されたキーに対応するキャッシュ値を取得します。
     *
     * @param key キャッシュキー
     * @return キャッシュ値。指定されたキーに対応する値が存在しない場合は {@code null}
     * @since 1.0
     */
    @Override
    public V get(final K key) {
        return this.cached.get(key);
    }

    /**
     * 指定されたキーにキャッシュ値を登録します。
     *
     * @param key   キャッシュキー
     * @param value キャッシュ値
     * @since 1.0
     */
    @Override
    public void put(final K key, final V value) {
        this.cached.put(key, value);
    }
}
