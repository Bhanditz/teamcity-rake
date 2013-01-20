/*
 * Copyright 2000-2013 JetBrains s.r.o.
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

import org.testng.annotations.Test;

/**
 * @author Roman Chernyatchik
 * @author Vladislav.Rassokhin
 */
@Test(groups = {"all", "slow"})
public class TestUnitMessagesTest extends AbstractTestUnitTest {

  @Override
  protected void setUp2() throws Throwable {
    super.setUp2();
    setMessagesTranslationEnabled(false);
    setMockingOptions(MockingOptions.FAKE_STACK_TRACE, MockingOptions.FAKE_LOCATION_URL);
  }

  public void testTestsOutput() throws Throwable {
    setPartialMessagesChecker();

    initAndDoTest("tests:test_output", false);
  }

  public void testTestGeneral() throws Throwable {
    setPartialMessagesChecker();

    initAndDoTest("stat:general", true);
  }

  public void testTestPassed() throws Throwable {
    setPartialMessagesChecker();
    initAndDoTest("stat:passed", true);
  }

  public void testTestFailed() throws Throwable {
    setPartialMessagesChecker();

    initAndDoTest("stat:failed", false);
  }

  public void testTestError() throws Throwable {
    setPartialMessagesChecker();

    initAndDoTest("stat:error", false);
  }

  public void testTestCompileError() throws Throwable {
    setPartialMessagesChecker();

    initAndDoTest("stat:compile_error", false);
  }

  public void testLocationUrl() throws Throwable {
    setPartialMessagesChecker();
    setMockingOptions();
    initAndDoTest("stat:passed", "_location", true);
  }
}
