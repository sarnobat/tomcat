package a;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;

import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
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

		if (!Paths.get(root).toFile().exists()) {
			Paths.get(root).toFile().mkdirs();
		}
		{
			StandardServer server2;
			{
				int port = 4453;
				server2 = createServer(catalinaHome,
						getBaseFile(ensureBaseDir(port, root, catalinaHome)),
						createService(), port);
			}

			{
				Tomcat server = new Tomcat("localhost", server2);

				// TODO: create the app context separately from adding it to
				// the server
				server.addWebapp("",
						Paths.get(root).toAbsolutePath().toString(),
						server.getHost())
						.addChild2(
								new ExistingStandardWrapper(
										new WebdavServlet(), "webdavservlet"))
						.addServletMappingDecoded2("/webdav/*", "webdavservlet");

				server.start2().getServerVar().await();
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
		}
		try {
			baseFile = baseFile.getCanonicalFile();
		} catch (IOException e) {
			baseFile = baseFile.getAbsoluteFile();
		}
		return baseFile;
	}

	private static StandardServer createServer(String catalinaHome,
			File catalinaBase, Service service, int port) {
		StandardServer server = new StandardServer();
		server.setCatalinaBase(catalinaBase);
		server.setPort(port);

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
