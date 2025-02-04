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

package eu.cloudnetservice.node.setup;

import eu.cloudnetservice.driver.permission.Permission;
import eu.cloudnetservice.driver.permission.PermissionGroup;
import eu.cloudnetservice.node.Node;
import eu.cloudnetservice.node.console.animation.setup.ConsoleSetupAnimation;
import eu.cloudnetservice.node.console.animation.setup.answer.Parsers;
import eu.cloudnetservice.node.console.animation.setup.answer.QuestionAnswerType;
import eu.cloudnetservice.node.console.animation.setup.answer.QuestionListEntry;
import lombok.NonNull;

public class PermissionGroupSetup implements DefaultSetup {

  @Override
  public void applyQuestions(@NonNull ConsoleSetupAnimation animation) {
    animation.addEntries(
      QuestionListEntry.<Boolean>builder()
        .key("addDefaultGroups")
        .translatedQuestion("cloudnet-init-perms-add-default-groups")
        .answerType(QuestionAnswerType.<Boolean>builder()
          .recommendation("yes")
          .possibleResults("no", "yes")
          .parser(Parsers.bool()))
        .build());
  }

  @Override
  public void handleResults(@NonNull ConsoleSetupAnimation animation) {
    if (animation.result("addDefaultGroups")) {
      var adminGroup = PermissionGroup.builder()
        .name("admin")
        .potency(100)
        .addPermission(Permission.of("*"))
        .prefix("&4Admin &8| &7")
        .color("&4")
        .suffix("&f")
        .display("&4")
        .sortId(10)
        .build();
      var defaultGroup = PermissionGroup.builder()
        .name("default")
        .potency(100)
        .addPermission(Permission.of("bukkit.broadcast.user"))
        .defaultGroup(true)
        .prefix("&7")
        .color("&7")
        .suffix("&f")
        .display("&7")
        .sortId(99)
        .build();

      Node.instance().permissionManagement().addPermissionGroup(adminGroup);
      Node.instance().permissionManagement().addPermissionGroup(defaultGroup);
    }
  }
}
