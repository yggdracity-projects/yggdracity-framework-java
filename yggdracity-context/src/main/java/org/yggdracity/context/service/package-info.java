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

/**
 * Yggdracityのサービスコンテキストおよび動的サービスローダーを提供します。
 *
 * <p>本パッケージは、サービスをアプリケーションの再起動なしに動的にロード、
 * 更新および管理するための基本的なAPIを提供します。</p>
 *
 * <p>サービス実装は {@link org.yggdracity.context.service.ServiceDefinition}
 * を付与し、サービス名、グループおよびバージョンを定義します。</p>
 *
 * <p>サービスJARは設定されたベースディレクトリに配置し、
 * {@link org.yggdracity.context.service.container.ServiceRegisterContainer}
 * によって自動的にロードおよび更新されます。</p>
 *
 * <h2>サービスの定義</h2>
 *
 * <p>サービス実装には {@code ServiceDefinition} を付与します。</p>
 *
 * <pre>{@code
 * @ServiceDefinition(
 *     name = "sample",
 *     group = "example",
 *     version = "1.0.0"
 * )
 * public class SampleServiceImpl implements SampleService {
 *
 *     @Override
 *     public void call() {
 *         // サービス処理
 *     }
 * }
 * }</pre>
 *
 * <h2>サービスのロード</h2>
 *
 * <p>サービスJARを設定されたサービスディレクトリに配置すると、
 * {@link org.yggdracity.context.service.factory.ServiceFactory} によって
 * {@link java.util.ServiceLoader} を使用してサービス実装がロードされます。</p>
 *
 * <p>サービスの自動ロードは
 * {@link org.yggdracity.context.service.container.ServiceRegisterContainer}
 * によって管理され、アプリケーション起動時および設定されたスケジュールに
 * 従ってサービスがロードされます。</p>
 *
 * <h2>サービスの取得</h2>
 *
 * <p>{@link org.yggdracity.context.service.manager.ServiceManager} から
 * サービス登録コンテナを取得し、サービス名を指定してサービスを取得できます。</p>
 *
 * <pre>{@code
 * final SampleService service =
 *         manager.getContainer().getService("sample");
 *
 * service.call();
 * }</pre>
 *
 * <p>{@code getService(String)} はジェネリックメソッドであるため、
 * 代入先の型からサービスの型が推論されます。</p>
 *
 * <h2>設定</h2>
 *
 * <p>サービスの配置場所、ロードスケジュール、ロード対象バージョンおよび
 * セキュリティ検証は、{@code yggdracity.auto-loader} 配下に設定します。</p>
 *
 * <pre>
 * yggdracity:
 *   auto-loader:
 *     schedule: &lt;cron expression&gt;
 *     service:
 *       base: /opt/yggdracity/services
 *       definition:
 *         sample:
 *           version: latest
 *           security:
 *             ...
 * </pre>
 *
 * <p>サービスのバージョンは設定によってロード対象を選択し、
 * ロードされたサービス自身の名前、グループおよびバージョンは
 * {@code ServiceDefinition} から取得します。</p>
 *
 * @since 1.0
 */
package org.yggdracity.context.service;