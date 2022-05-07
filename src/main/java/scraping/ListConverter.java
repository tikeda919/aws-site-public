package scraping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import pojo.ScrapingHtml;

public class ListConverter implements DynamoDBTypeConverter<List<String>, List<ScrapingHtml>> {
//public class ListConverter implements DynamoDBTypeConverter<String, ScrapingHtml> {

	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

//	@Override
//	public String convert(ScrapingHtml object) {
//		ScrapingHtml itemScrapingHtmls = (ScrapingHtml) object;
//		String item = null;
//		try {
//			if (itemScrapingHtmls != null) {
//				item = String.format("%s & %s", itemScrapingHtmls.getHtmlNo(), itemScrapingHtmls.getHtmlStr());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return item;
//	}
//
//	@Override
//	public ScrapingHtml unconvert(String s) {
//		ScrapingHtml itemScrapingHtml = new ScrapingHtml();
//		try {
//			if (s != null && s.length() != 0) {
//				String[] data = s.split("&");
//				itemScrapingHtml.setHtmlNo(data[0].trim());
//				itemScrapingHtml.setHtmlStr(data[1].trim());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return itemScrapingHtml;
//
//	}

		@Override
		public List<String> convert(List<ScrapingHtml> object) {
			List<String> strList = new ArrayList<>();
			for (ScrapingHtml string : object) {
				try {
					strList.add(mapper.writeValueAsString(string));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			return strList;
		}

		@Override
		public List<ScrapingHtml> unconvert(List<String> object) {
			List<ScrapingHtml> scrapingHtml = new ArrayList<>();
			for (String string : object) {
				ScrapingHtml element;
				try {
					element = mapper.readValue(string, ScrapingHtml.class);
					scrapingHtml.add(element);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			return scrapingHtml;
		}
}
