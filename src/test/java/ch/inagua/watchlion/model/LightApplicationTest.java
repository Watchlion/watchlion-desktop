package ch.inagua.watchlion.model;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;


public class LightApplicationTest {
	@Test
	public void test_parse() throws Exception {
		assertThat(LightApplication.parse(null), is(nullValue()));
		assertThat(LightApplication.parse(""), equalTo(new LightApplication("", "")));
		assertThat(LightApplication.parse("IBM Installation Manager"), equalTo(new LightApplication("IBM Installation Manager", "")));
		assertThat(LightApplication.parse("Adobe Flash Player 11 Plugin 11.3.300.270"), equalTo(new LightApplication("Adobe Flash Player 11 Plugin", "11.3.300.270")));
		assertThat(LightApplication.parse("Git version 1.9.5-preview20150319 1.9.5-preview20150319"), equalTo(new LightApplication("Git version", "1.9.5-preview20150319")));
		assertThat(LightApplication.parse("IntelliJ IDEA Community Edition 14.1.1 141.178.9"), equalTo(new LightApplication("IntelliJ IDEA Community Edition 14.1.1", "141.178.9")));
	}

	@Test
	public void test_containsNumbers() throws Exception {
		assertThat(LightApplication.containsNumbers("IBM"), is(false));
		assertThat(LightApplication.containsNumbers("v1.2.3"), is(true));
	}

	@Test
	public void test_nameToId() throws Exception {
		assertThat(LightApplication.nameToId(null), is(nullValue()));
		assertThat(LightApplication.nameToId("IBM"), equalTo("IBM"));
		assertThat(LightApplication.nameToId("I B M"), equalTo("I-B-M"));
		assertThat(LightApplication.nameToId(" I B M "), equalTo("I-B-M"));
	}

}
