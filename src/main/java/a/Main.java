package a;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.servlets.WebdavServlet;
import org.apache.catalina.startup.Tomcat;

public class Main {

  @SuppressWarnings("serial")
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
      Context appContext = server.addWebapp("", Paths.get(root).toAbsolutePath().toString());
      Tomcat.addServlet(
          appContext,
          "helloWorldServlet",
          new HttpServlet() {

            @Override
            protected void service(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
              response.setContentType("text/html;charset=utf-8");
              response.setStatus(HttpServletResponse.SC_OK);
              response.getWriter().println("<h1>Hello World</h1>");
            }
          });
      appContext.addParameter("listings", "true");
      appContext.addServletMappingDecoded("/helloworld", "helloWorldServlet");

      WebdavServlet servlet = new WebdavServlet();
      Tomcat.addServlet(appContext, "webdavservlet", servlet);
      appContext.addServletMappingDecoded("/webdav/*", "webdavservlet");
      appContext
          .getServletContext()
          .getServletRegistrations()
          .get("webdavservlet")
          .setInitParameter("listings", "true");
      server.start();
      server.getServerVar().await();
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (LifecycleException e) {
      e.printStackTrace();
    }
  }
}
