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

/**
 * サービス定義が不正な場合にスローされる例外です。
 *
 * @since 1.0
 */
public class InvalidServiceDefinitionException extends RuntimeException {

    /**
     * 指定されたサービスの定義が不正であることを示す例外を生成します。
     *
     * @param service サービス
     */
    public InvalidServiceDefinitionException(final Object service) {
        super("Service definition annotation is not supported: " + service.getClass().getName());
    }
}
