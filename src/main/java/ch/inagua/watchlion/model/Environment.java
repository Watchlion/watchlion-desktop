package ch.inagua.watchlion.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Environment {

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

	private String update;

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	// ------------------------------------------------------------------------

	private final List<Application> applications = new ArrayList<Application>();

	public List<Application> getApplications() {
		return applications;
	}

	public void addApplication(Application application) {
		applications.add(application);
	}

	public void removeApplication(Application application) {
		applications.remove(application);
	}

	Application getApplicationForId(String id) {
		for (Application app : applications) {
			if (StringUtils.equals(id, app.getId())) {
				return app;
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------

}
