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
import eu.cloudnetservice.driver.event.events.permission.PermissionAddGroupEvent;
import eu.cloudnetservice.driver.event.events.permission.PermissionAddUserEvent;
import eu.cloudnetservice.driver.event.events.permission.PermissionDeleteGroupEvent;
import eu.cloudnetservice.driver.event.events.permission.PermissionDeleteUserEvent;
import eu.cloudnetservice.driver.event.events.permission.PermissionSetGroupsEvent;
import eu.cloudnetservice.driver.event.events.permission.PermissionUpdateGroupEvent;
import eu.cloudnetservice.driver.event.events.permission.PermissionUpdateUserEvent;
import eu.cloudnetservice.driver.network.def.NetworkConstants;
import eu.cloudnetservice.driver.permission.PermissionGroup;
import eu.cloudnetservice.driver.permission.PermissionManagement;
import eu.cloudnetservice.driver.permission.PermissionUser;
import java.util.Collection;
import lombok.NonNull;

public final class PermissionChannelMessageListener {

  private final EventManager eventManager;
  private final PermissionManagement permissionManagement;

  public PermissionChannelMessageListener(
    @NonNull EventManager eventManager,
    @NonNull PermissionManagement management
  ) {
    this.eventManager = eventManager;
    this.permissionManagement = management;
  }

  @EventListener
  public void handleChannelMessage(@NonNull ChannelMessageReceiveEvent event) {
    if (event.channel().equals(NetworkConstants.INTERNAL_MSG_CHANNEL) && event.message()
      .startsWith("permissions_")) {
      // permission message - handler
      switch (event.message().replaceFirst("permissions_", "")) {
        // user add
        case "add_user" -> this.eventManager.callEvent(new PermissionAddUserEvent(
          this.permissionManagement,
          event.content().readObject(PermissionUser.class)));

        // user update
        case "update_user" -> this.eventManager.callEvent(new PermissionUpdateUserEvent(
          this.permissionManagement,
          event.content().readObject(PermissionUser.class)));

        // user remove
        case "delete_user" -> this.eventManager.callEvent(new PermissionDeleteUserEvent(
          this.permissionManagement,
          event.content().readObject(PermissionUser.class)));

        // group add
        case "add_group" -> {
          // read the group
          var group = event.content().readObject(PermissionGroup.class);
          this.eventManager.callEvent(new PermissionAddGroupEvent(this.permissionManagement, group));
        }

        // group update
        case "update_group" -> {
          // read the group
          var group = event.content().readObject(PermissionGroup.class);
          this.eventManager.callEvent(new PermissionUpdateGroupEvent(this.permissionManagement, group));
        }

        // group delete
        case "delete_group" -> {
          // read the group
          var group = event.content().readObject(PermissionGroup.class);
          this.eventManager.callEvent(new PermissionDeleteGroupEvent(this.permissionManagement, group));
        }

        // group set
        case "set_groups" -> {
          // read the group
          Collection<PermissionGroup> groups = event.content().readObject(PermissionGroup.COL_GROUPS);
          this.eventManager.callEvent(new PermissionSetGroupsEvent(this.permissionManagement, groups));
        }
        default -> throw new IllegalArgumentException("Unhandled permission message " + event.message());
      }
    }
  }
}
