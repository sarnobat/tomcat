package a;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Globals;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.core.StandardService;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;
import org.apache.catalina.servlets.WebdavServlet;
import org.apache.catalina.startup.Constants;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.Tomcat.DefaultWebXmlListener;
import org.apache.catalina.startup.Tomcat.ExistingStandardWrapper;
import org.apache.tomcat.util.buf.UriUtil;

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
			int port = 4453;

			String hostname = "localhost";
			StandardService standardService = createService();

			StandardHost standardHost = createStandardHost(hostname,
					createEngine(hostname, standardService));

			StandardServer standardServer = createServer(catalinaHome,
					getBaseFile(ensureBaseDir(port, root, catalinaHome)),
					standardService, port);

			String contextPath = "";
			StandardContext standardContext = createAppContext(standardHost,
					contextPath, Paths.get(root).toAbsolutePath().toString(),
					createListenerViaReflection(standardHost.getConfigClass()))
					.addChild2(
							new ExistingStandardWrapper(new WebdavServlet() {
								@Override
								public void service(HttpServletRequest req, HttpServletResponse resp)
							            throws ServletException, IOException {
									System.out
											.println("Main.main(...).new WebdavServlet() {...}.service()");
									super.service(req, resp);
									
									
								}
							},
									"webdavservlet"))
					.addServletMappingDecoded2("/webdav/*", "webdavservlet");

			Tomcat tomcat = new Tomcat(hostname, standardServer, standardHost)
					.addWebapp2(standardContext);

			Server serverVar = tomcat.start2().getServerVar();

			serverVar.await();
		}
	}

	public String createStandardServiceEngine(String domain,
			String defaultHost, String baseDir, Server server) throws Exception {

		StandardEngine engine = new StandardEngine();
		engine.setDomain(domain);
		engine.setName(domain);
		engine.setDefaultHost(defaultHost);

		Service service = new StandardService();
		service.setContainer(engine);
		service.setName(domain);

		server.addService(service);

		return engine.getObjectName().toString();
	}

	private static StandardHost createStandardHost(String hostname, Engine engine) {
		StandardHost host1;
		if (engine.findChildren().length > 0) {
			host1 = (StandardHost) engine.findChildren()[0];
		} else {
			host1 = new StandardHost();
			host1.setName(hostname);
			engine.addChild(host1);
		}
		return host1;
	}

	private static Engine createEngine(String hostname, Service service) {
		Engine engine = new StandardEngine();
		engine.setName("Tomcat");
		engine.setDefaultHost(hostname);
		engine.setRealm(new SimpleRealm());
		service.setContainer(engine);
		return engine;

	}

	private static final Map<String, String> userPass = new HashMap<>();
	private static final Map<String, List<String>> userRoles = new HashMap<>();
	private static final Map<String, Principal> userPrincipals = new HashMap<>();

	private static class SimpleRealm extends RealmBase {

		@Override
		protected String getPassword(String username) {
			return userPass.get(username);
		}

		@Override
		protected Principal getPrincipal(String username) {
			Principal p = userPrincipals.get(username);
			if (p == null) {
				String pass = userPass.get(username);
				if (pass != null) {
					p = new GenericPrincipal(username, pass,
							userRoles.get(username));
					userPrincipals.put(username, p);
				}
			}
			return p;
		}
	}

	private static StandardService createService() {
		StandardService service = new StandardService();
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

	private static LifecycleListener createListenerViaReflection(
			String configClass) {
		LifecycleListener listener = null;
		try {
			Class<?> clazz = Class.forName(configClass);
			listener = (LifecycleListener) clazz.getConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			// Wrap in IAE since we can't easily change the method signature to
			// to throw the specific checked exceptions
			throw new IllegalArgumentException(e);
		}

		if (listener instanceof ContextConfig) {
			// prevent it from looking ( if it finds one - it'll have dup error
			// )
			((ContextConfig) listener)
					.setDefaultWebXml(Constants.NoDefaultWebXml);
		} else {
			throw new RuntimeException(
					"Does this ever happen? If not, get rid of the cast");
		}
		return listener;
	}

	private static Context createAppContext(Host host, String contextPath,
			String docBase, LifecycleListener config) {

		Context ctx = createContext(host, contextPath);
		ctx.setPath(contextPath);
		ctx.setDocBase(docBase);
		ctx.addLifecycleListener(getDefaultWebXmlListener());
		ctx.setConfigFile(getWebappConfigFile(docBase, contextPath, host));

		ctx.addLifecycleListener(config);
		return ctx;
	}

	private static Context createContext(Host host, String url) {
		String contextClass = getContextClassName(host);
		try {
			return (Context) Class.forName(contextClass).getConstructor()
					.newInstance();
		} catch (ReflectiveOperationException | IllegalArgumentException
				| SecurityException e) {
			throw new IllegalArgumentException(
					"Can't instantiate context-class " + contextClass
							+ " for host " + host + " and url " + url, e);
		}
	}

	private static String getContextClassName(Host host) {
		String contextClass = StandardContext.class.getName();
		if (host == null) {
			throw new RuntimeException("This should never happen");
		}
		if (host instanceof StandardHost) {
			contextClass = ((StandardHost) host).getContextClass();
		} else {
			throw new RuntimeException("This should never happen");
		}
		return contextClass;
	}

	@Deprecated // Create your own listener that doesn't use web.xml
	private static LifecycleListener getDefaultWebXmlListener() {
		return new DefaultWebXmlListener();
	}

	private static URL getWebappConfigFile(String path, String contextName,
			Host host) {
		File docBase = new File(path);
		if (docBase.isDirectory()) {
			return getWebappConfigFileFromDirectory(docBase, contextName, host);
		} else {
			return getWebappConfigFileFromJar(docBase, contextName, host);
		}
	}

	private static URL getWebappConfigFileFromDirectory(File docBase,
			String contextName, Host host) {
		URL result = null;
		File webAppContextXml = new File(docBase,
				Constants.ApplicationContextXml);
		if (webAppContextXml.exists()) {
			try {
				result = webAppContextXml.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static URL getWebappConfigFileFromJar(File docBase,
			String contextName, Host host) {
		URL result = null;
		try (JarFile jar = new JarFile(docBase)) {
			JarEntry entry = jar.getJarEntry(Constants.ApplicationContextXml);
			if (entry != null) {
				result = UriUtil.buildJarUrl(docBase,
						Constants.ApplicationContextXml);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
