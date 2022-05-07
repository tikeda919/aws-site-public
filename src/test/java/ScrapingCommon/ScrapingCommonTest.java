package ScrapingCommon;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import scraping.ScrapingCommon;

public class ScrapingCommonTest {
	@Test
	public void testScrapingCommon() {
		ScrapingCommon scraping = new ScrapingCommon();
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse res = new MockHttpServletResponse();
		try {
			scraping.service(req, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("ans","/ans.jsp",res.getForwardedUrl());
	}
}
