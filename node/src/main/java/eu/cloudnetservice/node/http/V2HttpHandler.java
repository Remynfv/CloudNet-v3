/*
 * Copyright 2019-2022 CloudNetService team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.cloudnetservice.node.http;

import eu.cloudnetservice.common.document.gson.JsonDocument;
import eu.cloudnetservice.driver.network.http.HttpContext;
import eu.cloudnetservice.driver.network.http.HttpRequest;
import eu.cloudnetservice.driver.network.http.HttpResponse;
import eu.cloudnetservice.driver.network.http.HttpResponseCode;
import eu.cloudnetservice.node.Node;
import eu.cloudnetservice.node.config.Configuration;
import eu.cloudnetservice.node.config.RestConfiguration;
import java.nio.charset.StandardCharsets;
import lombok.NonNull;

public abstract class V2HttpHandler {

  protected final RestConfiguration restConfiguration;

  public V2HttpHandler() {
    this(Node.instance().config().restConfiguration());
  }

  public V2HttpHandler(@NonNull RestConfiguration restConfiguration) {
    this.restConfiguration = restConfiguration;
  }

  protected void send403(@NonNull HttpContext context, @NonNull String reason) {
    this.response(context, HttpResponseCode.FORBIDDEN)
      .body(this.failure().append("reason", reason).toString().getBytes(StandardCharsets.UTF_8))
      .context()
      .closeAfter(true)
      .cancelNext();
  }

  protected void send401(@NonNull HttpContext context, @NonNull String reason) {
    this.response(context, HttpResponseCode.UNAUTHORIZED)
      .body(this.failure().append("reason", reason).toString().getBytes(StandardCharsets.UTF_8))
      .context()
      .closeAfter(true)
      .cancelNext();
  }

  protected @NonNull HttpResponse ok(@NonNull HttpContext context) {
    return this.response(context, HttpResponseCode.OK);
  }

  protected @NonNull HttpResponse badRequest(@NonNull HttpContext context) {
    return this.response(context, HttpResponseCode.BAD_REQUEST);
  }

  protected @NonNull HttpResponse notFound(@NonNull HttpContext context) {
    return this.response(context, HttpResponseCode.NOT_FOUND);
  }

  protected @NonNull HttpResponse response(@NonNull HttpContext context, @NonNull HttpResponseCode statusCode) {
    return context.response()
      .status(statusCode)
      .header("Content-Type", "application/json")
      .header("Access-Control-Allow-Origin", this.restConfiguration.corsPolicy());
  }

  protected @NonNull JsonDocument body(@NonNull HttpRequest request) {
    return JsonDocument.fromJsonBytes(request.body());
  }

  protected @NonNull JsonDocument success() {
    return JsonDocument.newDocument("success", true);
  }

  protected @NonNull JsonDocument failure() {
    return JsonDocument.newDocument("success", false);
  }

  protected @NonNull Node node() {
    return Node.instance();
  }

  protected @NonNull Configuration nodeConfig() {
    return this.node().config();
  }
}
