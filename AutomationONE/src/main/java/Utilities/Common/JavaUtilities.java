package Utilities.Common;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import lombok.SneakyThrows;

/**
 * Utility class for common Java functions.
 * 
 * @author Saif
 */
public class JavaUtilities {

	/**
	 * Returns a random integer between 1 and 1000 (inclusive).
	 * 
	 * @return int Random number.
	 */
	public static int getRandomNumber() {
		return new Random().nextInt(1000) + 1; // Ensures range is 1-1000
	}

	/**
	 * Fetches a value from the properties file based on the given key.
	 * 
	 * @param configKey The key to look up.
	 * @return Value associated with the key.
	 * @throws IOException If the file cannot be read.
	 */
	@SneakyThrows
	public static String getConfig(String configKey) {
		Properties properties = new Properties();
		try (InputStream input = Files.newInputStream(Paths.get(PathConst.PROPFILEPATH))) {
			properties.load(input);
		}
		return properties.getProperty(configKey, "Key Not Found");
	}

	/**
	 * Reads a template file and replaces placeholders with given values.
	 * 
	 * @param dataFilePath Path of the file to read.
	 * @param replacements HashMap containing key-value pairs for replacements.
	 * @return Modified content as String.
	 * @throws IOException If an error occurs during file reading.
	 */
	public static String replaceTemplatewithValues(String dataFilePath, HashMap<String, String> replacements) {
		StringBuilder content = new StringBuilder();

		// 1️⃣ Read file line by line to avoid loading entire content into memory
		try (BufferedReader br = new BufferedReader(new FileReader(dataFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch (IOException e) {
			throw new RuntimeException("Error reading file: " + dataFilePath, e);
		}

		// 2️⃣ Use StringBuilder to replace placeholders efficiently
		for (var entry : replacements.entrySet()) {
			String placeholder = "{{" + entry.getKey() + "}}";
			int index;

			// Replace all occurrences of {{KEY}} in content
			while ((index = content.indexOf(placeholder)) != -1) {
				content.replace(index, index + placeholder.length(), entry.getValue());
			}
		}

		return content.toString();
	}

	/**
	 * Creates a CSV file at the specified path.
	 * 
	 * @param fileName Name of the CSV file.
	 * @param filePath Directory where the file will be saved.
	 * @param data     Varargs data to be written.
	 * @throws IOException If the file cannot be created.
	 */
	@SneakyThrows
	public static void createCSV(String fileName, String filePath, String... data) {
		File csvFile = Paths.get(filePath, fileName + ".csv").toFile();
		try (FileWriter writer = new FileWriter(csvFile)) {
			writer.write(String.join(",", data));
			System.out.println("CSV file created successfully: " + csvFile.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("CSV file creation failed!");
			e.printStackTrace();
		}
	}

	/**
	 * Runs a system command and waits for a specific log text. Works on both
	 * Windows & Linux/Mac.
	 * 
	 * @param command The command to execute.
	 * @param logText The text to look for in the output.
	 * @throws IOException          If an I/O error occurs.
	 * @throws InterruptedException If the process is interrupted.
	 */
	@SneakyThrows
	public static void runCommand(String command, String logText) {
		String os = System.getProperty("os.name").toLowerCase();
		ProcessBuilder builder;

		if (os.contains("win")) {
			builder = new ProcessBuilder("cmd.exe", "/c", command);
		} else {
			builder = new ProcessBuilder("sh", "-c", command);
		}

		builder.redirectErrorStream(true);
		Process process = builder.start();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			boolean found = false;

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (line.contains(logText)) {
					found = true;
					break;
				}
			}

			process.waitFor();

			if (!found) {
				System.err.println("Log text not found in output!");
			}
		}
	}
}