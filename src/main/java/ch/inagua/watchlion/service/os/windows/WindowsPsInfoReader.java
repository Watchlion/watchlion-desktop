package ch.inagua.watchlion.service.os.windows;

import java.util.ArrayList;
import java.util.List;

public class WindowsPsInfoReader {

	public List<String> parse(String content) {
		String[] lines = content.split("\n");
		List<String> apps = new ArrayList<String>();
		boolean started = false;
		boolean finished = false;
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.startsWith("Here is the standard error of the command (if any):")) {
				finished = true;
			}
			if (started && ! finished && !apps.contains(line)) {
				apps.add(line);
			}
			if (line.startsWith("Applications:")) {
				started = true;
			}
		}
		return apps;
	}

}
