package pojo;

import lombok.NoArgsConstructor;

/*
 * POJO Object with DynamoDB Mapper Annotations representing the
 *  DynamoDB table pollynotes
 */
//@DynamoDBDocument
@NoArgsConstructor
public class StrHtml {

	private int htmlNo;
	private String htmlStr;

	//	@DynamoDBAttribute(attributeName = "htmlNo")
	public int getHtmlNo() {
		return htmlNo;
	}

	public void setHtmlNo(int htmlNo) {
		this.htmlNo = htmlNo;
	}

	//	@DynamoDBAttribute(attributeName = "htmlStr")
	public String getHtmlStr() {
		return htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}
}
