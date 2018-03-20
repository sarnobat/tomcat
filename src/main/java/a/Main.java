package a;

import java.io.File;
import java.nio.file.Paths;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardService;
import org.apache.catalina.servlets.WebdavServlet;
import org.apache.catalina.startup.Tomcat;

public class Main {

  public static void main(String[] args) {

    String root = System.getProperty("webdav.root", System.getProperty("user.home") + "/Desktop");
    
    Service service = new StandardService();
    service.setName("Tomcat");
    
    // Do this in main()
    System.setProperty("catalina.useNaming", "false");
    
    Tomcat server = new Tomcat(4453, "localhost", root, service);
    File application = Paths.get(root).toFile();
    if (!application.exists()) {
      application.mkdirs();
    }

    try {
      // PICKUP create the app context separately from adding it to the server
      StandardContext appContext =
          (StandardContext) server.addWebapp("", Paths.get(root).toAbsolutePath().toString());
      Tomcat.addServlet(appContext, "webdavservlet", new WebdavServlet());
      appContext.addServletMappingDecoded("/webdav/*", "webdavservlet");
      server.start();
      server.getServerVar().await();
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (LifecycleException e) {
      e.printStackTrace();
    }
  }
}
