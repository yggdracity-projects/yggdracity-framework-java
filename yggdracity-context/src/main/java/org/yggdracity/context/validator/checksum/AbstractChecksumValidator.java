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

package org.yggdracity.context.validator.checksum;

import org.yggdracity.context.validator.Security;

import java.nio.file.Path;
import java.util.Properties;

/**
 * チェックサム検証処理の共通機能を提供する基底クラスです。
 *
 * <p>チェックサム検証に使用する設定を保持し、
 * 各チェックサム検証実装で共通して利用できるようにします。</p>
 *
 * @since 1.0
 */
public abstract class AbstractChecksumValidator implements ChecksumValidator {

    /**
     * チェックサム検証に使用する設定です。
     */
    protected Security.ChecksumParameter parameter;

    /**
     * チェックサム検証で使用するプロパティです。
     */
    protected Properties properties;


    @Override
    public void setParameter(final Security.ChecksumParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * チェックサム検証で使用するプロパティを設定します。
     *
     * @param properties チェックサム検証で使用するプロパティ
     * @since 1.0
     */
    @Override
    public final void setProperties(final Properties properties) {
        this.properties = properties;
    }
}
