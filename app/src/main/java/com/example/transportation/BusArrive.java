package com.example.transportation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BusArrive {
    String key="spIogpzyHT653IuGRUeM7ATfWrT7y25rWagxWFC%2FGX5i4Brt2D1gfv%2B%2BO8RAeQILa61XvGKz1WhM9sqK%2BH9qyw%3D%3D";
    String output;
    boolean check1, check2;

    ArrayList arrmsg1 = new ArrayList<>();
    ArrayList arrmsg2 = new ArrayList<>();
    int arrmsg1toint, arrmsg2toint;
    String getarrmsg1string() {
        return arrmsg1.get(0).toString();
    }
    String getarrmsg2string() {
        return arrmsg2.get(0).toString();
    }
    void BusArriveManager(String tempstationid, String tempbustid, String tempord){
        check1 = false;
        check2 = false;
        arrmsg1.clear();
        arrmsg2.clear();
        String stationid = URLEncoder.encode(tempstationid);
        String bustid = URLEncoder.encode(tempbustid);
        String ord = URLEncoder.encode(tempord);

        String queryUrl="http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute" + "?ServiceKey="
                + key + "&stId=" + stationid + "&busRouteId=" + bustid + "&ord=" + ord;
        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));
            String tag;

            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        if(tag.equals("arrmsg1")) {
                            xpp.next();
                            arrmsg1.add(xpp.getText());
                        }
                        else if(tag.equals("arrmsg2")) {
                            xpp.next();
                            arrmsg2.add(xpp.getText());
                        }
                        break;
                }
                eventType=xpp.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(isStringDouble((arrmsg1.get(0).toString()).substring(0,1))) {
            //숫자이면
            arrmsg1toint = setTime(arrmsg1);
            System.out.println("arrmsg1toint" + arrmsg1toint);
            check1 = true;
        }
        if(isStringDouble((arrmsg2.get(0).toString()).substring(0,1))){
            System.out.println("(arrmsg2.get(0).toString()).substring(0,1))" + (arrmsg2.get(0).toString()).substring(0,1));
            check2 = true;
            arrmsg2toint = setTime(arrmsg2);
        }
        else {
            output = arrmsg1.get(0).toString() + ", \n\t" + arrmsg2.get(0).toString() + " 도착예정";
            check1 = false;
            check2 = false;
        }
    }

    void getBusArrive(String tempstationid, String tempbustid, String tempord){
        BusArriveManager(tempstationid, tempbustid, tempord);
    }
    boolean getcheck1(){
        return check1;
    }
    boolean getcheck2(){
        return check2;
    }

    int setTime(ArrayList arrmsg) {

        String value = arrmsg.get(0).toString();

        System.out.println(value);


        String minute =value.substring(0,value.lastIndexOf("분"));
        String second;
        if(value.contains("초")) {
            second =value.substring(value.lastIndexOf("분")+1,value.lastIndexOf("초"));
        }
        else {
            second = "0";
        }
        int intminute = Integer.parseInt(minute);
        int intsecond = Integer.parseInt(second);
        System.out.println("value는 " + value);
        System.out.println("time은 " + intminute);
        System.out.println("second는 " + intsecond);
        System.out.println("int니?" + (intminute*60 + intsecond));
        return intminute*60 + intsecond;
    }

    int getTime1() {
        return arrmsg1toint;
    }

    int getTime2() {
        return arrmsg2toint;
    }

    String getoutput() {
        return output;
    }

    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
