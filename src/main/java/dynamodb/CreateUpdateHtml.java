package dynamodb;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import pojo.ScrapingHtml;

public class CreateUpdateHtml {

	/**
	 *
	 * This function does the following:
	 * 1. Takes a JSON payload from API gateway and converts it into a ScrapingHtml POJO {@link com.StrHtml.pollyscrapingHtmls.pojo.ScrapingHtml}
	 * 2. Creates or Updates the DynamoDB item based on the ScrapingHtml
	 * 3. Returns the scrapingHtmlId
	 *
	 * @param	scrapingHtml	POJO of ScrapingHtml created from the JSON sent from API GW into Lambda.
	 * @param	context	Lambda context which can be used for logging
	 * @return			The scrapingHtmlId of the scrapingHtml created or updated
	 */
	private static final String ACCESS_KEY = System.getenv("AWS_ACCESS_KEY_ID");
	private static final String SECRET_KEY = System.getenv("AWS_SECRET_KEY");
	private AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
	// Create the DynamoDB client and mapper
	//		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withEndpointConfiguration(
	//				new AwsClientBuilder.EndpointConfiguration("https://dynamodb.us-east-2.amazonaws.com", "us-east-2"))
	//				.build();
	AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();
	//		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
	DynamoDBMapper mapper = new DynamoDBMapper(client);

	public String CreateTable(ScrapingHtml scrapingHtmlList) {

		try {
			mapper.save(scrapingHtmlList);
		} catch (Exception e) {
			System.out.println(e);
		}

		System.out.println("Returning scrapingHtmlId: \"" + scrapingHtmlList.toString() + "\"");
		return scrapingHtmlList.toString();
	}
}