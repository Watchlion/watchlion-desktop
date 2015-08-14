package ch.inagua.watchlion.service.os.osx;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;


public class OsxInfoPlistReaderTest {
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void test_parse() throws Exception {
		String path = getClass().getResource("/Info.plist.xml").getPath();

		OsxInfoPlistReader reader = new OsxInfoPlistReader(path);
		
		assertThat(reader.getBundleVersion(), equalTo("3.5.3.201506180105"));
		assertThat(reader.getBundleShortVersionString(), equalTo("3.5.3"));
	}
}
