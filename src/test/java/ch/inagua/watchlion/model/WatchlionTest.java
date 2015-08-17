package ch.inagua.watchlion.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Environment;
import ch.inagua.watchlion.model.Watchlion;
import ch.inagua.watchlion.service.WatchlionLoader;
import ch.inagua.watchlion.test.file.FileUtils;

public class WatchlionTest {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	
	private static final Date BEFORE = createDate("01.01.2015");

	private static final Date NOW = createDate("06.06.2015");

	private static final Date AFTER = createDate("12.12.2015");

	private static final Date createDate(String source) {
		try {
			return DATE_FORMAT.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_delta() throws Exception {
		String refJSON = FileUtils.getPathOf("/WatchlionDiffTest/watchlion.ref.json");
		Environment reference = new WatchlionLoader().loadFromJSONFile(refJSON);

		String localJSON = FileUtils.getPathOf("/WatchlionDiffTest/watchlion.local.json");
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
		String refJSON = FileUtils.getPathOf("/WatchlionDiffTest/watchlion.ref.json");
		Environment reference = new WatchlionLoader().loadFromJSONFile(refJSON);

		String localJSON = FileUtils.getPathOf("/WatchlionDiffTest/watchlion.local.json");
		Environment local = new WatchlionLoader().loadFromJSONFile(localJSON);
		
		Watchlion watchlion = new Watchlion(reference, local);
		
		// assertThat(watchlion.getReport(), equalTo("Missing versions:\n - Node.js [1.1]\n - Ruby On Rails [1.1]\n - Dropbox [1.0]\n - my .bash_profile [Snap]\n"));
		assertThat(watchlion.getReport(), equalTo("Missing versions (2):\n - Node.js [1.1]\n - Dropbox [1.0]\n"));

	}

	@Test
	public void test_isNewApplication() throws Exception {
		Application dropbox = new Application().setId("DropbBox");
		Environment reference = new Environment().addApplication(dropbox);
		
		Environment local = new Environment();
		
		Watchlion watchlion = new Watchlion(reference, local);

		local.setUpdateDate(NOW);
		dropbox.setUpdateDate(AFTER);
		assertThat(watchlion.isNewApplication(dropbox.getId()), is(true));

		local.setUpdateDate(NOW);
		dropbox.setUpdateDate(BEFORE);
		assertThat(watchlion.isNewApplication(dropbox.getId()), is(false));
	}
}
