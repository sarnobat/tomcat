package a;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;

import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.core.StandardService;
import org.apache.catalina.servlets.WebdavServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.Tomcat.ExistingStandardWrapper;

public class Main {

	// TODO: Why does this only work in a web browser, not when mapped from
	// Apple finder?
	// It worked with the Spring boot version.
	public static void main(String[] args) throws LifecycleException,
			ServletException {

		String root = System.getProperty("webdav.root",
				System.getProperty("user.home") + "/Desktop");

		System.setProperty("catalina.useNaming", "false");

		String catalinaHome = System.getProperty(Globals.CATALINA_HOME_PROP);

		{
			File application = Paths.get(root).toFile();
			if (!application.exists()) {
				application.mkdirs();
			}
		}
		{
			StandardServer server2;
			{
				int port = 4453;
				server2 = createServer(catalinaHome,
						getBaseFile(ensureBaseDir(4453, root, catalinaHome)),
						createService());
				server2.setPort(port);

				System.setProperty(Globals.CATALINA_BASE_PROP,
						server2.getBaseFile());
				System.setProperty(Globals.CATALINA_HOME_PROP, server2
						.getCatalinaHome().getPath());
			}

			{
				Tomcat server = new Tomcat("localhost", server2);

				{
					// TODO: create the app context separately from adding it to
					// the server
					StandardContext appContext = (StandardContext) server
							.addWebapp("",
									Paths.get(root).toAbsolutePath().toString())
							.addChild2(
									new ExistingStandardWrapper(
											new WebdavServlet(),
											"webdavservlet"))
							.addServletMappingDecoded2("/webdav/*",
									"webdavservlet");
				}
				server.start();
				server.getServerVar().await();
			}
		}
	}

	private static Service createService() {
		Service service = new StandardService();
		service.setName("Tomcat");
		return service;
	}

	private static File getBaseFile(String basedir) {
		File baseFile = new File(basedir);
		if (baseFile.exists()) {
			if (!baseFile.isDirectory()) {
				throw new IllegalArgumentException("tomcat.baseDirNotDir"
						+ baseFile);
			}
		} else {
			if (!baseFile.mkdirs()) {
				// Failed to create base directory
				throw new IllegalStateException("tomcat.baseDirMakeFail"
						+ baseFile);
			}
			/*
			 * If file permissions were going to be set on the newly created
			 * directory, this is the place to do it. However, even simple calls
			 * such as File.setReadable(boolean,boolean) behaves differently on
			 * different platforms. Therefore, setBaseDir documents that the
			 * user needs to do this.
			 */
		}
		try {
			baseFile = baseFile.getCanonicalFile();
		} catch (IOException e) {
			baseFile = baseFile.getAbsoluteFile();
		}
		return baseFile;
	}

	private static StandardServer createServer(String catalinaHome,
			File catalinaBase, Service service) {
		StandardServer server = new StandardServer();
		server.setCatalinaBase(catalinaBase);

		if (catalinaHome == null) {
			// TODO: we should fail sooner than this
			server.setCatalinaHome(catalinaBase);
		} else {
			File homeFile = new File(catalinaHome);
			if (!homeFile.isDirectory() && !homeFile.mkdirs()) {
				// Failed to create home directory
				throw new IllegalStateException("tomcat.homeDirMakeFail"
						+ homeFile);
			}
			try {
				homeFile = homeFile.getCanonicalFile();
			} catch (IOException e) {
				homeFile = homeFile.getAbsoluteFile();
			}
			server.setCatalinaHome(homeFile);
		}

		server.setPort(-1);
		server.addService(service);
		return server;
	}

	private static String ensureBaseDir(int port, final String iBasedir,
			String catalinaHome) {
		String basedir = iBasedir;
		if (basedir == null) {
			basedir = System.getProperty(Globals.CATALINA_BASE_PROP);
		}
		if (basedir == null) {
			basedir = catalinaHome;
		}
		if (basedir == null) {
			// Create a temp dir.
			basedir = System.getProperty("user.dir") + "/tomcat." + port;
		}
		return basedir;
	}
}
