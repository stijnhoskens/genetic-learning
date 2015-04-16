package analysis;

import genetic.Population;
import genetic.individuals.rules.Condition;
import genetic.individuals.rules.Rule;
import genetic.individuals.rules.RuledIndividual;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import util.Pair;

public class DiversityMeasure {

	public static double between(RuledIndividual i0, RuledIndividual i1) {
		Set<String> clsfrs0 = classifiersOf(i0), clsfrs1 = classifiersOf(i1);
		Set<Integer> ftrs0 = featureChecksOf(i0), ftrs1 = featureChecksOf(i1);

		int cmp0 = complement(clsfrs0, clsfrs1).size(), cmp1 = complement(
				ftrs0, ftrs1).size(), int0 = intersection(clsfrs0, clsfrs1)
				.size(), int1 = intersection(ftrs0, ftrs1).size();

		int tot0 = cmp0 + int0, tot1 = cmp1 + int1;

		return ((double) cmp0 + cmp1) / ((double) tot0 + tot1);
	}

	public static double of(Population<RuledIndividual> pop) {
		return pairsOf(pop.asList())
				.mapToDouble(p -> between(p.getFirst(), p.getSecond()))
				.average().getAsDouble();
	}

	private static Stream<Rule> rulesOf(RuledIndividual i) {
		return i.getRules().asList().stream();
	}

	private static Set<String> classifiersOf(RuledIndividual i) {
		return rulesOf(i).map(Rule::get).collect(Collectors.toSet());
	}

	private static Set<Integer> featureChecksOf(RuledIndividual i) {
		return rulesOf(i).map(Rule::getCondition)
				.map(Condition::getCheckedIndices).flatMap(Collection::stream)
				.collect(Collectors.toSet());
	}

	@SafeVarargs
	private static <T> Set<T> intersection(Set<T> arg0, Set<T>... arg1) {
		return Arrays.stream(arg1).reduce(new HashSet<>(arg0), (s0, s1) -> {
			Set<T> s = new HashSet<>(s0);
			s.retainAll(s1);
			return s;
		});
	}

	@SafeVarargs
	private static <T> Set<T> complement(Set<T> arg0, Set<T>... arg1) {
		Set<T> set = Arrays.stream(arg1).reduce(new HashSet<>(arg0),
				(s0, s1) -> {
					Set<T> s = new HashSet<>(s0);
					s.addAll(s1);
					return s;
				});
		set.removeAll(intersection(arg0, arg1));
		return set;
	}

	private static <T> Stream<Pair<T, T>> pairsOf(List<T> list) {
		Builder<Pair<T, T>> builder = Stream.builder();
		for (int i = 0; i < list.size(); i++)
			for (int j = i + 1; j < list.size(); j++)
				builder.accept(new Pair<>(list.get(i), list.get(j)));
		return builder.build();
	}

	public static void main(String[] args) {

	}
}
