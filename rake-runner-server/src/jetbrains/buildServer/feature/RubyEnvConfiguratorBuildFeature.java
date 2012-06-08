/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.feature;

import com.intellij.util.PathUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import jetbrains.buildServer.serverSide.BuildFeature;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Roman.Chernyatchik
 */
public class RubyEnvConfiguratorBuildFeature extends BuildFeature {
  public static final String NOT_SPECIFIED_GOOD = "<i>not specified</i>";
  public static final String NOT_SPECIFIED_ERR = "<strong>NOT SPECIFIED!</strong>";
  private final String myEditUrl;

  public RubyEnvConfiguratorBuildFeature(@NotNull final PluginDescriptor descriptor) {
    myEditUrl = descriptor.getPluginResourcesPath("rubyEnvConfiguratorParams.jsp");
  }


  @NotNull
  @Override
  public String getType() {
    return RubyEnvConfiguratorConstants.RUBY_ENV_CONFIGURATOR_FEATURE_TYPE;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Ruby environment configurator";
  }

  @Override
  public String getEditParametersUrl() {
    return myEditUrl;
  }

  @Override
  public boolean isMultipleFeaturesPerBuildTypeAllowed() {
    return false;
  }

  @NotNull
  @Override
  public String describeParameters(@NotNull final Map<String, String> params) {
    StringBuilder result = new StringBuilder();

    result.append("<ul style=\"list-style: none; padding-left: 0; margin: 0;\">");

    final RubyEnvConfiguratorConfiguration configuration = new RubyEnvConfiguratorConfiguration(params);
    switch (configuration.getType()) {
      case INTERPRETER_PATH: {
        displayParameter(result, "Interpreter path", configuration.getRubySdkPath(), NOT_SPECIFIED_GOOD);
        break;
      }
      case RVM: {
        displayParameter(result, "RVM sdk", configuration.getRVMSdkName(), NOT_SPECIFIED_ERR);
        displayParameter(result, "RVM gemset", configuration.getRVMGemsetName(), NOT_SPECIFIED_GOOD);

        if (configuration.isRVMGemsetCreate()) {
          result.append("<li>Create gemset if not exist</li>");
        }
        break;
      }
      case RVMRC: {
        displayParameter(result, "Path to a '.rvmrc' file", configuration.getRVMRCFilePath(), NOT_SPECIFIED_GOOD);
        break;
      }
    }

    if (configuration.isShouldFailBuildIfNoSdkFound()) {
      result.append("<li>Fail build if Ruby interpreter wasn't found<li>");
    }
    result.append("</ul>");
    return result.toString();
  }

  private static void displayParameter(@NotNull final StringBuilder sb,
                                       @NotNull final String name,
                                       @Nullable final String value,
                                       @NotNull final String emptyValue) {
    sb.append("<li>").append(name).append(": ");
    sb.append(StringUtil.isEmptyOrSpaces(value) ? emptyValue : value);
    sb.append("</li>");
  }

  @Override
  public Map<String, String> getDefaultParameters() {
    final Map<String, String> defaults = new HashMap<String, String>(2);

    defaults.put(RubyEnvConfiguratorConstants.UI_FAIL_BUILD_IF_NO_RUBY_FOUND_KEY, Boolean.TRUE.toString());
    defaults.put(RubyEnvConfiguratorConstants.UI_USE_RVM_KEY, "manual");
    return defaults;
  }

  @Override
  public PropertiesProcessor getParametersProcessor() {
    return new ParametersValidator();
  }

  static class ParametersValidator implements PropertiesProcessor {
    public Collection<InvalidProperty> process(final Map<String, String> properties) {
      final Collection<InvalidProperty> ret = new ArrayList<InvalidProperty>(1);
      final RubyEnvConfiguratorConfiguration configuration = new RubyEnvConfiguratorConfiguration(properties);
      switch (configuration.getType()) {
        case RVM: {
          if (StringUtil.isEmptyOrSpaces(configuration.getRVMSdkName())) {
            ret.add(new InvalidProperty(RubyEnvConfiguratorConstants.UI_RVM_SDK_NAME_KEY,
                                        "RVM interpreter name cannot be empty. If you want to use system ruby interpreter please enter 'system'."));
          }
          break;
        }
        case RVMRC: {
          String rvmrcFilePath = StringUtil.emptyIfNull(configuration.getRVMRCFilePath());
          if (!StringUtil.isEmptyOrSpaces(rvmrcFilePath) && !PathUtil.getFileName(rvmrcFilePath).equals(".rvmrc")) {
            ret.add(new InvalidProperty(RubyEnvConfiguratorConstants.UI_RVM_RVMRC_PATH_KEY,
                                        "RVMRV file name must be '.rvmrc'. Other names doesn't supported by 'rvm-shell'"));
          }
        }
        break;
      }
      return ret;
    }
  }
}
