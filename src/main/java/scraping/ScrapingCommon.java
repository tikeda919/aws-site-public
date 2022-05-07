package scraping;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import dynamodb.CreateUpdateHtml;
import dynamodb.SearchHtml;
import pojo.ScrapingHtml;

/**
 * Servlet implementation class ScrapingCommon
 */
public class ScrapingCommon extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ScrapingCommon() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext ctx = request.getServletContext();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<ScrapingHtml> changedList = new ArrayList<ScrapingHtml>();
		Document document = null;
		StringBuilder html = new StringBuilder();
		try {
			String path = ctx.getRealPath("WEB-INF/pathlist.xml");
			File file = new File(path);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document domDocument = builder.parse(file);
			domDocument.getDocumentElement().normalize();
			org.w3c.dom.Element urlList = domDocument.getDocumentElement();
			NodeList nList = urlList.getElementsByTagName("Url");
			for (int i = 0; i < nList.getLength(); i++) {
				String url = nList.item(i).getTextContent();
				document = Jsoup.connect(url).get();
				Elements imgs = document.select("img");
				html = new StringBuilder();
				ScrapingHtml scrapingHtml = new ScrapingHtml();
				int htmlCnt = 0;
				org.w3c.dom.Element el = (org.w3c.dom.Element) nList.item(i);
				scrapingHtml.setSiteId(Integer.parseInt(el.getAttribute("no")));
				scrapingHtml.setSiteName(el.getAttribute("name"));
				scrapingHtml.setSiteUrl(url);
				List<String> strHtml = new ArrayList<String>();
				for (Element img : imgs) {
					if (!img.attr("abs:src")
							.contains("note")) {
						String absSrc = img.attr("abs:src");
						html.append("<img src=\"");
						html.append(absSrc);
						html.append("\"");
						html.append(">");
						html.append("<br>");
						html.append(System.getProperty("line.separator"));
						strHtml.add(htmlCnt + "," + absSrc);
						htmlCnt++;
					}
				}
				scrapingHtml.setStrHtml(strHtml);
				scrapingHtml.setHashCode(String.valueOf(strHtml.toString().hashCode()));

				SearchHtml searchHtml = new SearchHtml();
				searchHtml.scanTable();
				List<ScrapingHtml> oldScrapingHtml = searchHtml.queryTable(scrapingHtml);

				if (oldScrapingHtml.size() == 0
						|| !scrapingHtml.getHashCode().equals(oldScrapingHtml.get(0).getHashCode())) {
					scrapingHtml.setDateTime(sdf.format(date));
					CreateUpdateHtml createUpdateHtml = new CreateUpdateHtml();
					createUpdateHtml.CreateTable(scrapingHtml);
					changedList.add(scrapingHtml);
				} else {
					System.out.println(scrapingHtml.getSiteName() + "は、ハッシュコードが同一のため処理をパスします");
				}
			}

			if (changedList == null || changedList.size() == 1) {
				document = Jsoup.parse(html.toString());
				request.setAttribute("document", document);

				RequestDispatcher dispatcher = request.getRequestDispatcher("./jsp/ScrapingHtml.jsp");
				dispatcher.forward(request, response);

			} else {
				request.setAttribute("changedlist", changedList);

				RequestDispatcher dispatcher = request.getRequestDispatcher("./jsp/ScrapingList.jsp");
				dispatcher.forward(request, response);

			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
