package io.cutoutsy.utils;

public class ConfigParas {
    public static ReadConfigUtil readConfigUtil= new ReadConfigUtil("/opt/build/config.properties", true);

    public static String redis_host = readConfigUtil.getValue("redis_host");
    public static String redis_port = readConfigUtil.getValue("redis_port");
    public static String redis_auth = readConfigUtil.getValue("redis_auth");
    public static String redis_database = readConfigUtil.getValue("redis_database");

    public static String smtp_server_host = readConfigUtil.getValue("smtp_server_host");
    public static String smtp_server_port = readConfigUtil.getValue("smtp_server_port");
    public static String smtp_username = readConfigUtil.getValue("smtp_username");
    public static String smtp_password = readConfigUtil.getValue("smtp_password");
    public static String smtp_from_address = readConfigUtil.getValue("smtp_from_address");
    public static String mail_db = readConfigUtil.getValue("mail_db");
}
