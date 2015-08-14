package ch.inagua.watchlion.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Watchlion;

public class WatchlionDiffTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_loadFromJSONFile() throws Exception {
		String refJSON = getClass().getResource("/WatchlionDiffTest/watchlion.ref.json").getPath();
		Watchlion refWatchlion = new WatchlionLoader().loadFromJSONFile(refJSON);

		String localJSON = getClass().getResource("/WatchlionDiffTest/watchlion.local.json").getPath();
		Watchlion localWatchlion = new WatchlionLoader().loadFromJSONFile(localJSON);
		
		List<Application> apps = refWatchlion.diffWithLocal(localWatchlion);
		assertThat(apps, hasSize(2));
		{
			Application app = apps.get(0);
			assertThat(app.getId(), equalTo("nodejs"));
			assertThat(app.getLastVersionId(), equalTo("1.1"));
		}
		{
			Application app = apps.get(1);
			assertThat(app.getId(), equalTo("dropbox"));
			assertThat(app.getLastVersionId(), equalTo("1.0"));
		}
	}

}
