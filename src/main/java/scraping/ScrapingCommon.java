package scraping;

import java.io.File;
import java.io.IOException;

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

/**
 * Servlet implementation class ScrapingCommon
 */
public class ScrapingCommon extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScrapingCommon() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext ctx = request.getServletContext();
		String url = "https://justlift.com/";
		Document document = Jsoup.connect(url).get();
		Elements imgs = document.select("img");
		StringBuilder html = new StringBuilder();
		for (Element img : imgs) {
			if (!img.attr("abs:src")
					.contains("note")) {
				String absSrc = img.attr("abs:src");
				System.out.println("<img src=\"" + absSrc + "><br>");
				html.append("<img src=\"");
				html.append(absSrc);
				html.append("\"");
				html.append(">");
				html.append("<br>");
				html.append(System.getProperty("line.separator"));
			}
		}
		document = Jsoup.parse(html.toString());
		request.setAttribute("document", document);


		try {
			String path = ctx.getRealPath("WEB-INF/pathlist.xml");
			File file = new File(path);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder  = factory.newDocumentBuilder();
			org.w3c.dom.Document domDocument = builder.parse(file);
			domDocument.getDocumentElement().normalize();
			org.w3c.dom.Element  urlList = domDocument.getDocumentElement();
			NodeList nList = urlList.getElementsByTagName("Url");
			for (int i = 0; i < nList.getLength(); i++) {
				System.out.println(nList.item(i).getTextContent());
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		//		String url = "https://justlift.com/";
//		Document doc = Jsoup.connect(url).get();
//		Elements links = doc.select("a");
//		StringBuilder html = new StringBuilder();
//		for (Element link : links) {
//			if (!link.attr("abs:href")
//					.contains("note")) {
//				String linkName = link.text();
//				String absHref = link.attr("abs:href"); // "http://jsoup.org/"
//				System.out.println("<a href=\"" + absHref + ">" + linkName + "</a>");
//				html.append("<a href=\"");
//				html.append(absHref);
//				html.append("\"");
//				html.append(">");
//				html.append(linkName);
//				html.append("</a><br>");
//				html.append(System.getProperty("line.separator"));
//			}
//		}
//		Document document = Jsoup.parse(html.toString());
//		request.setAttribute("document", document);
		RequestDispatcher dispatcher = request.getRequestDispatcher("./jsp/ScrapingCommon.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
