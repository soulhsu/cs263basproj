
package cs263w16;

import java.util.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;
// The Worker servlet should be mapped to the "/worker" URL.
public class Worker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("keyname");
        // Do something with key.
		String value = request.getParameter("value");

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

		Entity entity = new Entity("TaskData", key);
		entity.setProperty("value", value);
		entity.setProperty("date", new Date());
		datastore.put(entity);
		syncCache.put(key, value);
    }
}