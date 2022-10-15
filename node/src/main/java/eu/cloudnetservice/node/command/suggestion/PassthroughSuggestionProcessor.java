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

package eu.cloudnetservice.node.command.suggestion;

import cloud.commandframework.execution.CommandSuggestionProcessor;
import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext;
import eu.cloudnetservice.common.Nameable;
import eu.cloudnetservice.node.command.CommandProvider;
import eu.cloudnetservice.node.command.source.CommandSource;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;

public final class PassthroughSuggestionProcessor implements CommandSuggestionProcessor<CommandSource> {

  private final CommandProvider provider;

  public PassthroughSuggestionProcessor(@NonNull CommandProvider provider) {
    this.provider = provider;
  }

  @Override
  public @NonNull List<String> apply(
    @NonNull CommandPreprocessingContext<CommandSource> context,
    @NonNull List<String> allSuggestions
  ) {
    // check if the user tries to complete all command roots
    if (!context.getCommandContext().getRawInputJoined().contains(" ")) {
      return this.provider.commands().stream().map(Nameable::name).collect(Collectors.toList());
    }

    return allSuggestions;
  }
}
