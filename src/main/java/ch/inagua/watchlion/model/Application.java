package ch.inagua.watchlion.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

		// ------------------------------------------------------------------------
		private String instructions;

		public String getInstructions() {
			return instructions;
		}

		public void setInstructions(String instructions) {
			this.instructions = instructions;
		}

		// ------------------------------------------------------------------------
		private String install;

		public String getInstall() {
			return install;
		}

		public void setInstall(String install) {
			this.install = install;
		}

	}

	// ------------------------------------------------------------------------

	private List<Application> plugins;

	// ------------------------------------------------------------------------
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

	private Date lastModication;

	public String getUpdate() {
		return lastModication == null ? null : DATE_FORMAT.format(lastModication);
	}

	public void setUpdate(String update) throws ParseException {
		lastModication = update == null ? null : DATE_FORMAT.parse(update);
	}

	public void setUpdated() {
		lastModication = new Date();
	}
	
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
	// TODO PROPERTY 4

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// ------------------------------------------------------------------------
	private String brief;

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	// ------------------------------------------------------------------------
	private String url;

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	// ------------------------------------------------------------------------
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// ------------------------------------------------------------------------
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	// ------------------------------------------------------------------------
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// ------------------------------------------------------------------------
	private String required;

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
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
	private String install;

	public String getInstall() {
		return install;
	}

	public void setInstall(String install) {
		this.install = install;
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

	public Version getLastVersion() {
		if (versions.size() == 0) {
			return null;
		}
		return versions.get(versions.size() - 1);
	}

	public String getLastVersionId() {
		Version lastVersion = getLastVersion();
		if (lastVersion == null) {
			return null;
		}
		return lastVersion.getId();
	}

	public String getLabelWithLastVersion() {
		return getName() + " [" + getLastVersion().getName() + "]";
	}

	//	private void setDirty() {
	//		lastModication = new Date();
	//	}

}
