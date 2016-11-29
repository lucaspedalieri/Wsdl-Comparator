package businesslogic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileManager {

	public FileManager() {

	}

	public void writeFile(String path, List<String> content) throws IOException {
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(path));
		for (int i = 0; i < content.size(); i++) {
			outputWriter.write(content.get(i));
			outputWriter.newLine();
		}
		outputWriter.flush();
		outputWriter.close();
	}

}
