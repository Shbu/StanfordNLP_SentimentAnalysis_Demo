package com.yelp;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.sentiments.analyzers.SentimentAnalyzer;

/**
 * Servlet implementation class Yelp
 */

public class Yelp {
	
	private SentimentAnalyzer sentimentAnalyzer=new SentimentAnalyzer();
	private static final long serialVersionUID = 1L;
	static final private String CONTENT_TYPE = "text/html";

	OAuthService service;
	Token accessToken;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public static void main(String[] a) {
		/*Yelp yp = new Yelp("YgCxN__gT3xLomQqD2LqHg",
				"V57KujRBinicVlhhzFTuF9qAYjw",
				"0Ce_tOkX-70vMF66xVxbEtbtDpDkNkI6",
				"-7avLlFIGB5Zf8UsR1_Wbrx3R_I");*/

		// String searchResponse = yp.search("Taxi", "Paris");
		// System.out.println("Yelp result" + searchResponse);
		Yelp yp=new Yelp();
		yp.doGet();

	}

	public Yelp(String consumerKey, String consumerSecret, String token,
			String tokenSecret) {
		this.service = (OAuthService) new ServiceBuilder()
				.provider(YelpApi2.class).apiKey(consumerKey)
				.apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}

	public Yelp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String search(String term, String loc) {
		/*
		 * OAuthRequest request1 = new OAuthRequest(Verb.GET,
		 * "http://api.yelp.com/v2/business/1886-cafe-and-bakery-austin");
		 */
		OAuthRequest request1 = new OAuthRequest(Verb.GET,
				"http://api.yelp.com/business_review_search");
		// request1.addQuerystringParameter("term", term);
		// request1.addQuerystringParameter("location", loc);
		request1.addQuerystringParameter("term", "Hotel");
		request1.addQuerystringParameter("limit", "20");
		request1.addQuerystringParameter("ywsid", "q-ZnZaRpB-Upw1fJ8nnKfQ");
		request1.addQuerystringParameter("location", "Tucson");
		this.service.signRequest(this.accessToken, request1);
		Response reponse = request1.send();
		return reponse.getBody();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet() {
		// TODO Auto-generated method stub
		String consumerKey = "YgCxN__gT3xLomQqD2LqHg";
		String consumerSecret = "V57KujRBinicVlhhzFTuF9qAYjw";
		String token = "0Ce_tOkX-70vMF66xVxbEtbtDpDkNkI6";
		String tokenSecret = "-7avLlFIGB5Zf8UsR1_Wbrx3R_I";

		Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
		String reponse = yelp.search("taxi", "San Francisco");
		System.out.println("raw response" + reponse);
		// response.setContentType(CONTENT_TYPE);
		// PrintWriter out = response.getWriter();
		// out.println("<html><head></head><body>What if i have no clue"+
		// reponse+"</body></html>");
		// System.out.println(reponse);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(reponse);
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray businesses = (JSONArray) jsonObject.get("businesses");

		/*
		 * response.setContentType(CONTENT_TYPE); PrintWriter out =
		 * response.getWriter();
		 */

		String table = "<table>";

		for (int i = 0; i < businesses.size(); i++) {
			JSONObject lang = (JSONObject) businesses.get(i);

			// System.out.print((String)lang.get("name")+"; address::");
			String address1 = (String) lang.get("address1");
			String address2 =  (String) lang.get("address2");
			String address3 =  (String) lang.get("address3");
			Double avg_rating =   (Double) lang.get("avg_rating");
			String city =  (String) lang.get("city");
			Double distance =  (Double) lang.get("distance");
			String phone =  (String) lang.get("phone");
			String photo_url =  (String) lang.get("photo_url");
			String rating_img_url =  (String) lang.get("rating_img_url");
			String state = (String) lang.get("state");
			Long review_count=(Long) lang.get("review_count");
			System.out.println("review_count: " + lang.get("review_count"));
			JSONArray reviewList = (JSONArray) lang.get("reviews");
			System.out.println("review object" + address1 + "|" + address2
					+ "|" + address3 + "|" + city + "|" + distance + "|"
					+ phone + "|" + photo_url + "|" + rating_img_url + "|"
					+ state + "|");

			for (int j = 0; j < reviewList.size(); j++) {
				JSONObject reviewItem = (JSONObject) reviewList.get(j);
				String text_excerpt = (String) reviewItem
						.get("text_excerpt");
				String user_name = (String) reviewItem.get("user_name");
				String user_photo_url = (String) reviewItem
						.get("user_photo_url");

				System.out
						.println("review object" + reviewItem + "|"
								+ text_excerpt + "|" + user_name + "|"
								+ user_photo_url);
				System.out.println("text_excerpt: " +text_excerpt);
				
				System.out.println("Final Values: " +sentimentAnalyzer.calculateSentimentForGivenString(text_excerpt));
				System.out.println("----------------------------------------------------");
			}

		}

		/*
		 * String add = ""; for (int k = 0; k < structures.size(); k++) { add +=
		 * structures.get(k).toString() + "</br>"; } table = table +
		 * "<tr><td><img src='" + (String) lang.get("image_url") + "'</td><td>"
		 * + (String) lang.get("name") + "<img src='" + (String)
		 * lang.get("rating_img_url") + "'><br><b>Phone</b>:" + (String)
		 * lang.get("display_phone") + "<br><b>Address</b>:" + add +
		 * "</td></tr><tr></tr>";
		 * 
		 * } table = table + "</table>";
		 * System.out.println("<html><head></head><body>" + table +
		 * "</body></html>");
		 */
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost() {

	}

}
