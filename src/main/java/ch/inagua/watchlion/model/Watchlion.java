package ch.inagua.watchlion.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Watchlion {

	public Watchlion(Environment reference, Environment local) {
		this.reference = reference;
		this.local = local;
	}

	// ------------------------------------------------------------------------

	private Environment reference;

	public Environment getReference() {
		return reference;
	}

	public void setReference(Environment reference) {
		this.reference = reference;
	}

	// ------------------------------------------------------------------------

	private Environment local;

	public Environment getLocal() {
		return local;
	}

	public void setLocal(Environment local) {
		this.local = local;
	}

	// ------------------------------------------------------------------------

	public List<Application> delta() {
		List<Application> result = new ArrayList<Application>();
		for (Application refApp : reference.getApplications()) {
			Application localApp = local.getApplicationForId(refApp.getId());
			if (localApp == null 
				|| !StringUtils.equals(refApp.getLastVersion().getId(), localApp.getLastVersion().getId())
				// || !localApp.getLastVersion().isInstalled()
				) {
				result.add(refApp);
			}
		}
		return result;
	}

	public String getReport() {
		return getReport(delta());
	}

	private String getReport(List<Application> apps) {
		String result = "Missing versions:\n";
		for (Application app : apps) {
			result += " - " + app.getLabelWithLastVersion() + "\n";
		}
		return result;
	}

	public void setApplicationInstalled(Application application) {
		Application localApp = local.getApplicationForId(application.getId());
		if (localApp == null) {
			localApp = new Application();
			localApp.setId(application.getId());
			localApp.setName(application.getName());
			local.addApplication(localApp);
		}
		localApp.addVersion(application.getLastVersion());
		localApp.setUpdated();
	}

}
