/*
 * Copyright 2000-2010 JetBrains s.r.o.
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
import jetbrains.buildServer.agent.rakerunner.SupportedTestFramework;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static jetbrains.slow.plugins.rakerunner.MockingOptions.*;

/**
 * @author Roman Chernyatchik
 */
@Test(groups = {"all","slow"})
public class CucumberBuildLogTest extends AbstractRakeRunnerTest {
  @BeforeMethod
  @Override
  protected void setUp1() throws Throwable {
    super.setUp1();
    myShouldTranslateMessages = true;
  }

  @Override
  protected void appendRunnerSpecificRunParameters(Map<String, String> runParameters) throws IOException, RunBuildException {
    super.appendRunnerSpecificRunParameters(runParameters);

    setWorkingDir(runParameters, "app_cucumber");
    // enable cucumber
    SupportedTestFramework.CUCUMBER.activate(runParameters);
  }

  public void testGeneral()  throws Throwable {
    setPartialMessagesChecker();

    setMockingOptions(FAKE_TIME, FAKE_STACK_TRACE, FAKE_LOCATION_URL);
    initAndDoTest("stat:features", false, "app_cucumber");
  }

  public void testCounts()  throws Throwable {
    doTestWithoutLogCheck("stat:features", false, "app_cucumber");

    assertTestsCount(9, 3, 2);
  }
}