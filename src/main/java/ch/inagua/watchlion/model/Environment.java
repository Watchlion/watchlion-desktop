package ch.inagua.watchlion.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Environment {
	
	private static final String PROTOCOL = "0.0.1";

	private static final String VERSION = "001";

	public Environment() {
		setProtocol(PROTOCOL);
		setVersion(VERSION);
		setUpdated();
	}
	
	// ------------------------------------------------------------------------

	private String path;

	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	// ------------------------------------------------------------------------

	private String protocol;

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProtocol() {
		return protocol;
	}

	// ------------------------------------------------------------------------

	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	// ------------------------------------------------------------------------

	private Date updateDate;

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdated() {
		setUpdateDate(new Date());
	}

	public void setUpdateDate(Date date) {
		this.updateDate = date;
	}

	// ------------------------------------------------------------------------

	private final List<Application> applications = new ArrayList<Application>();

	public List<Application> getApplications() {
		return applications;
	}

	public Environment addApplication(Application application) {
		applications.add(application);
		return this;
	}

	public void removeApplication(Application application) {
		applications.remove(application);
	}

	public Application getApplicationForId(String id) {
		for (Application app : applications) {
			if (StringUtils.equals(id, app.getId())) {
				return app;
			}
		}
		return null;
	}
	
	// ------------------------------------------------------------------------

}
