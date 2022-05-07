package tmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class DynamoDBMapperExample {

	static AmazonDynamoDB client;
	static final String ACCESS_KEY = System.getenv("AWS_ACCESS_KEY_ID");
	static final String SECRET_KEY = System.getenv("AWS_SECRET_KEY");
	static AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

	public static void main(String[] args) throws IOException {

		// Set the AWS region you want to access.
		client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

		DimensionType dimType = new DimensionType();
		dimType.setHeight("8.00");
		dimType.setLength("11.0");
		dimType.setThickness("1.0");

		Book book = new Book();
		book.setId(502);
		book.setTitle("Book 502");
		book.setISBN("555-5555555555");
//		book.setBookAuthors(new HashSet<String>(Arrays.asList("Author1", "Author2")));
//		book.setBookAuthors(new ArrayList<String>(Arrays.asList("Author1", "Author2")));
		String url = "https://simeonpanda.com/collections/shakers";
		Document document = Jsoup.connect(url).get();
		Elements imgs = document.select("img");

		StringBuilder html = new StringBuilder();
		List<String> htmlList = new ArrayList<String>();
		int cnt = 0;
		for (Element img : imgs) {
			if (!img.attr("abs:src")
					.contains("note")) {
				String absSrc = img.attr("abs:src");
				html.append(cnt);
				html.append(",");
				html.append(absSrc);
				htmlList.add(html.toString());
				cnt++;
			}
		}
		System.out.println(htmlList.toString().hashCode());
		System.out.println("-1169793276");
		book.setBookAuthors(htmlList);
		book.setDimensions(dimType);

		DynamoDBMapper mapper = new DynamoDBMapper(client);
		mapper.save(book);

		Book bookRetrieved = mapper.load(Book.class, 502);
		System.out.println("Book info: " + "\n" + bookRetrieved);

		bookRetrieved.getDimensions().setHeight("9.0");
		bookRetrieved.getDimensions().setLength("12.0");
		bookRetrieved.getDimensions().setThickness("2.0");


		url = "https://justlift.com/collections/outerwear";
		document = Jsoup.connect(url).get();
		imgs = document.select("img");
		cnt = 0;
		for (Element img : imgs) {
			if (!img.attr("abs:src")
					.contains("note")) {
				String absSrc = img.attr("abs:src");
				html.append(cnt);
				html.append(",");
				html.append(absSrc);
				htmlList.add(html.toString());
				cnt++;
			}
		}
		bookRetrieved.setBookAuthors(htmlList);

		mapper.save(bookRetrieved);

		bookRetrieved = mapper.load(Book.class, 502);
		System.out.println("Updated book info: " + "\n" + bookRetrieved);
	}

	@DynamoDBTable(tableName = "ProductCatalog")
	public static class Book {
		private int id;
		private String title;
		private String ISBN;
		private List<String> bookAuthors;
		private DimensionType dimensionType;

		// Partition key
		@DynamoDBHashKey(attributeName = "Id")
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@DynamoDBAttribute(attributeName = "Title")
		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		@DynamoDBAttribute(attributeName = "ISBN")
		public String getISBN() {
			return ISBN;
		}

		public void setISBN(String ISBN) {
			this.ISBN = ISBN;
		}

		@DynamoDBAttribute(attributeName = "Authors")
		public List<String> getBookAuthors() {
			return bookAuthors;
		}

		public void setBookAuthors(List<String> bookAuthors) {
			this.bookAuthors = bookAuthors;
		}

		@DynamoDBTypeConverted(converter = DimensionTypeConverter.class)
		@DynamoDBAttribute(attributeName = "Dimensions")
		public DimensionType getDimensions() {
			return dimensionType;
		}

		@DynamoDBAttribute(attributeName = "Dimensions")
		public void setDimensions(DimensionType dimensionType) {
			this.dimensionType = dimensionType;
		}

		@Override
		public String toString() {
			return "Book [ISBN=" + ISBN + ", bookAuthors=" + bookAuthors + ", dimensionType= "
					+ dimensionType.getHeight() + " X " + dimensionType.getLength() + " X "
					+ dimensionType.getThickness()
					+ ", Id=" + id + ", Title=" + title + "]";
		}
	}

	static public class DimensionType {

		private String length;
		private String height;
		private String thickness;

		public String getLength() {
			return length;
		}

		public void setLength(String length) {
			this.length = length;
		}

		public String getHeight() {
			return height;
		}

		public void setHeight(String height) {
			this.height = height;
		}

		public String getThickness() {
			return thickness;
		}

		public void setThickness(String thickness) {
			this.thickness = thickness;
		}
	}

	// Converts the complex type DimensionType to a string and vice-versa.
	static public class DimensionTypeConverter implements DynamoDBTypeConverter<String, DimensionType> {

		@Override
		public String convert(DimensionType object) {
			DimensionType itemDimensions = (DimensionType) object;
			String dimension = null;
			try {
				if (itemDimensions != null) {
					dimension = String.format("%s x %s x %s", itemDimensions.getLength(), itemDimensions.getHeight(),
							itemDimensions.getThickness());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dimension;
		}

		@Override
		public DimensionType unconvert(String s) {

			DimensionType itemDimension = new DimensionType();
			try {
				if (s != null && s.length() != 0) {
					String[] data = s.split("x");
					itemDimension.setLength(data[0].trim());
					itemDimension.setHeight(data[1].trim());
					itemDimension.setThickness(data[2].trim());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return itemDimension;
		}
	}
}