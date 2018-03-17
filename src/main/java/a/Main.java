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

    System.out.println("SRIDHAR Main.main() - classpath = " + System.getProperty("java.class.path"));

    Tomcat server = new Tomcat();
    server.setPort(8080);

    // Document root
    // Server context root
    server.setBaseDir("/tmp");

    // Application context root
    String appDocBase = "/tmp/";
    File application = Paths.get(appDocBase).toFile();
    if (!application.exists()) {
      application.mkdirs();
    }

    try {
      Context appContext = server.addWebapp("", Paths.get(appDocBase).toAbsolutePath().toString());

      // A Jetty AbstractHandler is an HttpServlet here:
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

      appContext.addServletMappingDecoded("/helloworld", "helloWorldServlet");

      Tomcat.addServlet(appContext, "webdavservlet", new WebdavServlet());
      //      appContext.addServletMapping("/webdav/*", "webdavservlet");
      //appContext.addServletMapping("/*", "webdavservlet");

      server.start();
      System.out.println("Tomcat server: http://" + server.getHost().getName() + ":" + 8080 + "/");
      server.getServer().await();
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (LifecycleException e) {
      e.printStackTrace();
    }
  }
}
