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

import org.yggdracity.context.cache.CacheHolder;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * サービスのロード結果を保持するキャッシュです。
 *
 * <p>サービスJARをキーとして {@link CachedService} を保持し、
 * 複数のスレッドから安全にアクセスできるよう
 * {@link ConcurrentHashMap} を使用します。</p>
 *
 * @since 1.0
 */
public class ServiceCacheHolder implements CacheHolder<File, CachedService> {

    private final Map<File, CachedService> cached;

    /**
     * サービスキャッシュを生成します。
     *
     * @since 1.0
     */
    public ServiceCacheHolder() {
        this.cached = new ConcurrentHashMap<>();
    }

    /**
     * 指定されたサービスJARに対応するキャッシュを取得します。
     *
     * @param key サービスJAR
     * @return キャッシュされたサービス、存在しない場合は {@code null}
     * @since 1.0
     */
    @Override
    public CachedService get(final File key) {
        return this.cached.get(key);
    }

    /**
     * 指定されたサービスJARに対応するサービスをキャッシュします。
     *
     * @param key サービスJAR
     * @param value キャッシュするサービス
     * @since 1.0
     */
    @Override
    public void put(final File key, final CachedService value) {
        this.cached.put(key, value);
    }

}
