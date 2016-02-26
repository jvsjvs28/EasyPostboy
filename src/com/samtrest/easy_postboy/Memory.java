package com.samtrest.easy_postboy;

import java.sql.Connection;
import java.util.Properties;

public class Memory {
    private static TagObject[] jsonElements;
    private static String targetDir;
    private static Connection dbCon;
	private static Hint hint;
	private static String reo;
	private static String s1,s2,s3;
    private static String trial_msg = "";
    private static DBConnection dbConnection;
    private static Properties epProps;
	private static boolean hsqldbJARexists = false;

	public static MainFrame mainFrame;
	public static TagMapper tagMapper;
	public static byte [] bytes;

    public static int [] sD = new int[3],hD = new int[3],dbD = new int[3];
    public static String sK,hK,dbK,dbVer;
    public static String dmn,hst,ip;
    public static RtfTemplate rtfProcessor;
    private static String repositoryType;
    

    public static Hint getHint() {
		return hint;
	}
	public static void setHint(Hint hint) {
		Memory.hint = hint;
	}
	public static String getReo() {
		return reo;
	}
	public static void setReo(String reo) {
		Memory.reo = reo;
	}
	public static String getS1() {
		return s1;
	}
	public static void setS1(String s1) {
		Memory.s1 = s1;
	}
	public static String getS2() {
		return s2;
	}
	public static void setS2(String s2) {
		Memory.s2 = s2;
	}
	public static String getS3() {
		return s3;
	}
	public static void setS3(String s3) {
		Memory.s3 = s3;
	}
	public static String getIp() {
		return ip;
	}
	public static String getHst() {
		return hst;
	}
	public static String getDmn() {
		return dmn;
	}
	public static String getTrial_msg() {
		return trial_msg;
	}
	public static TagObject[] getJsonElements() {
		return jsonElements;
	}
	public static void setJsonElements(TagObject[] jsonElements) {
		Memory.jsonElements = jsonElements;
	}
	public static String getTargetDir() {
		return targetDir;
	}
	public static void setTargetDir(String targetDir) {
		Memory.targetDir = targetDir;
	}
	public static Connection getDbCon() {
		return dbCon;
	}
	public static void setDbCon(Connection dbCon) {
		Memory.dbCon = dbCon;
	}
	public static RtfTemplate getTemplate() {
		return rtfProcessor;
	}
	public static String getRepositoryType() {
		return repositoryType;
	}
	public static void setRepositoryType(String repositoryType) {
		Memory.repositoryType = repositoryType;
	}
	public static boolean isHSQLRepository(){
		return repositoryType.equals(DBConnection.HSQL_REMOTE_REPOSITORY_DB) || 
				repositoryType.equals(DBConnection.HSQL_LOCAL_REPOSITORY_DB);
	}
	public static boolean isOracleRepository(){
		return repositoryType.equals(DBConnection.ORACLE_REPOSITORY_DB);
	}
	public static DBConnection getDbConnection() {
		return dbConnection;
	}
	public static void setDbConnection(DBConnection dbConnection) {
		Memory.dbConnection = dbConnection;
	}
	public static Properties getEpProps() {
		return epProps;
	}
	public static void setEpProps(Properties epProps) {
		Memory.epProps = epProps;
	}
	public static boolean isHsqldbJARexists() {
		return hsqldbJARexists;
	}
	public static void setHsqldbJARexists(boolean hsqldbJARexists) {
		Memory.hsqldbJARexists = hsqldbJARexists;
	}
}
