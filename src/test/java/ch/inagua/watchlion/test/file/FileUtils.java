package ch.inagua.watchlion.test.file;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class FileUtils {

	public static String getPathOf(String resourceName) throws URISyntaxException {
		return Paths.get(FileUtils.class.getResource(resourceName).toURI()).toString();
	}
	
}
