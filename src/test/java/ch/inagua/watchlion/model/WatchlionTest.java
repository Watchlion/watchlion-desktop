package ch.inagua.watchlion.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import ch.inagua.watchlion.service.WatchlionLoader;

public class WatchlionTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_getApplicationForId() throws Exception {
		Watchlion watchlion = new Watchlion();

		Application banana = new Application().setId("banana");
		watchlion.addApplication(banana);
		watchlion.addApplication(new Application().setId("orange"));
		watchlion.addApplication(new Application().setId("kiwi"));

		assertThat(watchlion.getApplicationForId("banana"), equalTo(banana));
		assertThat(watchlion.getApplicationForId("mango"), nullValue());
	}

	@Test
	public void test_report() throws Exception {
		String refJSON = getClass().getResource("/WatchlionDiffTest/watchlion.ref.json").getPath();
		Watchlion watchlion = new WatchlionLoader().loadFromJSONFile(refJSON);
		
		assertThat(watchlion.getReport(), equalTo("Missing versions:\n - Node.js [1.1]\n - Ruby On Rails [1.1]\n - Dropbox [1.0]\n - my .bash_profile [snapshot]\n"));

	}

}
