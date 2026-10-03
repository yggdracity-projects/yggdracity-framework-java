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

package org.yggdracity.context.validator.certificate.none;

import org.yggdracity.context.validator.certificate.AbstractCertificateValidator;
import org.yggdracity.context.validator.certificate.CertificateValidator;

import java.security.cert.Certificate;
import java.util.List;

/**
 * 証明書の検証を行わない {@link CertificateValidator} の実装です。
 *
 * <p>検証対象の証明書に関係なく、常に検証成功として扱います。
 * 証明書の検証を必要としない環境や、開発・テスト環境などで
 * 使用することを想定しています。</p>
 *
 * <p><strong>この実装を使用すると、証明書の信頼性が検証されないため、
 * 信頼できない証明書を使用した検証対象も有効として扱われます。
 * 本番環境など、証明書の信頼性を確保する必要がある環境では、
 * 適切な証明書検証を行う実装を使用してください。</strong></p>
 *
 * @see AbstractCertificateValidator
 * @see Certificate
 * @since 1.0
 */
public class NoOpCertificateValidator extends AbstractCertificateValidator {

    /**
     * 証明書の検証を行わず、常に検証成功として扱います。
     *
     * @param certificates 検証対象の証明書一覧
     * @return 常に {@code true}
     * @since 1.0
     */
    @Override
    public final boolean isValid(final List<Certificate> certificates) {
        return true;
    }
}
