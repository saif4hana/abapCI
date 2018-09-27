package abapci.result;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import abapci.domain.AbapPackageTestState;
import abapci.domain.SourcecodeState;
import abapci.domain.TestResult;
import abapci.domain.TestState;
import abapci.feature.FeatureFacade;
import abapci.feature.UnitFeature;

@RunWith(PowerMockRunner.class)
public class SourceCodeStateEvaluatorTest {

	SourceCodeStateEvaluator sourceCodeStateEvaluator;
	List<AbapPackageTestState> abapPackageTestStates;

	@Mock
	TestResult unitTestResult1;
	@Mock
	TestResult unitTestResult2;

	@Mock
	TestResult atcTestResult1;
	@Mock
	TestResult atcTestResult2;

	@Mock
	FeatureFacade featureFacade;
	@Mock
	UnitFeature unitFeature;

	@Before
	public void before() {
		sourceCodeStateEvaluator = new SourceCodeStateEvaluator();
		sourceCodeStateEvaluator.featureFacade = featureFacade;
		abapPackageTestStates = new ArrayList<AbapPackageTestState>();
		PowerMockito.when(featureFacade.getUnitFeature()).thenReturn(unitFeature);
	}

	@Test
	public void evaluateEmptyPackagesTest() {
		PowerMockito.when(unitFeature.isActive()).thenReturn(true);
		SourcecodeState sourcecodeState = sourceCodeStateEvaluator.evaluate(abapPackageTestStates);
		assertEquals(SourcecodeState.UNDEF, sourcecodeState);
	}

	@Test
	public void evalutateOneAbapPackageTest() {
		abapPackageTestStates.add(
				new AbapPackageTestState("TESTPROJECT", "TESTPACKAGE", "jenkinsInfo", unitTestResult1, atcTestResult1));

		PowerMockito.when(featureFacade.getUnitFeature().isActive()).thenReturn(true);
		assertOnePackage(new TestStateTuple(TestState.OK, TestState.OK), SourcecodeState.OK);
		assertOnePackage(new TestStateTuple(TestState.OK, TestState.OK), SourcecodeState.OK);
		assertOnePackage(new TestStateTuple(TestState.NOK, TestState.OK), SourcecodeState.UT_FAIL);
		assertOnePackage(new TestStateTuple(TestState.OK, TestState.NOK), SourcecodeState.ATC_FAIL);
		assertOnePackage(new TestStateTuple(TestState.DEACT, TestState.OK), SourcecodeState.UNDEF);
		assertOnePackage(new TestStateTuple(TestState.OFFL, TestState.OFFL), SourcecodeState.OFFL);
		assertOnePackage(new TestStateTuple(TestState.DEACT, TestState.DEACT), SourcecodeState.UNDEF);
		assertOnePackage(new TestStateTuple(TestState.OK, TestState.OFFL), SourcecodeState.OK);
	}

	@Test
	public void evalutateTwoAbapPackagesTest() {

		abapPackageTestStates.add(new AbapPackageTestState("TESTPROJECT1", "TESTPACKAGE1", "jenkinsInfo1",
				unitTestResult1, atcTestResult1));
		abapPackageTestStates.add(new AbapPackageTestState("TESTPROJECT2", "TESTPACKAGE2", "jenkinsInfo2",
				unitTestResult2, atcTestResult2));

		PowerMockito.when(unitFeature.isActive()).thenReturn(true);

		PowerMockito.when(unitFeature.isRunActivatedObjectsOnly()).thenReturn(false);

		assertTwoPackages(new TestStateTuple(TestState.OK, TestState.OK),
				new TestStateTuple(TestState.OK, TestState.OK), SourcecodeState.OK);

		assertTwoPackages(new TestStateTuple(TestState.OK, TestState.OK),
				new TestStateTuple(TestState.NOK, TestState.OK), SourcecodeState.UT_FAIL);

		assertTwoPackages(new TestStateTuple(TestState.OK, TestState.OK),
				new TestStateTuple(TestState.OK, TestState.NOK), SourcecodeState.ATC_FAIL);

		assertTwoPackages(new TestStateTuple(TestState.OK, TestState.OK),
				new TestStateTuple(TestState.OK, TestState.NOK), SourcecodeState.ATC_FAIL);

		assertTwoPackages(new TestStateTuple(TestState.OK, TestState.OK),
				new TestStateTuple(TestState.OFFL, TestState.OK), SourcecodeState.OFFL);

		PowerMockito.when(unitFeature.isRunActivatedObjectsOnly()).thenReturn(true);
		assertTwoPackages(new TestStateTuple(TestState.OK, TestState.OK),
				new TestStateTuple(TestState.OFFL, TestState.OK), SourcecodeState.OK);

		PowerMockito.when(unitFeature.isActive()).thenReturn(false);
		assertTwoPackages(new TestStateTuple(TestState.OFFL, TestState.OK),
				new TestStateTuple(TestState.OFFL, TestState.OK), SourcecodeState.OK);

	}

	private void assertOnePackage(TestStateTuple testStateTuple, SourcecodeState sourcecodeTestState) {
		PowerMockito.when(unitTestResult1.getTestState()).thenReturn(testStateTuple.getUnitTestState());
		PowerMockito.when(atcTestResult1.getTestState()).thenReturn(testStateTuple.getAtcTestState());

		assertEquals(sourcecodeTestState, sourceCodeStateEvaluator.evaluate(abapPackageTestStates));

	}

	private void assertTwoPackages(TestStateTuple testStateTuple1, TestStateTuple testStateTuple2,
			SourcecodeState sourcecodeTestState) {
		PowerMockito.when(unitTestResult1.getTestState()).thenReturn(testStateTuple1.getUnitTestState());
		PowerMockito.when(atcTestResult1.getTestState()).thenReturn(testStateTuple1.getAtcTestState());

		PowerMockito.when(unitTestResult2.getTestState()).thenReturn(testStateTuple2.getUnitTestState());
		PowerMockito.when(atcTestResult2.getTestState()).thenReturn(testStateTuple2.getAtcTestState());

		assertEquals(sourcecodeTestState, sourceCodeStateEvaluator.evaluate(abapPackageTestStates));
	}

	private class TestStateTuple {
		private TestState unitTestState;
		private TestState atcTestState;

		public TestStateTuple(TestState unitTestState, TestState atcTestState) {
			this.unitTestState = unitTestState;
			this.atcTestState = atcTestState;
		}

		public TestState getUnitTestState() {
			return unitTestState;
		}

		public TestState getAtcTestState() {
			return atcTestState;
		}

	}

}
