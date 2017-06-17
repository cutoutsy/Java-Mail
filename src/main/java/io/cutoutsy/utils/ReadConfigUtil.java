package io.cutoutsy.utils;

import java.io.*;
import java.util.Properties;

public class ReadConfigUtil {
    public InputStream in = null;
    public BufferedReader br = null;
    private Properties config = null;
    private String lineConfigTxt;

    public String getLineConfigTxt() {
        return lineConfigTxt;
    }

    public void setLineConfigTxt(String lineConfigTxt) {
        this.lineConfigTxt = lineConfigTxt;
    }

    //configFilePath若是非普通文件,即properties文件的话,要另行处理
    public ReadConfigUtil(String configFilePath, boolean isConfig){
        try{
            in = new FileInputStream(new File(configFilePath));
            if(isConfig){
                config = new Properties();
                config.load(in);
                in.close();
            }else{
                br = new BufferedReader(new InputStreamReader(in));
                this.lineConfigTxt = getTextLines();
            }
        }catch (IOException e){
            System.out.println("加载配置文件时,出现问题");
        }
    }

    public String getValue(String key){
        try{
            String value = config.getProperty(key);
            return value;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("ConfigInfoError" + e.toString());
            return null;
        }
    }

    private String getTextLines(){
        StringBuilder sb = new StringBuilder();
        String temp = null;
        try{
            while ((temp = br.readLine()) != null){
                if(temp.trim().length() > 0 && (!temp.trim().startsWith("#"))){
                    sb.append(temp);
                    sb.append("\n");
                }
            }
            br.close();
            in.close();
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("read config file error:"+ e.toString());
        }
        return sb.toString();
    }

}
