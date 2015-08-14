package ch.inagua.watchlion.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Watchlion {

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

	// ------------------------------------------------------------------------

	public List<Application> diffWithLocal(Watchlion localWatchlion) {
		List<Application> result = new ArrayList<Application>();
		for (Application refApp : applications) {
			Application localApp = localWatchlion.getApplicationForId(refApp.getId());
			if (localApp == null || !StringUtils.equals(refApp.getLastVersionId(), localApp.getLastVersionId())) {
				result.add(refApp);
			}
		}
		return result;
	}

	Application getApplicationForId(String id) {
		for (Application app : applications) {
			if (StringUtils.equals(id, app.getId())) {
				return app;
			}
		}
		return null;
	}

	public String getReport() {
		return getReport(applications);
	}

	public String getReport(List<Application> apps) {
		String result = "Missing versions:\n";
		for (Application app : apps) {
			result += " - " + app.getName() + " [" + app.getLastVersionId() + "]\n";
		}
		return result;
	}

}
