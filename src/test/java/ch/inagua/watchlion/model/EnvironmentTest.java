package ch.inagua.watchlion.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import ch.inagua.watchlion.service.WatchlionLoader;

public class EnvironmentTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_getApplicationForId() throws Exception {
		Environment environment = new Environment();

		Application banana = new Application().setId("banana");
		environment.addApplication(banana);
		environment.addApplication(new Application().setId("orange"));
		environment.addApplication(new Application().setId("kiwi"));

		assertThat(environment.getApplicationForId("banana"), equalTo(banana));
		assertThat(environment.getApplicationForId("mango"), nullValue());
	}

}
