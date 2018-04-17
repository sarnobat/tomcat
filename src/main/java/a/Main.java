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
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.servlets.WebdavServlet;
import org.apache.catalina.startup.Tomcat;

public class Main {

  public static void main(String[] args) {

    int port = Integer.parseInt(System.getProperty("webdav.port", "4453"));

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
      org.apache.catalina.webresources.StandardRoot r = new org.apache.catalina.webresources.StandardRoot();
      r.setAllowLinking(true);
      appContext.setResources(r);

      appContext
          .getServletContext()
          .getServletRegistrations()
          .get("webdavservlet")
          .setInitParameter("listings", "true");
boolean taken = true;
{
	int port2 = port;
	try {
		(new java.net.Socket("localhost", port2)).close();
		System.out.println("port " + port2 + " is in use.");
		// Successful connection means the port is taken.
System.exit(-1);
	}
	catch(java.net.SocketException e) {
		System.out.println("port " + port2 + " is free.");
		taken = false;
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}
      server.start();
System.out.println("after start()");
      server.getServerVar().await();
System.out.println("after await()");
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (LifecycleException e) {
      e.printStackTrace();
    } catch (Exception e) {
	e.printStackTrace();
	System.exit(-1);
}
  }
}
