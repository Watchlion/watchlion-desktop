package ch.inagua.watchlion.service.os.osx;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.plist.XMLPropertyListConfiguration;

public class OsxInfoPlistReader {

	private XMLPropertyListConfiguration plist;

	public OsxInfoPlistReader(String path) throws ConfigurationException {
        plist = new XMLPropertyListConfiguration();
        plist.setFileName(path);
        plist.load();
	}

	public String getBundleVersion() {
		return (String) plist.getProperty("CFBundleVersion");
	}

	public String getBundleShortVersionString() {
		return (String) plist.getProperty("CFBundleShortVersionString");
	}

}
