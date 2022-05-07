package pojo;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.NoArgsConstructor;

/*
 * POJO Object with DynamoDB Mapper Annotations representing the
 *  DynamoDB table pollynotes
 */
@NoArgsConstructor
@DynamoDBTable(tableName = "scrapingHtml")
public class ScrapingHtml {
	private int siteId;
	private String siteName;
	private String siteUrl;
	private String dateTime;
	private String hashCode;
	private List<String> strHtml = new ArrayList<String>();

	@DynamoDBHashKey(attributeName = "siteId")
	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	@DynamoDBAttribute(attributeName = "siteName")
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	@DynamoDBAttribute(attributeName = "siteUrl")
	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	@DynamoDBAttribute(attributeName = "dateTime")
	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@DynamoDBAttribute(attributeName = "hashCode")
	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

	@DynamoDBAttribute(attributeName = "strHtml")
//	@DynamoDBTypeConverted(converter = ListConverter.class)
	public List<String> getStrHtml() {
		return strHtml;
	}

	public void setStrHtml(List<String> strHtml) {
		this.strHtml = strHtml;
	}

//	public void appendScrapingHtmlList(ScrapingHtml scrapingHtml) {
//		strHtml.add(scrapingHtml);
//	}
}
