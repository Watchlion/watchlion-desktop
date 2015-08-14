package ch.inagua.watchlion.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ch.inagua.watchlion.model.Application.Version;

public class Watchlion {

	public Watchlion(Environment reference, Environment local) {
		this.reference = reference;
		this.local = local;
		
		for (Application refApp : reference.getApplications()) {
			Application localApp = local.getApplicationForId(refApp.getId());
			if (localApp == null) {
				local.addApplication(refApp.copyLocal());
			}
		}
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
			if (!refApp.isIgnored()) {
				Application localApp = local.getApplicationForId(refApp.getId());
				if (localApp == null 
					|| refApp.getLastVersion() == null
					|| localApp.getLastVersion() == null
					|| !StringUtils.equals(refApp.getLastVersion().getId(), localApp.getLastVersion().getId())
					// || !localApp.getLastVersion().isInstalled()
					) {
					Application localCopy = refApp.copy();
					localCopy.addVersion(refApp.getLastVersion().copy());
					result.add(localCopy);
				}
			}
		}
		return result;
	}

	public String getReport() {
		return getReport(delta());
	}

	private String getReport(List<Application> apps) {
		String result = "Missing versions (" + apps.size() + "):\n";
		for (Application app : apps) {
			result += " - " + app.getLabelWithLastVersion() + "\n";
		}
		return result;
	}

	// ------------------------------------------------------------------------

	public void setLastVersionInstalled(Application application, boolean installed) {
		Application refApp = reference.getApplicationForId(application.getId());
		Application localApp = local.getApplicationForId(application.getId());
		if (installed) {
			if (localApp == null) {
				localApp = refApp.copyLocal();
				local.addApplication(localApp);
			}
			localApp.addVersion(refApp.getLastVersion().copy());
		} else {
			if (localApp != null) {
				Version localVersion = localApp.getVersionForId(refApp.getLastVersion().getId());
				if (localVersion != null) {
					localApp.removeVersion(localVersion);
				}
			}
		}
		if (localApp != null) {
			localApp.setUpdated();
		}
	}

	public boolean isLastVersionInstalled(Application app) {
		if (app != null) {
			Application localApp = getLocal().getApplicationForId(app.getId());
			if (localApp != null) {
				return app.getLastVersion() != null //
						&& localApp.getVersionForId(app.getLastVersion().getId()) != null;
				// && !version.isIgnored();
			}
		}
		return false;
	}

	public boolean isApplicationIgnored(String appId) {
		Application localApp = getLocal().getApplicationForId(appId);
		return localApp != null //
				&& localApp.getLastVersion() != null //
				&& localApp.getLastVersion().isIgnored() //
				;
	}

	public void ignoreLastVersionForApplication(Application application, boolean ignore) {
		Application localApp = getLocal().getApplicationForId(application.getId());
		if (ignore) {
			if (localApp == null) {
				localApp = application.copy();
				getLocal().addApplication(localApp);
			}
			if (localApp.getLastVersion() == null) {
				localApp.addVersion(application.getLastVersion().copy());
			}
			localApp.getLastVersion().setIgnored(true);
		} else {
			if (localApp != null) {
				localApp.removeVersion(localApp.getLastVersion());
			}
//			Application refApp = getReference().getApplicationForId(application.getId());
//			if (refApp != null) {
//				refApp.getLastVersion().setIgnored(false);
//			}
		}
	}

	public Map<String, List<Application>> getApplicationsByCategory(List<Application> applications) {
		Map<String, List<Application>> result = new HashMap<String, List<Application>>();
		for (Application app : applications) {
			List<Application> apps = result.get(app.getCategory());
			if (apps == null) {
				apps = new ArrayList<Application>();
				result.put(app.getCategory(), apps);
			}
			apps.add(app);
		}
		return result;
	}

	public boolean isNewApplication(String applicationId) throws ParseException {
		Date localUpdateDate = getLocal().getUpdateDate();
		Application application = getReference().getApplicationForId(applicationId);
		Date appUpdateDate = application.getUpdateDate();
		
		return appUpdateDate.after(localUpdateDate);
	}

}
