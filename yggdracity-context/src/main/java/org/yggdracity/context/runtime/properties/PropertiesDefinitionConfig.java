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

import org.yggdracity.context.runtime.resolver.Definition;
import org.yggdracity.context.validator.Security;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 動的プロパティの解決に使用する定義情報を保持します。
 *
 * <p>プロパティの名前、バージョン、セキュリティ検証、
 * ファイル形式および読み込み対象から除外するプロパティを定義します。</p>
 *
 * @param name       プロパティ定義の名前
 * @param version    解決対象のバージョン
 * @param security   セキュリティ検証の設定
 * @param extension  プロパティファイルの拡張子
 * @param exclusions 動的に読み込む対象から除外するプロパティ
 * @since 1.0
 */
public record PropertiesDefinitionConfig(String name, String version, Security security, Extension extension,
                                         List<String> exclusions) implements Definition {


    /**
     * 動的に読み込む対象から除外するプロパティを取得します。
     *
     * @return 除外するプロパティの一覧
     * @since 1.0
     */
    @Override
    public List<String> exclusions() {
        return Collections.unmodifiableList(exclusions);
    }

    /**
     * プロパティファイルの形式を定義します。
     *
     * @since 1.0
     */
    public enum Extension {

        /** YAML形式のプロパティファイルです。 */
        YAML("yml", "yaml"),

        /** Javaプロパティ形式のファイルです。 */
        PROPERTY("properties", "xml");


        private final Set<String> values;

        Extension(final String... values) {
            this.values = Set.of(values);
        }

        /**
         * この拡張子に対応する値を取得します。
         *
         * @return 対応する拡張子の集合
         * @since 1.0
         */
        public Set<String> getValues() {
            return values;
        }

    }
}
