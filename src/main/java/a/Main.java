package a;

import java.io.File;
import java.nio.file.Paths;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.servlets.WebdavServlet;
import org.apache.catalina.startup.Tomcat;

public class Main {

  public static void main(String[] args) {

    int port = 4453;

    String root = System.getProperty("user.home") + "/Desktop";
    if (System.getProperties().containsKey("webdav.root")) {
      root = System.getProperty("webdav.root");
    }
    Tomcat server = new Tomcat(port, "localhost", root);
    File application = Paths.get(root).toFile();
    if (!application.exists()) {
      application.mkdirs();
    }

    try {
      StandardContext appContext = (StandardContext)server.addWebapp("", Paths.get(root).toAbsolutePath().toString());
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
