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

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.arguments.CommandSuggestionEngine;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.execution.CommandSuggestionProcessor;
import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext;
import cloud.commandframework.internal.CommandInputTokenizer;
import cloud.commandframework.services.State;
import java.util.LinkedList;
import java.util.List;
import lombok.NonNull;

public final class HandlerSpecificCommandSuggestionEngine<C> implements CommandSuggestionEngine<C> {

  private final CommandManager<C> manager;
  private final CommandTree<C> commandTree;
  private final CommandSuggestionProcessor<C> suggestionProcessor;

  public HandlerSpecificCommandSuggestionEngine(
    @NonNull CommandManager<C> manager,
    @NonNull CommandSuggestionProcessor<C> suggestionProcessor
  ) {
    this.manager = manager;
    this.commandTree = manager.commandTree();
    this.suggestionProcessor = suggestionProcessor;
  }

  @Override
  public @NonNull List<String> getSuggestions(@NonNull CommandContext<C> context, @NonNull String input) {
    // Store a copy of the input queue in the context
    var inputQueue = new CommandInputTokenizer(input).tokenize();
    context.store("__raw_input__", new LinkedList<>(inputQueue));

    // check if we're allowed to process
    if (this.manager.preprocessContext(context, inputQueue) == State.ACCEPTED) {
      var allSuggestions = this.commandTree.getSuggestions(context, inputQueue);
      return this.suggestionProcessor.apply(new CommandPreprocessingContext<>(context, inputQueue), allSuggestions);
    } else {
      return List.of();
    }
  }
}
