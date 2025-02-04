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

package eu.cloudnetservice.modules.labymod.node;

import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.events.channel.ChannelMessageReceiveEvent;
import eu.cloudnetservice.modules.labymod.LabyModManagement;
import eu.cloudnetservice.modules.labymod.config.LabyModConfiguration;
import lombok.NonNull;

public class NodeLabyModListener {

  private final NodeLabyModManagement labyModManagement;

  public NodeLabyModListener(@NonNull NodeLabyModManagement labyModManagement) {
    this.labyModManagement = labyModManagement;
  }

  @EventListener
  public void handleConfigUpdate(ChannelMessageReceiveEvent event) {
    if (!event.channel().equals(LabyModManagement.LABYMOD_MODULE_CHANNEL)) {
      return;
    }

    if (LabyModManagement.LABYMOD_UPDATE_CONFIG.equals(event.message())) {
      // read the configuration from the databuf
      var configuration = event.content().readObject(LabyModConfiguration.class);
      // write the configuration silently to the file
      this.labyModManagement.configurationSilently(configuration);
    }
  }
}
