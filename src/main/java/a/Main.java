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

    System.out.println(
        "SRIDHAR Main.main() - classpath = " + System.getProperty("java.class.path"));

    int port = 4453;

    // Document root
    // Server context root
    String root = System.getProperty("user.home") + "/Desktop";
    if (System.getProperties().containsKey("webdav.root")) {
      root = System.getProperty("webdav.root");
    }
    Tomcat server = new Tomcat(port, "localhost", root);

    // Application context root
    File application = Paths.get(root).toFile();
    if (!application.exists()) {
      application.mkdirs();
    }

    try {
      Context appContext = server.addWebapp("", Paths.get(root).toAbsolutePath().toString());

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
      appContext.addParameter("listings", "true");
      appContext.addServletMappingDecoded("/helloworld", "helloWorldServlet");

      WebdavServlet servlet = new WebdavServlet();
      //      {
      //          /**
      //           * Initialize this servlet.
      //           */
      //          @Override
      //          public void init()
      //              throws ServletException {
      //
      //              super.init();
      //              super.getServletConfig().getServletContext().setInitParameter("listings", "true");
      //          }
      //      };
      //      ServletConfig servletConfig = servlet.getServletConfig();
      //  servlet.init(servletConfig);
      Tomcat.addServlet(appContext, "webdavservlet", servlet);
      appContext.addServletMappingDecoded("/webdav/*", "webdavservlet");
      //      appContext.addServletMapping("/*", "webdavservlet");
      appContext
          .getServletContext()
          .getServletRegistrations()
          .get("webdavservlet")
          .setInitParameter("listings", "true");
      server.start();
      System.out.println(
          "SRIDHAR Main.main() - " + appContext.getServletContext().getServletRegistrations());
      System.out.println("Tomcat server: http://" + server.getHost().getName() + ":" + port + "/");
      System.out.println(
          "SRIDHAR Main.main() - Try visiting this url:  http://"
              + server.getHost().getName()
              + ":"
              + port
              + "/1.txt");
      server.getServer().await();
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (LifecycleException e) {
      e.printStackTrace();
    }
  }
}
