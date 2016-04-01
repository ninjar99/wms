/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DBUtil;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author samzheng
 */
public class LogInfo {
    static int threadcount = 0;
    static int ftpcount = 0;
    static int sapcount = 0;
    static int sapcount2 = 0;

    public static void main(String[] args) {
        String newLog = " Date:" + new Date() + "  |";
        appendLog(newLog);
    }
    
    public static String getCurrentDate() {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        return sm.format(new Date());
    }
    
    public static String getCurrentDate_Short() {
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
        return sm.format(new Date());
    }

    public static String strRight(String value) {
        return value.substring(value.length() - 2, value.length());
    }

    public static void appendLog(String newLog) {
        Calendar c = new GregorianCalendar();
        File log = new File(System.getProperty("user.dir")+"/log/loginfo" + String.valueOf(c.get(c.YEAR))
                + strRight("00" + String.valueOf(c.get(c.MONTH)+1)) + strRight("00" + String.valueOf(c.get(c.DAY_OF_MONTH))) + ".log");
        try {
            if (!log.exists())//����ļ�������,���½�.
            {
                File parentDir = new File(log.getParent());
                if (!parentDir.exists())//�������Ŀ¼������,���½�.
                {
                    parentDir.mkdirs();
                }
                log.createNewFile();
            }
            fileAppend(log.getAbsolutePath(),newLog+" ["+getCurrentDate()+"]\r\n\r\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void appendLog(String logFileName,String newLog) {
        Calendar c = new GregorianCalendar();
        File log = new File(System.getProperty("user.dir")+"/log/"+logFileName + String.valueOf(c.get(c.YEAR))
                + strRight("00" + String.valueOf(c.get(c.MONTH)+1)) + strRight("00" + String.valueOf(c.get(c.DAY_OF_MONTH))) + ".log");
        try {
            if (!log.exists())//����ļ�������,���½�.
            {
                File parentDir = new File(log.getParent());
                if (!parentDir.exists())//�������Ŀ¼������,���½�.
                {
                    parentDir.mkdirs();
                }
                log.createNewFile();
            }
            fileAppend(log.getAbsolutePath(),newLog+" ["+getCurrentDate()+"]\r\n\r\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**   
     * ׷���ļ���ʹ��FileWriter   
     *    
     * @param fileName   
     * @param content   
     */    
    public static void fileAppend(String fileName, String content) {   
        FileWriter writer = null;  
        try {     
            // ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�     
            writer = new FileWriter(fileName, true);     
            writer.write(content);       
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
    } 
}
