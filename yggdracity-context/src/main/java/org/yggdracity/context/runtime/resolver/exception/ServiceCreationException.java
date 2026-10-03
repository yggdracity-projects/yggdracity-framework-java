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

package org.yggdracity.context.runtime.resolver.exception;

/**
 * サービスの生成に失敗した場合にスローされる例外です。
 *
 * @since 1.0
 */
public class ServiceCreationException extends RuntimeException {

    /**
     * 指定された例外を原因としてサービス生成例外を生成します。
     *
     * @param exception サービス生成時に発生した例外
     * @since 1.0
     */
    public ServiceCreationException(final Exception exception) {
        super(exception);
    }

    /**
     * 指定されたメッセージでサービス生成例外を生成します。
     *
     * @param message エラーメッセージ
     * @since 1.0
     */
    public ServiceCreationException(final String message) {
        super(message);
    }
}
