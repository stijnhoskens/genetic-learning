package learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.core.neighboursearch.NearestNeighbourSearch;

public class DocumentNNSearch extends NearestNeighbourSearch {

	private static final long serialVersionUID = 1L;

	private static final double DOCFREQ_UPPERLIMIT = 0.003;
	private static final int DOCFREQ_LOWERLIMIT = 1;

	private final MultiList<Instance> sparseInstances = new MultiList<>();
	private int nbOfInstances;
	private double[] kDistances;

	public DocumentNNSearch() {
		super();
	}

	public DocumentNNSearch(Instances instances) throws Exception {
		super();
		setInstances(instances);
	}

	@Override
	public void setInstances(Instances insts) throws Exception {
		insts.forEach(this::addToSparse);
		nbOfInstances = insts.size();
		cleanSparse();
		m_DistanceFunction.setInstances(insts);
		super.setInstances(insts);
	};

	private void cleanSparse() {
		sparseInstances.clean();
	}

	@Override
	public Instance nearestNeighbour(Instance target) throws Exception {
		return kNearestNeighbours(target, 1).instance(0);
	}

	@Override
	public Instances kNearestNeighbours(Instance target, int k)
			throws Exception {
		List<Instance> instances = sparseIndicesOf(target)
				.mapToObj(sparseInstances::get).flatMap(Set::stream).distinct()
				.collect(Collectors.toList());
		List<Double> distances = instances.stream()
				.mapToDouble(i -> m_DistanceFunction.distance(i, target))
				.boxed().collect(Collectors.toList());
		Instances result = new Instances(m_Instances, k);
		kDistances = new double[k];
		for (int i = 0; i < k; i++) {
			if (instances.isEmpty())
				result.add(m_Instances.stream().findAny().get());
			else {
				int index = minimumIndex(distances);
				result.add(instances.get(index));
				instances.remove(index);
				kDistances[i] = distances.get(index);
				distances.remove(index);
			}
		}
		return result;
	}

	private static int minimumIndex(List<Double> list) {
		double min = Double.POSITIVE_INFINITY;
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			double val = list.get(i);
			if (val < min) {
				index = i;
				min = val;
			}
		}
		return index;
	}

	@Override
	public double[] getDistances() throws Exception {
		return Arrays.copyOf(kDistances, kDistances.length);
	}

	@Override
	public void update(Instance ins) throws Exception {
		m_DistanceFunction.update(ins);
	}

	private void addToSparse(Instance ins) {
		sparseIndicesOf(ins).forEach(i -> sparseInstances.add(i, ins));
	}

	private IntStream sparseIndicesOf(Instance ins) {
		return IntStream.range(0, ins.numValues()).map(ins::index);
	}

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 10141 $");
	}

	private class MultiList<T> {
		private List<Set<T>> data = new ArrayList<>();
		private int size = 0;

		void add(int i, T t) {
			if (i >= size) {
				IntStream.rangeClosed(size, i).forEach(
						x -> data.add(new HashSet<>()));
				size = i + 1;
			}
			data.get(i).add(t);
		}

		Set<T> get(int i) {
			if (i >= size)
				return Collections.emptySet();
			return Collections.unmodifiableSet(data.get(i));
		}

		void clean() {
			int upperlimit = (int) (DOCFREQ_UPPERLIMIT * nbOfInstances);
			if (upperlimit <= DOCFREQ_LOWERLIMIT)
				return;
			for (int i = 0; i < data.size(); i++) {
				Set<T> instances = get(i);
				if (instances.size() > upperlimit
						|| instances.size() <= DOCFREQ_LOWERLIMIT)
					data.get(i).clear();
			}
		}
	}

}
