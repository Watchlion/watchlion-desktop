package ch.inagua.watchlion.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Application {

	public static class Version {

		public Version() {
			setUpdated();
		}

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
		private Date updateDate;

		public Date getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(Date date) {
			updateDate = date;
		}

		public void setUpdated() {
			setUpdateDate(new Date());
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
		private boolean ignored;

		public boolean isIgnored() {
			return ignored;
		}

		public void setIgnored(boolean ignored) {
			this.ignored = ignored;
		}

		// ------------------------------------------------------------------------

		public Version copy() {
			Version copy = new Version();
			
			copy.setId(getId());
			copy.setName(getName());
			copy.setUpdateDate(getUpdateDate());
			copy.setInstructions(getInstructions());
			copy.setInstall(getInstall());
			// copy.setIgnored(isIgnored());

			return copy;
		}

	}

	public Application() {
		setUpdated();
	}

	// ------------------------------------------------------------------------
	private Date updateDate;

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date date) {
		updateDate = date;
	}

	public void setUpdated() {
		setUpdateDate(new Date());
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
	private String serial;

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
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
	private boolean ignored;

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
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

	public Version getVersionForId(String versionId) {
		for (Version version : versions) {
			if (StringUtils.equals(versionId, version.getId())) {
				return version;
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------

	private final List<Version> plugins = new ArrayList<Version>();

	public List<Version> getPlugins() {
		return plugins;
	}

	public void addPlugin(Version plugin) {
		plugins.add(plugin);
	}

	public void removePlugin(Version plugin) {
		plugins.remove(plugin);
	}

	public Version getPluginForId(String pluginId) {
		for (Version plugin : plugins) {
			if (StringUtils.equals(pluginId, plugin.getId())) {
				return plugin;
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------

	public String getLabelWithLastVersion() {
		String lastVersion = getLastVersion() == null ? "-" : getLastVersion().getName();
		String prefix = getVersions().size() > 1 ? ">> " : "";
		return getName() + " [" + prefix + lastVersion + "]";
	}

	// TODO PROPERTY 8
	public Application copy() {
		Application copy = new Application();

		copy.setId(getId());
		copy.setName(getName());
		copy.setBrief(getBrief());
		copy.setURL(getURL());
		copy.setType(getType());
		copy.setCategory(getCategory());
		copy.setUsername(getUsername());
		copy.setSerial(getSerial());
		copy.setRequired(getRequired());
		copy.setInstructions(getInstructions());
		copy.setInstall(getInstall());
		copy.setIgnored(isIgnored());

		for (Version plugin : plugins) {
			copy.addPlugin(plugin.copy());
		}
		
		return copy;
	}

	public Application copyLocal() {
		Application copy = new Application();
		copy.setId(getId());
		copy.setName(getName());
		copy.setIgnored(isIgnored());
		return copy;
	}

}
