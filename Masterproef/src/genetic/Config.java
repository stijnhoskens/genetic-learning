package genetic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {

	public static final Properties INIT = getProperties("init");
	public static final Properties MUTATION = getProperties("mutation");
	public static final Properties CROSSOVER = getProperties("crossover");
	public static final Properties CLASSIFIERS = getProperties("classifiers");

	private static Properties getProperties(String fileName) {
		Properties prop = new Properties();
		try {
			prop.load(Files.newInputStream(getPath(fileName)));
		} catch (IOException e) {
			// do nothing, an empty properties object will be returned.
		}
		return prop;
	}

	private static Path getPath(String fileName) {
		return Paths.get("config/" + fileName + ".properties");
	}

	public static void main(String[] args) {
		System.out.println(CLASSIFIERS.getProperty("SVM"));
	}
}
