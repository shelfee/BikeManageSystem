package org.gyf.test;

import org.gyf.common.LocationInfo;
import org.gyf.common.UserInfo;
import org.gyf.servlet.SessionUserListener;

import javax.jws.soap.SOAPBinding;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by shelfee on 2017/2/9.
 */
public class test {
    public static String sessionId = "";
    public void showLocation(){
        String urlStr = "http://192.168.1.101:8080/LocationInfoServlet";
        try {
            URL urlLocation = new URL(urlStr);
            HttpURLConnection urlLocationConn = (HttpURLConnection) urlLocation.openConnection();
            urlLocationConn.connect();
            ObjectInputStream inUser = new ObjectInputStream(urlLocationConn.getInputStream());
            ArrayList<LocationInfo> locationInfos = (ArrayList<LocationInfo>) inUser.readObject();
            if(locationInfos==null)
                System.out.println("Fail");
            else {
                System.out.println("Location:");
                for(int i = 0; i < locationInfos.size(); i++){
                    System.out.println(locationInfos.get(i).getId() + ":" + String.valueOf(locationInfos.get(i).getBikeNum()));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void login(String id, String key){
        String urlStr = "http://192.168.1.101:8080/LoginServlet";

        try {
            URL urlLocation = new URL(urlStr);
            HttpURLConnection urlLoginConn = (HttpURLConnection) urlLocation.openConnection();
            urlLoginConn.setDoOutput(true);
            urlLoginConn.setRequestMethod("POST");
            OutputStreamWriter out = new OutputStreamWriter(urlLoginConn.getOutputStream(), "UTF-8");
            out.write("username=" + id + "&password=" + key);
            out.flush();
            out.close();
            urlLoginConn.connect();
            String cookieValue=urlLoginConn.getHeaderField("Set-Cookie");
            if(cookieValue != null)
                sessionId = cookieValue.substring(0, cookieValue.indexOf(";"));
            InputStream in =urlLoginConn.getInputStream();
            String strLine="";
            String strResponse ="";
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while((strLine =reader.readLine()) != null)
                strResponse +=strLine +"\n";
            System.out.print(strResponse);

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(String id){
        String urlStr = "http://192.168.1.101:8080/LogoutServlet";
        try {
            URL urlLocation = new URL(urlStr);
            HttpURLConnection urlLoginConn = (HttpURLConnection) urlLocation.openConnection();
            urlLoginConn.setRequestProperty("Cookie", sessionId);
            urlLoginConn.setDoOutput(true);
            urlLoginConn.setRequestMethod("POST");
            OutputStreamWriter out = new OutputStreamWriter(urlLoginConn.getOutputStream(), "UTF-8");
            out.write("username=" + id);
            out.flush();
            out.close();
            urlLoginConn.connect();
            InputStream in =urlLoginConn.getInputStream();
            String strLine="";
            String strResponse ="";
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while((strLine =reader.readLine()) != null)
                strResponse +=strLine +"\n";
            System.out.print(strResponse);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserInfo(String id){
        String urlStr = "http://192.168.1.101:8080/UserInfoServlet";
        try {
            URL urlLocation = new URL(urlStr);
            HttpURLConnection urlUserConn = (HttpURLConnection) urlLocation.openConnection();
            urlUserConn.setDoOutput(true);
            urlUserConn.setRequestMethod("POST");
            urlUserConn.setRequestProperty("Cookie", sessionId);
            OutputStreamWriter out = new OutputStreamWriter(urlUserConn.getOutputStream(), "UTF-8");
            out.write("username=" + id );
            out.flush();
            out.close();
            urlUserConn.connect();
            ObjectInputStream inUser = new ObjectInputStream(urlUserConn.getInputStream());
            UserInfo userInfo = (UserInfo) inUser.readObject();
            if(userInfo==null)
                System.out.println("Fail");
            else {
                System.out.println("id:" + userInfo.getId());
                System.out.println("balance" + userInfo.getBalance());
                System.out.println("bike"  + userInfo.getBike());
                System.out.println("time" + userInfo.getTime());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[]argv) {
        test a = new test();
        a.login("u1", "123");

    }
}
