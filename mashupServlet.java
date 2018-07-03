

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Servlet implementation class mashupServlet
 */
@WebServlet("/mashupServlet")
public class mashupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mashupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Mashup_Query query = new Mashup_Query();
		PrintWriter out = response.getWriter();
		@SuppressWarnings({ "resource", "deprecation" })
		
		//connection to database
		DB db = (new MongoClient("localhost", 27017).getDB("local"));
        DBCollection dbCollection = db.getCollection("myChannel_mashup");
        
        //retrieving what the user wants to query
		String valueOfSubmit = request.getParameter("submit");
		
		//writing output to html file
		out.println("<html><link rel='stylesheet' href='CSS/style.css'>");
		out.println("<body>");
		
		//if query is based on updated year
		if(valueOfSubmit.equals("SubmitYear")){
			ArrayList<String> result = new ArrayList<String>();
			String year = request.getParameter("updated_year");
			result = query.queryBasedOnYear(dbCollection, year);
			for (int index = 0; index < result.size();index++){
				out.println(result.get(index)+"<style='margin-top:10px;'>");
				out.println("<br>");
			}
		}
		
		//if query is based on used APIs
		if(valueOfSubmit.equals("SubmitApis")){
			ArrayList<String> result = new ArrayList<String>();
			String apis = request.getParameter("used_apis");
			result = query.queryBasedOnApis(dbCollection, apis);
			for (int index = 0; index < result.size();index++){
				out.println(result.get(index)+"<style='margin-top:10px;'>");
				out.println("<br>");
			}
		}
		
		//if query is based on tags
		if(valueOfSubmit.equals("SubmitTags")){
			ArrayList<String> result = new ArrayList<String>();
			String tags = request.getParameter("tags");
			result = query.queryBasedOnTags(dbCollection, tags);
			for (int index = 0; index < result.size();index++){
				out.println(result.get(index)+"<style='margin-top:10px;'>");
				out.println("<br>");
			}
		}
		
		//if query is based on keywords
		if(valueOfSubmit.equals("SubmitKeywords")){
			ArrayList<String> result = new ArrayList<String>();
			String keywords = request.getParameter("keywords");
			result = query.queryBasedOnKeywords(dbCollection, keywords);
			for (int index = 0; index < result.size();index++){
				out.println(result.get(index));
				out.println("<br>");
			}
		}
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
