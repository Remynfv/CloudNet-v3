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

package eu.cloudnetservice.wrapper.network.listener.message;

import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.event.events.channel.ChannelMessageReceiveEvent;
import eu.cloudnetservice.driver.event.events.group.GroupConfigurationAddEvent;
import eu.cloudnetservice.driver.event.events.group.GroupConfigurationRemoveEvent;
import eu.cloudnetservice.driver.network.def.NetworkConstants;
import eu.cloudnetservice.driver.service.GroupConfiguration;
import lombok.NonNull;

public final class GroupChannelMessageListener {

  private final EventManager eventManager;

  public GroupChannelMessageListener(@NonNull EventManager eventManager) {
    this.eventManager = eventManager;
  }

  @EventListener
  public void handle(@NonNull ChannelMessageReceiveEvent event) {
    if (event.channel().equals(NetworkConstants.INTERNAL_MSG_CHANNEL)) {
      switch (event.message()) {
        // add group
        case "add_group_configuration" -> {
          var configuration = event.content().readObject(GroupConfiguration.class);
          this.eventManager.callEvent(new GroupConfigurationAddEvent(configuration));
        }

        // remove group
        case "remove_group_configuration" -> {
          var configuration = event.content().readObject(GroupConfiguration.class);
          this.eventManager.callEvent(new GroupConfigurationRemoveEvent(configuration));
        }

        // none of our business
        default -> {
        }
      }
    }
  }
}
