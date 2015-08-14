package ch.inagua.watchlion.service.os.windows;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WindowsPsInfoReaderTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void test_parse() throws Exception {
		String path = getClass().getResource("/PsInfo.output.txt").getPath();
		String content = loadFileContent(path);
		
		List<String> installedApps = new WindowsPsInfoReader().parse(content);
		
		assertThat(installedApps, hasSize(151));
	}

	private String loadFileContent(String jsonFilePath) throws IOException {
		String path = jsonFilePath;
		if ("/".equals(jsonFilePath.substring(0, 1))) {
			path = jsonFilePath.substring(1);
		}

		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			throw new IllegalArgumentException("File Not Found: " + path);
		}
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}
}
