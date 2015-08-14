package ch.inagua.watchlion.model;

import java.util.ArrayList;
import java.util.List;

public class Application {

	public static class Version {
		// ------------------------------------------------------------------------

		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		// ------------------------------------------------------------------------

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		// ------------------------------------------------------------------------

		private String update;

		public String getUpdate() {
			return update;
		}

		public void setUpdate(String update) {
			this.update = update;
		}

	}

	// ------------------------------------------------------------------------

	private List<Application> plugins;

	// ------------------------------------------------------------------------

	private String id;

	public String getId() {
		return id;
	}

	public Application setId(String id) {
		this.id = id;
		return this;
	}

	// ------------------------------------------------------------------------

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ------------------------------------------------------------------------

	private String instructions;

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	// ------------------------------------------------------------------------

	private final List<Version> versions = new ArrayList<Version>();

	public List<Version> getVersions() {
		return versions;
	}

	public void addVersion(Version version) {
		versions.add(version);
	}

	public void removeVersion(Version version) {
		versions.remove(version);
	}

	public String getLastVersionId() {
		if (versions.size() == 0) {
			return null;
		}
		return versions.get(versions.size() - 1).getId();
	}

}
