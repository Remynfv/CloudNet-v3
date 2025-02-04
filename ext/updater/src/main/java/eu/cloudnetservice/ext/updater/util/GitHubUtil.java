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

package eu.cloudnetservice.ext.updater.util;

import java.net.URI;
import lombok.NonNull;

public final class GitHubUtil {

  private static final String GITHUB_URL_FORMAT = "https://github.com/%s/raw/%s/%s";

  private GitHubUtil() {
    throw new UnsupportedOperationException();
  }

  public static @NonNull URI buildUri(@NonNull String repo, @NonNull String branch, @NonNull String filePath) {
    return URI.create(String.format(GITHUB_URL_FORMAT, repo, branch, filePath));
  }
}
