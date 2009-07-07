/*
 * Copyright 2000-2009 JetBrains s.r.o.
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

package jetbrains.slow.plugins.rakerunner;

import java.io.IOException;
import java.util.Map;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRuntimeProperties;
import static jetbrains.slow.plugins.rakerunner.MockingOptions.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.jetbrains.annotations.Nullable;

/**
 * @author Roman Chernyatchik
 */
@Test(groups = {"all","slow"})
public class RakeBuildScriptBuildLogTest extends AbstractRakeRunnerTest {
  public RakeBuildScriptBuildLogTest(String s) {
    super(s);
  }

  @BeforeMethod
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myShouldTranslateMessages = true;
  }

  protected void appendRunnerSpecificRunParameters(Map<String, String> runParameters) throws IOException, RunBuildException {
    setWorkingDir(runParameters, "app1");
  }

  public void testBuildScript_stdout() throws Throwable {
    setPartialMessagesChecker();

    initAndDoTest("build_script:std_out", true, "app1");
  }

  public void testBuildScript_stderr() throws Throwable {
    setPartialMessagesChecker();

    initAndDoTest("build_script:std_err", true, "app1");
  }

  public void testBuildScript_show_one_task() throws Throwable {
    setPartialMessagesChecker();

    initAndDoTest("build_script:show_one_task", true, "app1");
  }

  public void testBuildScript_show_one_task_trace() throws Throwable {
    rakeUI_EnableTraceOption();
    setPartialMessagesChecker();

    initAndDoTest("build_script:show_one_task", "_trace", true, "app1");
  }

  public void testBuildScript_exception_in_embedded_task_trace_real() throws Throwable {
    setPartialMessagesChecker();
    rakeUI_EnableTraceOption();
    setMockingOptions(FAKE_TIME, FAKE_STACK_TRACE, FAKE_LOCATION_URL);

    initAndDoTest("build_script:exception_in_embedded_task", "_trace", false, "app1");
  }

  public void testBuildScript_warning_in_task_in_task() throws Throwable {
    setPartialMessagesChecker();

    initAndDoTest("build_script:warning_in_task", true, "app1");
  }

  public void testBuildScript_compile_error_task() throws Throwable {
    setPartialMessagesChecker();

    setMockingOptions(FAKE_TIME, FAKE_STACK_TRACE, FAKE_LOCATION_URL);
    initAndDoTest("compile_error:some_task", false, "app2");
  }

  public void testBuildScript_embedded_tasks_trace() throws Throwable {
    setPartialMessagesChecker();
    rakeUI_EnableTraceOption();

    initAndDoTest("build_script:embedded_tasks", "_trace", true, "app1");
  }

  public void testBuildScript_cmd_failed_real() throws Throwable {
    setPartialMessagesChecker();
    rakeUI_EnableTraceOption();
    setMockingOptions(FAKE_TIME, FAKE_STACK_TRACE, FAKE_LOCATION_URL);
    initAndDoRealTest("build_script:cmd_failed", false, "app1");
  }

  public void testBuildScript_depends_on_cmd_failed_real() throws Throwable {
    setPartialMessagesChecker();
    rakeUI_EnableTraceOption();
    setMockingOptions(FAKE_TIME, FAKE_STACK_TRACE, FAKE_LOCATION_URL);
    initAndDoRealTest("build_script:depends_on_cmd_failed", false, "app1");
  }
}