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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.yggdracity.context.validator.Security;

import java.util.Map;

/**
 * 動的にロードするサービスの設定を保持します。
 *
 * <p>{@code yggdracity.auto-loader} 配下の設定を管理し、
 * サービスのロードスケジュール、配置場所および
 * サービスごとの定義を提供します。</p>
 *
 * @param service サービスに関する設定
 * @since 1.0
 */
@ConfigurationProperties(prefix = "yggdracity.auto-loader")
public record ServiceDefinitionProperties(Service service) {
    /**
     * 動的にロードするサービスに関する設定を保持します。
     *
     * <p>サービスJARのロードスケジュール、配置先となるベースディレクトリおよび
     * サービスごとのバージョン選択とセキュリティ検証に関する設定を管理します。</p>
     *
     * @param schedule   サービスをロードするスケジュール（cron式）
     * @param base       サービスJARを配置するベースディレクトリ
     * @param definition サービスごとのバージョンおよびセキュリティ検証に関する設定
     * @since 1.0
     */
    public record Service(String schedule, String base, Map<String, Definition> definition) {

        /**
         * 指定されたキーに対応するサービスバージョンを取得します。
         *
         * @param key サービス定義を識別するキー
         * @return サービスバージョン
         * @since 1.0
         */
        public String version(final String key) {
            return this.getDefinition(key).version();
        }

        /**
         * 指定されたキーに対応するサービスのセキュリティ検証設定を取得します。
         *
         * @param key サービス定義を識別するキー
         * @return サービスのセキュリティ検証設定
         * @since 1.0
         */
        public Security security(final String key) {
            return this.getDefinition(key).security();
        }

        /**
         * 指定されたキーに対応するサービス定義を取得します。
         *
         * @param key サービス定義を識別するキー
         * @return 指定されたキーに対応するサービス定義
         * @throws IllegalArgumentException 指定されたキーに対応するサービス定義が存在しない場合
         * @since 1.0
         */
        private Definition getDefinition(final String key) {
            final Definition definition = this.definition.get(key);
            if (definition == null) {
                throw new IllegalArgumentException("Unknown service definition: " + key);
            }
            return definition;
        }

        /**
         * サービスごとのロード定義を保持します。
         *
         * <p>ロード対象となるサービスバージョンおよび
         * サービスJARのセキュリティ検証に関する設定を管理します。</p>
         *
         * @param version  ロード対象となるサービスのバージョン
         * @param security サービスJARのセキュリティ検証に関する設定
         * @since 1.0
         */
        public record Definition(String version, Security security) {
        }

    }
}
