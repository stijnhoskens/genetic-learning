package pre.data;

import io.IO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pre.models.FullDataSet;
import pre.models.TextInstance;

public class TopicsCollector {

	public static List<String> build(FullDataSet data) {
		Stream<TextInstance> instances = TextInstanceLoader.loadMultiple(
				data.train(), data.test());
		Set<String> set = instances.map(i -> i.getTopic()).collect(
				Collectors.toSet());
		List<String> list = new ArrayList<>(set);
		list.sort(Comparator.naturalOrder());
		instances.close();
		return list;
	}

	public static List<String> load(FullDataSet data) {
		return IO.allLines(data.topics());
	}

	public static void buildAndExport(FullDataSet data) {
		IO.write(data.topics(), w -> {
			build(data).forEach(s -> IO.writeLine(w, s));
		});
	}

	public static void main(String[] args) {
		FullDataSet.ALL.forEach(data -> {
			buildAndExport(data);
			System.out.println("building topics for " + data.toString()
					+ " done");
		});
	}
}
