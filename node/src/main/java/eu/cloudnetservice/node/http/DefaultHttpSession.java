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

import eu.cloudnetservice.driver.CloudNetDriver;
import eu.cloudnetservice.driver.permission.PermissionUser;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class DefaultHttpSession implements HttpSession {

  protected final String uniqueId;
  protected final UUID userId;
  protected final V2HttpAuthentication issuer;
  protected final Map<String, Object> properties;

  protected long expireTime;

  public DefaultHttpSession(long expireTime, @NonNull UUID userId, @NonNull V2HttpAuthentication issuer) {
    this(expireTime, UUID.randomUUID().toString(), userId, issuer);
  }

  public DefaultHttpSession(
    long expireTime,
    @NonNull String uniqueId,
    @NonNull UUID userId,
    @NonNull V2HttpAuthentication issuer
  ) {
    this(expireTime, uniqueId, userId, issuer, new HashMap<>());
  }

  public DefaultHttpSession(
    long expireTime,
    @NonNull String uniqueId,
    @NonNull UUID userId,
    @NonNull V2HttpAuthentication issuer,
    @NonNull Map<String, Object> properties
  ) {
    this.expireTime = expireTime;
    this.uniqueId = uniqueId;
    this.userId = userId;
    this.issuer = issuer;
    this.properties = properties;
  }

  @Override
  public long expireTime() {
    return this.expireTime;
  }

  @Override
  public long refreshFor(long liveMillis) {
    return this.expireTime += liveMillis;
  }

  @Override
  public @NonNull String uniqueId() {
    return this.uniqueId;
  }

  @Override
  public @NonNull UUID userId() {
    return this.userId;
  }

  @Override
  public PermissionUser user() {
    return CloudNetDriver.instance().permissionManagement().user(this.userId);
  }

  @Override
  public <T> T property(@NonNull String key) {
    return this.property(key, null);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T property(@NonNull String key, @Nullable T def) {
    return (T) this.properties.getOrDefault(key, def);
  }

  @Override
  public @NonNull HttpSession setProperty(@NonNull String key, @NonNull Object value) {
    this.properties.put(key, value);
    return this;
  }

  @Override
  public @NonNull HttpSession removeProperty(@NonNull String key) {
    this.properties.remove(key);
    return this;
  }

  @Override
  public boolean hasProperty(@NonNull String key) {
    return this.properties.containsKey(key);
  }

  @Override
  public @NonNull Map<String, Object> properties() {
    return this.properties;
  }

  @Override
  public @NonNull V2HttpAuthentication issuer() {
    return this.issuer;
  }
}
