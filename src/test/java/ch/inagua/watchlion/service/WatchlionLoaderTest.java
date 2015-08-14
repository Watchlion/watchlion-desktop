package ch.inagua.watchlion.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Environment;

public class WatchlionLoaderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_loadFromJSONFile() throws Exception {
		String jsonFile = getClass().getResource("/watchlion.ref.json").getPath();
		Environment watchlion = new WatchlionLoader().loadFromJSONFile(jsonFile);

		assertThat(watchlion.getProtocol(), equalTo("0.0.1"));
		assertThat(watchlion.getVersion(), equalTo("001"));
		assertThat(watchlion.getUpdate(), equalTo("2015.08.04 05:47:50"));

		List<Application> apps = watchlion.getApplications();
		assertThat(apps, hasSize(3));
		{
			Application app = apps.get(0);
			assertThat(app.getId(), equalTo("nodejs"));
			assertThat(app.getName(), equalTo("Node.js"));
			assertThat(app.getInstructions(), equalTo("Download and double click"));
			List<Application.Version> versions = app.getVersions();
			assertThat(versions, hasSize(1));
			{
				Application.Version version = versions.get(0);
				assertThat(version.getId(), equalTo("1.1"));
				assertThat(version.getName(), equalTo("1.1"));
				assertThat(version.getUpdate(), equalTo("2015.08.04 05:47:50"));
			}
		}
		{
			assertThat(apps.get(1).getId(), equalTo("dropbox"));
			assertThat(apps.get(2).getId(), equalTo("mybashprofile"));
		}
	}

}
