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

package eu.cloudnetservice.node.template;

import eu.cloudnetservice.common.Nameable;
import eu.cloudnetservice.driver.service.ServiceTemplate;
import eu.cloudnetservice.driver.template.TemplateStorage;
import eu.cloudnetservice.driver.template.TemplateStorageProvider;
import eu.cloudnetservice.node.Node;
import java.util.Collection;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public class NodeTemplateStorageProvider implements TemplateStorageProvider {

  private final Node node;

  public NodeTemplateStorageProvider(@NonNull Node node) {
    this.node = node;
    this.node.rpcFactory()
      .newHandler(TemplateStorageProvider.class, this)
      .registerToDefaultRegistry();
  }

  @Override
  public @NonNull TemplateStorage localTemplateStorage() {
    var storage = this.templateStorage(ServiceTemplate.LOCAL_STORAGE);
    if (storage != null) {
      return storage;
    }

    throw new UnsupportedOperationException("The local storage was unregistered!");
  }

  @Override
  public @Nullable TemplateStorage templateStorage(@NonNull String storage) {
    return this.node.serviceRegistry().provider(TemplateStorage.class, storage);
  }

  @Override
  public @NonNull Collection<String> availableTemplateStorages() {
    return this.node.serviceRegistry().providers(TemplateStorage.class).stream().map(Nameable::name).toList();
  }
}
