package test;

import static org.junit.Assert.assertEquals;
import genetic.individuals.Evaluator;
import genetic.individuals.RangeCheck;
import genetic.individuals.rules.Condition;
import genetic.individuals.rules.Rule;
import genetic.individuals.rules.RuleList;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import datasets.DataSet;
import datasets.stats.Features;

public class RulesTest {

	private DataSet data1, data2;
	private Features features1, features2;

	@Before
	public void before() {
		data1 = DataSet.CLASSIC_TRAIN;
		// These features are [2436.0, 4.0, 326.29945346772087, 2.0,
		// 53.34031198686371, 41.32188904087918, 6.79722001568811]
		Features.load(data1);
		data2 = DataSet.TWENTY_NG_TRAIN;
		// These features are [5710.0, 20.0, 29.57506061745801,
		// 4.1219280948873624, 119.09719789842381, 238.78529785508252,
		// 7.876419997524581]
		features2 = Features.load(data2);
	}

	@Test
	public void test() {
		RangeCheck check = new RangeCheck(0, 3000, true);
		Condition condition = new Condition(Arrays.asList(check));
		Rule rule = new Rule(condition, "SVM");
		RuleList rules = new RuleList(Arrays.asList(rule), "kNN");
		Evaluator eval = new Evaluator();
		long start = System.currentTimeMillis();
		System.out.println(eval.evaluate("kNN", data1));
		long elapsed = System.currentTimeMillis() - start;
		System.out.println("Elapsed time: " + elapsed);
		start = System.currentTimeMillis();
		// cache test
		System.out.println(eval.evaluate("kNN", data2));
		elapsed = System.currentTimeMillis() - start;
		System.out.println("Elapsed time: " + elapsed);
		assertEquals("kNN", rules.apply(features1));
		assertEquals("SVM", rules.apply(features2));
	}

}
