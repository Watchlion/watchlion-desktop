package ch.inagua.watchlion.model;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.core.StringEndsWith;

public class LightApplication {

	private String name;
	
	private String version;

	public LightApplication() {
	}

	public LightApplication(String name) {
		setName(name);
	}

	public LightApplication(String name, String version) {
		setName(name);
		setVersion(version);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return name + "\n" + version;
	}

	public static LightApplication parse(String programName) {
		if (programName == null) {
			return null;
		}
		String name = programName;
		String version = "";
		String[] tokens = programName.split(" ");
		if (tokens.length > 1) {
			String last = tokens[tokens.length-1];
			if (containsNumbers(last)) {
				tokens = (String[]) ArrayUtils.removeElement(tokens, last);
				String beforeLast = tokens[tokens.length-1];
				if (StringUtils.equals(last, beforeLast)) {
					tokens = (String[]) ArrayUtils.removeElement(tokens, beforeLast);
				}
				name = StringUtils.join(tokens, " ");
				version = last;
			}
		}
		return new LightApplication(name, version);
	}

	static boolean containsNumbers(String string) {
		for (int i = 0; i < 10; i++) {
			if (string.contains("" + i)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LightApplication other = (LightApplication) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	public static String nameToId(String name) {
		if (name == null) {
			return null;
		}
		return name.trim().replace(" ", "-");
	}

	
}
