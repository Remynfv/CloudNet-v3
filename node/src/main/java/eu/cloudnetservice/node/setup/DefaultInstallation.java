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

import eu.cloudnetservice.node.console.Console;
import eu.cloudnetservice.node.console.animation.setup.ConsoleSetupAnimation;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.LockSupport;
import lombok.NonNull;

public class DefaultInstallation {

  private final Queue<DefaultSetup> setups = new LinkedList<>();
  private final ConsoleSetupAnimation animation = createAnimation();

  private static @NonNull ConsoleSetupAnimation createAnimation() {
    return new ConsoleSetupAnimation(
      """
        &f   ___  _                    _ &b     __    __  _____  &3  _____              _           _  _\s
        &f  / __\\| |  ___   _   _   __| |&b  /\\ \\ \\  /__\\/__   \\ &3  \\_   \\ _ __   ___ | |_   __ _ | || |
        &f / /   | | / _ \\ | | | | / _` |&b /  \\/ / /_\\    / /\\/ &3   / /\\/| '_ \\ / __|| __| / _` || || |
        &f/ /___ | || (_) || |_| || (_| |&b/ /\\  / //__   / /    &3/\\/ /_  | | | |\\__ \\| |_ | (_| || || |
        &f\\____/ |_| \\___/  \\__,_| \\__,_|&b\\_\\ \\/  \\__/   \\/     &3\\____/  |_| |_||___/ \\__| \\__,_||_||_|
        &f                               &b                      &3                                     \s""",
      null,
      "&r> &e");
  }

  public void executeFirstStartSetup(@NonNull Console console) {
    if (!Boolean.getBoolean("cloudnet.installation.skip") && !this.setups.isEmpty()) {
      var runningThread = Thread.currentThread();
      // apply all questions of all setups to the animation
      this.setups.forEach(setup -> setup.applyQuestions(this.animation));
      // start the animation
      this.animation.cancellable(false);
      console.startAnimation(this.animation);

      this.animation.addFinishHandler(() -> {
        // post the finish handling to the installations
        DefaultSetup setup;
        while ((setup = this.setups.poll()) != null) {
          setup.handleResults(this.animation);
        }
        // notify the monitor about the success
        LockSupport.unpark(runningThread);
      });

      // wait for the finish signal
      LockSupport.park();
    }
  }

  public void registerSetup(@NonNull DefaultSetup setup) {
    this.setups.add(setup);
  }
}
