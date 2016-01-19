package cs263w16;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.memcache.*;
@SuppressWarnings("serial")
public class DatastoreServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      resp.setContentType("text/html");
      resp.getWriter().println("<html><body>");
      
      //Add your code here
	  int argsCnt = req.getParameterMap().size();
      String keyname = req.getParameter("keyname");
      String value = req.getParameter("value");
	  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	  if(argsCnt == 0){
		    resp.getWriter().println("<b>Datastore:</b><br>");
		    Query query = new Query("TaskData");
            query.addSort(Entity.KEY_RESERVED_PROPERTY, SortDirection.ASCENDING);
            List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
            List<String> keynames = new ArrayList<String>();
			for (Entity entity : results) {
                resp.getWriter().println(entity.getKey().getName() + ": " + entity.getProperty("value") + "<br>");
                if (!keynames.contains(entity.getKey().getName())) {
                    keynames.add(entity.getKey().getName());
                }
            }
		 
	  }
	  else if(argsCnt == 1 && keyname != null && value == null){
		        Key entkey = KeyFactory.createKey("TaskData", keyname);
				Entity entity;
		     try{
				  entity = datastore.get(entkey);
				  value = (String) entity.getProperty("value");
				  resp.getWriter().println(keyname + ": " + value + " (Datastore)<br>");
			 }   
			 catch (EntityNotFoundException e) {
                  resp.getWriter().println("(Neither)<br>");
             }	
				
                // print
                //value = (String) entity.getProperty("value");
                //resp.getWriter().println(keyname + ": " + value + " (Datastore)<br>");
                
		   
	  }
	  else if(argsCnt == 2 && keyname != null && value != null){
		    Entity entity = new Entity("TaskData", keyname);
            entity.setProperty("date", new Date());
            entity.setProperty("value", value);
            datastore.put(entity);
            //syncCache.put(keyname, value);
            // print Stored KEY and VALUE in Datastore
            resp.getWriter().println("Stored " + keyname + " and " + value + " in Datastore<br>");
	  }
	  else{
		   resp.getWriter().println("Can't recognize the parameters.");
	  }
      resp.getWriter().println("</body></html>");
  }
}