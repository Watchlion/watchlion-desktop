package ch.inagua.watchlion.model;

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
	
}
