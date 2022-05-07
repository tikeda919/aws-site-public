package dynamodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import pojo.ScrapingHtml;

public class SearchHtml {

	private static final String ACCESS_KEY = System.getenv("AWS_ACCESS_KEY_ID");
	private static final String SECRET_KEY = System.getenv("AWS_SECRET_KEY");
	private AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();
	//	AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
	DynamoDBMapper mapper = new DynamoDBMapper(client);

	public List<ScrapingHtml> queryTable(ScrapingHtml scrapingHtmlList) {

		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":val1",
				new AttributeValue().withN(String.valueOf(scrapingHtmlList.getSiteId())));

		DynamoDBQueryExpression<ScrapingHtml> queryExpression = new DynamoDBQueryExpression<ScrapingHtml>()
				.withKeyConditionExpression("siteId = :val1")
				.withExpressionAttributeValues(expressionAttributeValues);

		try {
			List<ScrapingHtml> htmlList = new ArrayList<ScrapingHtml>(
					mapper.query(ScrapingHtml.class, queryExpression));
			return htmlList;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			List<ScrapingHtml> htmlList = new ArrayList<ScrapingHtml>();
			return htmlList;
		}
	}

	public List<ScrapingHtml> scanTable() {

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

		try {
			List<ScrapingHtml> htmlList = mapper.scan(ScrapingHtml.class, scanExpression);
			return htmlList;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			List<ScrapingHtml> htmlList = new ArrayList<ScrapingHtml>();
			return htmlList;
		}
	}
}
