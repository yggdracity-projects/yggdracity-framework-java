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

package org.yggdracity.context.validator.checksum.none;

import org.yggdracity.context.validator.Security;
import org.yggdracity.context.validator.checksum.AbstractChecksumValidator;

import java.nio.file.Path;

/**
 * チェックサムの検証を行わない実装です。
 *
 * <p>チェックサムによるファイルの完全性検証を必要としない環境や、
 * 開発・テスト環境などで使用することを想定しています。</p>
 *
 * <p><strong>この実装を使用すると、ファイルの完全性が検証されないため、
 * チェックサムが設定されていないファイルや、設定されたチェックサムと
 * 一致しないファイルも検証成功として扱われます。</strong></p>
 *
 * @see AbstractChecksumValidator
 * @since 1.0
 */
public class NoOpChecksumValidator extends AbstractChecksumValidator {

    /**
     * チェックサムの検証を行わず、常に検証成功として扱います。
     *
     * @param file 検証対象のファイル
     * @return 常に {@code true}
     * @since 1.0
     */
    @Override
    public final boolean isValid(final Path file) {
        return true;
    }

}
