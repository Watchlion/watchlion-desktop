package ch.inagua.watchlion.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Environment;
import ch.inagua.watchlion.model.Watchlion;

public class WatchlionTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_delta() throws Exception {
		String refJSON = getClass().getResource("/WatchlionDiffTest/watchlion.ref.json").getPath();
		Environment reference = new WatchlionLoader().loadFromJSONFile(refJSON);

		String localJSON = getClass().getResource("/WatchlionDiffTest/watchlion.local.json").getPath();
		Environment local = new WatchlionLoader().loadFromJSONFile(localJSON);
		
		Watchlion watchlion = new Watchlion(reference, local);
		
		List<Application> apps = watchlion.delta();
		assertThat(apps, hasSize(2));
		{
			Application app = apps.get(0);
			assertThat(app.getId(), equalTo("nodejs"));
			assertThat(app.getLastVersionId(), equalTo("1.1"));
		}
//		{
//			Application app = apps.get(1);
//			assertThat(app.getId(), equalTo("rails"));
//			assertThat(app.getLastVersionId(), equalTo("1.1"));
//		}
		{
			Application app = apps.get(1);
			assertThat(app.getId(), equalTo("dropbox"));
			assertThat(app.getLastVersionId(), equalTo("1.0"));
		}
//		{
//			Application app = apps.get(3);
//			assertThat(app.getId(), equalTo("mybashprofile"));
//			assertThat(app.getLastVersionId(), equalTo("snapshot"));
//		}
	}

	@Test
	public void test_report() throws Exception {
		String refJSON = getClass().getResource("/WatchlionDiffTest/watchlion.ref.json").getPath();
		Environment reference = new WatchlionLoader().loadFromJSONFile(refJSON);

		String localJSON = getClass().getResource("/WatchlionDiffTest/watchlion.local.json").getPath();
		Environment local = new WatchlionLoader().loadFromJSONFile(localJSON);
		
		Watchlion watchlion = new Watchlion(reference, local);
		
		// assertThat(watchlion.getReport(), equalTo("Missing versions:\n - Node.js [1.1]\n - Ruby On Rails [1.1]\n - Dropbox [1.0]\n - my .bash_profile [Snap]\n"));
		assertThat(watchlion.getReport(), equalTo("Missing versions:\n - Node.js [1.1]\n - Dropbox [1.0]\n"));

	}

}
