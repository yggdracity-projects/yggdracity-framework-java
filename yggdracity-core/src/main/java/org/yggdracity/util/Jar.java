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

package org.yggdracity.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * JARファイルを操作するためのユーティリティです。
 *
 * <p>JARファイルに付与された署名から証明書を取得する機能を提供します。</p>
 *
 * @since 1.0
 */
public final class Jar {

    private Jar() {
    }

    /**
     * 指定されたJARファイルから署名に使用された証明書を取得します。
     *
     * <p>JARファイルの署名検証を有効にしてファイルを開き、
     * 署名対象となるエントリを読み込んだ後に証明書を取得します。</p>
     *
     * @param path JARファイルのパス
     * @return 署名に使用された証明書の一覧
     * @throws IOException JARファイルの読み込みに失敗した場合
     * @since 1.0
     */
    public static List<Certificate> getCertificates(final Path path) throws IOException {
        Objects.requireNonNull(path, "path");

        try (final JarFile jarFile = new JarFile(path.toFile(), true)) {
            final Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();

                if (entry.isDirectory() || entry.getName().startsWith("META-INF/")) {
                    continue;
                }

                try (final InputStream input = jarFile.getInputStream(entry)) {
                    input.transferTo(java.io.OutputStream.nullOutputStream());
                }

                final Certificate[] certificates = entry.getCertificates();

                if (certificates != null && certificates.length > 0) {
                    return List.of(certificates);
                }
            }
        }

        return List.of();
    }
}
