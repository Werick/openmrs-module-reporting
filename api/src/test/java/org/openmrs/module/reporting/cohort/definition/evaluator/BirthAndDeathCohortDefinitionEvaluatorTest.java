package org.openmrs.module.reporting.cohort.definition.evaluator;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.cohort.definition.BirthAndDeathCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.common.TestUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class BirthAndDeathCohortDefinitionEvaluatorTest extends BaseModuleContextSensitiveTest {
	
	protected static final String XML_DATASET_PATH = "org/openmrs/module/reporting/include/";
	
	protected static final String XML_REPORT_TEST_DATASET = "ReportTestDataset";
	
	@Before
	public void setup() throws Exception {
		executeDataSet(XML_DATASET_PATH + new TestUtil().getTestDatasetFilename(XML_REPORT_TEST_DATASET));
	}
	
	/**
	 * @see {@link BirthAndDeathCohortDefinitionEvaluator#evaluate(CohortDefinition,EvaluationContext)}
	 */
	@Test
	@Verifies(value = "should find patients by birth range", method = "evaluate(CohortDefinition,EvaluationContext)")
	public void evaluate_shouldFindPatientsByBirthRange() throws Exception {
		BirthAndDeathCohortDefinition cd = new BirthAndDeathCohortDefinition();
		cd.setBornOnOrAfter(DateUtil.getDateTime(1950, 1, 1));
		cd.setBornOnOrBefore(DateUtil.getDateTime(1999, 12, 31));
		Cohort cohort = Context.getService(CohortDefinitionService.class).evaluate(cd, null);
		Assert.assertEquals(4, cohort.size());
		Assert.assertTrue(cohort.contains(2));
		Assert.assertTrue(cohort.contains(7));
		Assert.assertTrue(cohort.contains(21));
		Assert.assertTrue(cohort.contains(22));
	}
	
	/**
	 * @see {@link BirthAndDeathCohortDefinitionEvaluator#evaluate(CohortDefinition,EvaluationContext)}
	 */
	@Test
	@Verifies(value = "should find patients by death range", method = "evaluate(CohortDefinition,EvaluationContext)")
	public void evaluate_shouldFindPatientsByDeathRange() throws Exception {
		BirthAndDeathCohortDefinition cd = new BirthAndDeathCohortDefinition();
		cd.setDiedOnOrAfter(DateUtil.getDateTime(2005, 1, 1));
		cd.setDiedOnOrBefore(DateUtil.getDateTime(2005, 12, 31));
		Cohort cohort = Context.getService(CohortDefinitionService.class).evaluate(cd, null);
		Assert.assertEquals(1, cohort.size());
		Assert.assertTrue(cohort.contains(20));
	}
	
	/**
	 * @see {@link BirthAndDeathCohortDefinitionEvaluator#evaluate(CohortDefinition,EvaluationContext)}
	 */
	@Test
	@Verifies(value = "should find patients by birth range and death range", method = "evaluate(CohortDefinition,EvaluationContext)")
	public void evaluate_shouldFindPatientsByBirthRangeAndDeathRange() throws Exception {
		BirthAndDeathCohortDefinition cd = new BirthAndDeathCohortDefinition();
		cd.setBornOnOrAfter(DateUtil.getDateTime(1900, 1, 1));
		cd.setBornOnOrBefore(DateUtil.getDateTime(1950, 12, 31));
		cd.setDiedOnOrAfter(DateUtil.getDateTime(2005, 1, 1));
		cd.setDiedOnOrBefore(DateUtil.getDateTime(2005, 12, 31));
		Cohort cohort = Context.getService(CohortDefinitionService.class).evaluate(cd, null);
		Assert.assertEquals(1, cohort.size());
		Assert.assertTrue(cohort.contains(20));
	}
}