package com.samtrest.easy_postboy;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class   DBConnection
{
	static  Logger log = LoggerFactory.getLogger(DBConnection.class );
	public static String ORACLE_REPOSITORY_DB = "Oracle" ;
	public static String HSQL_LOCAL_REPOSITORY_DB = "HiperSQL Local" ;
	public static String HSQL_REMOTE_REPOSITORY_DB = "HiperSQL Server" ;

	private static String stmt;

	private  Connection genDBCOnn;
	private  String DBName ;
	private  String DBHost ;
	private  String DBPort ;

	private  String usr ;
	private  String pwd ;

	private  String urlString ;
	private  String connType = ORACLE_REPOSITORY_DB;
	private  boolean isLocalDB = false;

	public  void createGenDBConnection() throws SQLException{
		loadDBDriver();
		urlString = getUrlString();
		try	{
			genDBCOnn = DriverManager.getConnection(urlString,usr,pwd);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(),UICommonUtil.formatMessage(e));
			return;
			//			System.exit(666);
		}		
		DatabaseMetaData dbmd = genDBCOnn.getMetaData();
		if (log.isDebugEnabled()){
			log.debug("=====  Database info =====");  
			log.debug("DatabaseProductName: {}", dbmd.getDatabaseProductName() );  
			log.debug("DatabaseProductVersion: {}" , dbmd.getDatabaseProductVersion() );  
			log.debug("DatabaseMajorVersion: {}" , dbmd.getDatabaseMajorVersion());  
			log.debug("DatabaseMinorVersion: {}" , dbmd.getDatabaseMinorVersion() );  
			log.debug("=====  Driver info =====");  
			log.debug("DriverName: {}" , dbmd.getDriverName() );  
			log.debug("DriverVersion: {}" , dbmd.getDriverVersion() );  
			log.debug("DriverMajorVersion: {}" , dbmd.getDriverMajorVersion() );  
			log.debug("DriverMinorVersion: {}" , dbmd.getDriverMinorVersion() );  
			log.debug("=====  System info =====");  
			Properties props = System.getProperties();
			Enumeration e = props.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				log.debug("{}: {}",key,props.getProperty(key));
			}  
			log.debug("=====  JDBC/DB attributes =====");  
			log.debug("Supports getGeneratedKeys(): ");  
			if (dbmd.supportsGetGeneratedKeys() )  
				log.debug("true");  
			else  
				log.debug("false");
		}
		log.debug("Total memory: "+Runtime.getRuntime().totalMemory()/1024/1024+"Mb");
		String env = "";
		String OS = System.getProperty("os.name").toLowerCase();
		// System.out.println(OS);
		if (OS.indexOf("windows 9") > -1){
			env = RunCommand.exec( "command.com /c set" ,"Y");
		}else if ( (OS.indexOf("nt") > -1) || OS.indexOf("windows 7") > -1
				|| OS.indexOf("windows 200") > -1|| OS.indexOf("windows 8") > -1  
				|| OS.indexOf("windows xp") > -1|| OS.indexOf("windows server 200") > -1) {
			env = RunCommand.exec( "cmd.exe /c set" ,"Y");
		}else {
			// our last hope, we assume Unix (thanks to H. Ware for the fix)
			env = RunCommand.exec( "env"  ,"Y");
		}
		//		System.out.println(env);
	}

	public void closeGenDBConnection() throws SQLException{
		genDBCOnn.commit();
		if( Memory.isHSQLRepository() && isLocalDB)	{
			Statement stm = genDBCOnn.createStatement();
			stm.executeUpdate("SHUTDOWN");
		}
		genDBCOnn.close();
	}

	public void setLocalDB(boolean isLocalDB) {
		this.isLocalDB = isLocalDB;
	}
	public  String getDBHost() {
		return DBHost;
	}
	public  void setDBHost(String host) {
		DBHost = host;
	}
	public  String getDBPort() {
		return DBPort;
	}
	public  void setDBPort(String port) {
		DBPort = port;
	}
	public  String getPwd() {
		return pwd;
	}
	public  void setPwd(char[] wd) {
		pwd = new String(wd);
	}
	public  String getUrlString() {
		if (connType.equals(ORACLE_REPOSITORY_DB)){
			if (!"".equals(DBHost))
				urlString = "jdbc:oracle:thin:@"+DBHost+":"+DBPort+":"+DBName;
			urlString = "jdbc:oracle:thin:@(DESCRIPTION="                   +
					"(ADDRESS_LIST="              +
					"(ADDRESS=(PROTOCOL=TCP)" +
					"(HOST="+DBHost+")"    +
					"(PORT="+DBPort+")"    +
					")"                       +
					")"                           +
					"(CONNECT_DATA="              +
					"(SERVICE_NAME="+DBName+")"      +
					"(SERVER=DEDICATED)"      +
					")"                           +
					")";
			if ("".equals(DBHost) && !"".equals(DBName))
				urlString = "jdbc:oracle:thin:@"+DBName;
		}else if (connType.equals(HSQL_REMOTE_REPOSITORY_DB)){
			urlString = "jdbc:hsqldb:hsql://"+DBHost+":"+DBPort+"/"+DBName;
		}else if (connType.equals(HSQL_LOCAL_REPOSITORY_DB)){
			usr = Sets.HSQLDBUser;
			pwd = Sets.HSQLDBPwd;
			urlString = "jdbc:hsqldb:hsql://"+Sets.HSQLDBHost+":"+Sets.HSQLDBPort+"/"+Sets.HSQLDBName;
		}
		return urlString;
	}

	public void loadDBDriver(){
		if (connType.equals(ORACLE_REPOSITORY_DB))
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(new JFrame(),
						"Oracle jdbc driver not found.");
				System.exit(666);
			}else if (Memory.isHSQLRepository())
				try {
					Class.forName("org.hsqldb.jdbcDriver");
				} catch (ClassNotFoundException e) {
					JOptionPane.showMessageDialog(new JFrame(),
							"HiperSQL DB driver not found.");
					System.exit(666);
				}
	}

	public  void setUrlString(String urlStr) {
		urlString = urlStr;
	}

	public  String getUsr() {
		return usr.toUpperCase();
	}

	public  void setUsr(String u) {
		usr = u;
	}

	public  String getDBName() {
		return DBName;
	}

	public  void setDBName(String name) {
		DBName = name;
	}

	public  Connection getGenDBConn() {
		return genDBCOnn;
	}

	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
	}
}