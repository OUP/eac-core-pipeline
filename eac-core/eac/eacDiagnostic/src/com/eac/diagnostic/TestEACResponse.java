package com.eac.diagnostic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClient;
import com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import com.amazonaws.util.IOUtils;

public class TestEACResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static String envProtocol = "http://";
	static final String securedProtocol = "https://";
	static final Properties propE = new Properties();
	static final String protocol = "http://";
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if ((session != null)
				&& (session.getAttribute("diagnosticSessionId") != null)
				&& (!"".equals(session.getAttribute("diagnosticSessionId")))) {
			doPost(request, response);
		} else {
			response.sendRedirect("/eacDiagnostic/");
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CheckEACResponse call started");
		HttpSession session = request.getSession(false);
		InputStream is = getClass().getResourceAsStream(
				"/resources/eacDiagnostic.properties");
		propE.load(is);

		String env = propE.getProperty("diagnosti.defaultenvironment");
		if (request.getParameter("Environment") != null) {
			env = request.getParameter("Environment").toString();
		}
		String eacweb = propE.getProperty("diagnostic.url.eacweb");
		String eacAdmin = propE.getProperty("diagnostic.url.eacAdmin");
		String eacAccount = propE.getProperty("diagnostic.url.eacAccount");
		String eacWSDL = propE.getProperty("diagnostic.url.eac.wsdl");
		String atyponWSDL = propE.getProperty("diagnostic.url.Atypon.wsdl");
		String AtyponAdmin = propE.getProperty("diagnostic.url.atyponAdmin");
		String httpsPort = propE.getProperty("diagnostic.port.https");
		final String httpPort=propE.getProperty("diagnostic.port.http");
		String colon = propE.getProperty("diagnostic.colon");
		String defaultPort = propE.getProperty("diagnostic.proxy.port");
		int passResponseCode = Integer.parseInt(propE
				.getProperty("diagnostic.passResponseCode"));
		String passResponse = propE.getProperty("diagnostic.pass");
		String failResponse = propE.getProperty("diagnostic.fail");
		String fileSeperator = propE.getProperty("diagnostic.fileseperator");
		String secretKey = propE.getProperty("diagnosti.secretkey");
		String accessKey = propE.getProperty("diagnosti.accesskey");
		String atyponCheckAuthText = propE
				.getProperty("diagnostic.atyponCheckAuthText");
		String atyponCheckAuthContext = propE
				.getProperty("diagnostic.atyponCheckAuth.context");
		String eacDBTest = propE.getProperty("diagnostic.eac.db.text");
		String atyponDBTest = propE.getProperty("diagnostic.atypon.db.text");
		String concateDatabaseName = propE
				.getProperty("diagnostic.db.concate.dbname.text");
		String atyponCheckAuthResponse = propE
				.getProperty("diagnostic.atyponCheckAuth.response");
		String enableProxy = propE.getProperty("diagnostic.enable.proxy");
		String envSmall = env.toLowerCase();

		String eacDomainUrl = propE.getProperty(envSmall
				+ ".diagnostic.eac.url");
		String atyponDomainUrl = propE.getProperty(envSmall
				+ ".diagnostic.Atypon.url");
		String eacLoadBalancerUrl = propE.getProperty(envSmall
				+ ".diagnostic.eac.loadbalancer");
		String atyponLoadBalancerUrl = propE.getProperty(envSmall
				+ ".diagnostic.Atypon.loadbalancer");
		String atyponCheckAuthPath = propE.getProperty(envSmall
				+ ".diagnostic.atyponCheckAuth.path");

		String dbConnection = propE.getProperty(envSmall
				+ ".diagnostic.db.connectionstring");
		String eacDBName = propE.getProperty(envSmall
				+ ".diagnostic.eac.db.name");
		String atyponDBName = propE.getProperty(envSmall
				+ ".diagnostic.atypon.db.name");
		String userName = propE.getProperty(envSmall
				+ ".diagnostic.db.username");
		String password = propE.getProperty(envSmall
				+ ".diagnostic.db.password");
		String eacErightsAutoScale = propE.getProperty(envSmall
				+ ".diagnostic.db.eacErightsAutoScaleName");
		
		System.out.println("Read All property successfully");
		List<diagnosticbean> responseList = new ArrayList<diagnosticbean>();
		List<diagnosticbean> responseListAtypon = new ArrayList<diagnosticbean>();
		List<diagnosticbean> responseListMiscellaneous = new ArrayList<diagnosticbean>();

		AWSCredentials credentials = new BasicAWSCredentials(accessKey,
				secretKey);
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTPS);
		if (propE.getProperty("diagnostic.proxy.oup") != null) {
			clientConfig
					.setProxyHost(propE.getProperty("diagnostic.proxy.oup"));
			clientConfig.setProxyPort(Integer.parseInt(defaultPort));
		}
		AmazonAutoScalingClient clientAuto = null;
		if (enableProxy.equalsIgnoreCase("Y")) {
			clientAuto = new AmazonAutoScalingClient(credentials, clientConfig);
		} else {
			clientAuto = new AmazonAutoScalingClient(credentials);
		}
		System.out.println("Client object is not null : " + String.valueOf(clientAuto != null) );
		System.out.println("Client object service name : " + clientAuto.getServiceName() );
		AmazonElasticLoadBalancingClient cl = new AmazonElasticLoadBalancingClient(
				credentials);
		cl.setRegion(Region.getRegion(Regions.EU_WEST_1));
		DescribeLoadBalancersResult ld = cl.describeLoadBalancers();
		List<LoadBalancerDescription> test = ld.getLoadBalancerDescriptions();
		for (LoadBalancerDescription ldd : test) {
			ldd.getBackendServerDescriptions();
		}
		AmazonEC2 ec2 = null;
		if (enableProxy.equalsIgnoreCase("Y")) {
			ec2 = new AmazonEC2Client(credentials, clientConfig);
		} else {
			ec2 = new AmazonEC2Client(credentials);
		}
		ec2.setRegion(Region.getRegion(Regions.EU_WEST_1));
		
		System.out.println("EC2 object is not null : " + String.valueOf(ec2 != null) );
		clientAuto.setRegion(Region.getRegion(Regions.EU_WEST_1));
		DescribeAutoScalingGroupsResult autoScalingGroupsResult = clientAuto
				.describeAutoScalingGroups();
		List<AutoScalingGroup> autoScalingGroups = autoScalingGroupsResult
				.getAutoScalingGroups();
		System.out.println("Auto scalling group size : " + autoScalingGroups.size());
		boolean executeEACURL = true;
		boolean executeAtyponURL = true;
		for (AutoScalingGroup autoScalingGroup : autoScalingGroups) {
			boolean containEAC = autoScalingGroup.getAutoScalingGroupName()
					.startsWith(eacLoadBalancerUrl);
			boolean containAtypon = autoScalingGroup.getAutoScalingGroupName()
					.startsWith(atyponLoadBalancerUrl);
			if (containEAC) {
				int responseCode = 0;
				diagnosticbean diagnostic = new diagnosticbean();
				if (executeEACURL) {
					executeEACURL = false;
					diagnostic.setInstanceName(eacDomainUrl);
					try {
						responseCode = 0;

						envProtocol = securedProtocol;
						responseCode = getResponseCode(eacDomainUrl
								+ fileSeperator + eacweb);
						if (responseCode == passResponseCode) {
							diagnostic.setEacWebResponse(passResponse);
						} else {
							diagnostic.setEacWebResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in eacWeb ");
						e.printStackTrace();
						diagnostic.setEacWebResponse(failResponse);
					}
					try {
						responseCode = 0;
						responseCode = getResponseCode(eacDomainUrl
								+ fileSeperator + eacAdmin);
						if (responseCode == passResponseCode) {
							diagnostic.setEacAdminResponse(passResponse);
						} else {
							diagnostic.setEacAdminResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in eacAdmin ");
						e.printStackTrace();
						diagnostic.setEacAdminResponse(failResponse);
					}
					try {
						responseCode = 0;
						responseCode = getResponseCode(eacDomainUrl
								+ fileSeperator + eacAccount);
						if (responseCode == passResponseCode) {
							diagnostic.setEacAccountResponse(passResponse);
						} else {
							diagnostic.setEacAccountResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in eacAccount ");
						e.printStackTrace();
						diagnostic.setEacAccountResponse(failResponse);
					}
					try {
						responseCode = 0;
						responseCode = getResponseCode(eacDomainUrl
								+ fileSeperator + eacWSDL);
						if (responseCode == passResponseCode) {
							diagnostic.setEacWSDLResponse(passResponse);
						} else {
							diagnostic.setEacWSDLResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in eacWSDL ");
						e.printStackTrace();
						diagnostic.setEacWSDLResponse(failResponse);
					}
					responseList.add(diagnostic);
				}
				for (com.amazonaws.services.autoscaling.model.Instance instance : autoScalingGroup
						.getInstances()) {
					envProtocol = protocol;
					String publicDNS = getInstancePublicDnsName(
							instance.getInstanceId(), ec2);

					String basicURL = publicDNS + colon + httpPort
							+ fileSeperator;
					diagnostic = new diagnosticbean();

					diagnostic.setInstanceName(instance.getInstanceId());
					try {
						responseCode = 0;

						responseCode = getResponseCode(basicURL + eacweb);
						if (responseCode == passResponseCode) {
							diagnostic.setEacWebResponse(passResponse);
						} else {
							diagnostic.setEacWebResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in eacWeb autoscaling");
						e.printStackTrace();
						diagnostic.setEacWebResponse(failResponse);
					}
					try {
						responseCode = 0;
						responseCode = getResponseCode(basicURL + eacAdmin);
						if (responseCode == passResponseCode) {
							diagnostic.setEacAdminResponse(passResponse);
						} else {
							diagnostic.setEacAdminResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in eacAdmin autoscaling");
						e.printStackTrace();
						diagnostic.setEacAdminResponse(failResponse);
					}
					try {
						responseCode = 0;
						responseCode = getResponseCode(basicURL + eacAccount);
						if (responseCode == passResponseCode) {
							diagnostic.setEacAccountResponse(passResponse);
						} else {
							diagnostic.setEacAccountResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in eacAccount autoscaling");
						e.printStackTrace();
						diagnostic.setEacAccountResponse(failResponse);
					}
					try {
						responseCode = 0;
						responseCode = getResponseCode(basicURL + eacWSDL);
						if (responseCode == passResponseCode) {
							diagnostic.setEacWSDLResponse(passResponse);
						} else {
							diagnostic.setEacWSDLResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in eacWSDL autoscaling");
						e.printStackTrace();
						diagnostic.setEacWSDLResponse(failResponse);
					}
					responseList.add(diagnostic);
				}
			} else if (containAtypon && autoScalingGroup.getAutoScalingGroupName().contains(eacErightsAutoScale)) {
				envProtocol = securedProtocol;
				int responseCode = 0;
				diagnosticbean diagnostic = new diagnosticbean();
				if (executeAtyponURL) {
					executeAtyponURL = false;
					diagnostic.setInstanceName(atyponDomainUrl);
					try {
						responseCode = 0;
						responseCode = getResponseCode(atyponDomainUrl
								+ fileSeperator + atyponWSDL);
						if (responseCode == passResponseCode) {
							diagnostic.setEacWSDLResponse(passResponse);
						} else {
							diagnostic.setEacWSDLResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in atyponWSDL ");
						e.printStackTrace();
						diagnostic.setEacWSDLResponse(failResponse);
					}
					try {
						responseCode = 0;
						responseCode = getResponseCode(atyponDomainUrl
								+ fileSeperator + AtyponAdmin);
						if (responseCode == passResponseCode) {
							diagnostic.setEacAdminResponse(passResponse);
						} else {
							diagnostic.setEacAdminResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in atyponAdmin ");
						e.printStackTrace();
						diagnostic.setEacAdminResponse(failResponse);
					}
					responseListAtypon.add(diagnostic);
				}
				for (com.amazonaws.services.autoscaling.model.Instance instance : autoScalingGroup
						.getInstances()) {
					envProtocol = protocol;
					String publicDNS = getInstancePublicDnsName(
							instance.getInstanceId(), ec2);

					String basicURL = publicDNS + colon + defaultPort
							+ fileSeperator;
					diagnostic = new diagnosticbean();

					diagnostic.setInstanceName(instance.getInstanceId());
					try {
						responseCode = 0;
						responseCode = getResponseCode(basicURL + atyponWSDL);
						if (responseCode == passResponseCode) {
							diagnostic.setEacWSDLResponse(passResponse);
						} else {
							diagnostic.setEacWSDLResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in atyponWSDL autoscaling");
						e.printStackTrace();
						diagnostic.setEacWSDLResponse(failResponse);
					}
					try {
						responseCode = 0;
						responseCode = getResponseCode(basicURL + AtyponAdmin);
						if (responseCode == passResponseCode) {
							diagnostic.setEacAdminResponse(passResponse);
						} else {
							diagnostic.setEacAdminResponse(failResponse);
						}
					} catch (Exception e) {
						System.out.println("Exception in atyponADMIN autoscaling");
						e.printStackTrace();
						diagnostic.setEacAdminResponse(failResponse);
					}
					responseListAtypon.add(diagnostic);
				}
			}
		}
		diagnosticbean diagnostic = new diagnosticbean();
		try {
			diagnostic.setInstanceName(atyponCheckAuthText);
			String status = null;
			status = getResponseBody(request.getServerName() + colon
					+ defaultPort + atyponCheckAuthContext
					+ atyponCheckAuthPath);
			if ((status != null) && (status.contains(atyponCheckAuthResponse))) {
				diagnostic.setEacCheckAuthResponse(passResponse);
			} else {
				diagnostic.setEacCheckAuthResponse(failResponse);
			}
		} catch (Exception e) {
			System.out.println("Exception in atyponCheckAutoResponse");
			e.printStackTrace();
			diagnostic.setEacCheckAuthResponse(failResponse);
		}
		try {
			diagnostic.setInstanceName(eacDBTest);
			String status = null;
			String eacConnectionString = dbConnection + concateDatabaseName
					+ eacDBName;
			status = checkDBConnection(eacConnectionString, userName, password);
			diagnostic.setEacDBStatus(status);
		} catch (Exception e) {
			System.out.println("Exception in eacDbConnection");
			e.printStackTrace();
			diagnostic.setEacDBStatus(failResponse);
		}
		try {
			diagnostic.setInstanceName(atyponDBTest);
			String status = null;
			String atyponConnectionString = dbConnection + concateDatabaseName
					+ atyponDBName;
			status = checkDBConnection(atyponConnectionString, userName,
					password);
			diagnostic.setAtyponDBStatus(status);
		} catch (Exception e) {
			System.out.println("Exception in eacDbConnection");
			e.printStackTrace();
			diagnostic.setAtyponDBStatus(failResponse);
		}
		responseListMiscellaneous.add(diagnostic);
		request.setAttribute("ResponseDeatilsList", responseList);
		request.setAttribute("responseListAtypon", responseListAtypon);
		request.setAttribute("responseListMiscellaneous",
				responseListMiscellaneous);

		request.getRequestDispatcher("WEB-INF/EACResponseStatus.jsp").forward(
				request, response);
	}

	private static String getInstancePublicDnsName(String instanceId,
			AmazonEC2 ec2) {
		DescribeInstancesResult describeInstancesRequest = ec2
				.describeInstances();
		List<Reservation> reservations = describeInstancesRequest
				.getReservations();
		for (Reservation reservation : reservations) {
			for (com.amazonaws.services.ec2.model.Instance instance : reservation
					.getInstances()) {
				if (instance.getInstanceId().equals(instanceId))
					return instance.getPublicDnsName();
			}
		}
		return null;

	}

	public static int getResponseCode(String urlString)
			throws MalformedURLException, IOException,
			NoSuchAlgorithmException, KeyManagementException {
		System.out.println("In getResponseCode of : " + urlString);
		String enableProxy = propE.getProperty("diagnostic.enable.proxy");
		URL u = new URL(envProtocol + urlString);
		HttpURLConnection huc = null;
		if (envProtocol.equals("https://")) {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs,
						String authType) {
				}
			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HostnameVerifier allHostsValid = new HostnameVerifier() {

				@Override
				public boolean verify(String hostname, SSLSession session) {
					// TODO Auto-generated method stub
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		}
		if ((enableProxy != null) && (enableProxy.equalsIgnoreCase("Y"))
				&& (propE.getProperty("diagnostic.proxy.oup") != null)) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress(
							propE.getProperty("diagnostic.proxy.oup"),
							Integer.parseInt(propE
									.getProperty("diagnostic.proxy.port"))));
			huc = (HttpURLConnection) u.openConnection(proxy);
			System.out.println("url : " + u);
		} else {
			System.out.println("url : " + u);
			huc = (HttpURLConnection) u.openConnection();
		}
		huc.setConnectTimeout(Integer.parseInt(propE
				.getProperty("diagnostic.url.connection.timeout")));
		huc.setReadTimeout(Integer.parseInt(propE
				.getProperty("diagnostic.url.connection.timeout")));
		huc.setRequestMethod("GET");
		huc.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
		huc.connect();
		return huc.getResponseCode();
	}

	public String getResponseBody(String urlString)
			throws MalformedURLException, IOException {
		URL u = new URL("https://" + urlString);
		String enableProxy = propE.getProperty("diagnostic.enable.proxy");

		HttpURLConnection huc = null;

		huc = (HttpURLConnection) u.openConnection();

		System.out.println("Atypon check auth" + u);
		huc.setConnectTimeout(Integer.parseInt(propE
				.getProperty("diagnostic.url.connection.timeout")));
		huc.setReadTimeout(Integer.parseInt(propE
				.getProperty("diagnostic.url.connection.timeout")));
		huc.connect();
		InputStream in = huc.getInputStream();

		String encoding = huc.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in);
		return body;
	}

	private String checkDBConnection(String connectionString, String userName,
			String password) throws SQLException, IOException {
		InputStream is = getClass().getResourceAsStream(
				"/resources/eacDiagnostic.properties");
		propE.load(is);
		String passResponse = propE.getProperty("diagnostic.pass");
		String failResponse = propE.getProperty("diagnostic.fail");
		String sqlCheckQuery = propE.getProperty("diagnostic.sql.check.query");
		Connection con = null;
		String status = failResponse;
		Statement stmt = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("connectionString ::::::" + connectionString);

			con = DriverManager.getConnection(connectionString, userName,
					password);
			stmt = con.createStatement();

			String sql = sqlCheckQuery;
			ResultSet rs = stmt.executeQuery(sql);

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			rs.close();
			if (columnCount > 0) {
				status = passResponse;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			String str1 = status;
			return str1;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				return status;
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				return status;
			}
		}
		return status;
	}
}
