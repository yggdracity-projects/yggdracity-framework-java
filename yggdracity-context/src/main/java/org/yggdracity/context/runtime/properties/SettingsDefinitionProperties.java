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

package org.yggdracity.context.runtime.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 動的プロパティの読み込みに関する設定を保持します。
 *
 * <p>{@code yggdracity.runtime} 配下の設定を管理し、
 * プロパティファイルの配置先およびプロパティ定義を提供します。</p>
 *
 * @param setting 動的プロパティの設定
 * @since 1.0
 */
@ConfigurationProperties(prefix = "yggdracity.runtime")
public record SettingsDefinitionProperties(Settings setting) {

    /**
     * 動的プロパティの設定を保持します。
     *
     * <p>プロパティファイルの配置先となるベースディレクトリおよび
     * プロパティごとの定義を管理します。</p>
     *
     * @param base       プロパティファイルを配置するベースディレクトリ
     * @param definition プロパティごとの定義
     * @since 1.0
     */
    public record Settings(String base, Map<String, PropertiesDefinitionConfig> definition) {
    }
}
