package com.example.transportation;

import android.widget.ArrayAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class StationNameList {
    String key="spIogpzyHT653IuGRUeM7ATfWrT7y25rWagxWFC%2FGX5i4Brt2D1gfv%2B%2BO8RAeQILa61XvGKz1WhM9sqK%2BH9qyw%3D%3D";

    ArrayList station = new ArrayList<>();
    ArrayList stationNm = new ArrayList<>();
    ArrayList seq = new ArrayList<>();
    ArrayList arsId = new ArrayList<>();
    ArrayList direction = new ArrayList<>();

    ArrayList stationNameList = new ArrayList<>();

    ArrayList getStationList(String busid) {
        System.out.println("get Station 들어옴");
        StationNameListManager(busid);
        makestationNameList();
        return stationNameList;
    }

    ArrayList getStationID() {
        return station;
    }

    ArrayList getStationOrd() {
        return seq;
    }

    ArrayList getStationarsID() {
        return arsId;
    }
    ArrayList getStationName() {
        return stationNm;
    }


    ArrayList getStationNm(){
        return stationNm;
    }

    void makestationNameList() {
        for(int i = 0; i < station.size();i++) {
            stationNameList.add(stationNm.get(i).toString() + " (" + direction.get(i).toString() +"행)");
        }
    }

    void StationNameListManager(String tempbusid) {
        System.out.println("StationNameListManager 들어옴");
        String busid = URLEncoder.encode(tempbusid);

        String queryUrl="http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute" + "?ServiceKey="
                + key + "&busRouteId=" + busid;

        try {
            System.out.println("try 들어옴" + queryUrl);
            URL url = new URL(queryUrl);
            System.out.println("url" + url);
            InputStream is = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));
            String tag;

            int eventType = xpp.getEventType();
            System.out.println("eventType" + eventType);
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        if(tag.equals("station")) {
                            System.out.println("tag station 들어옴");
                            xpp.next();
                            station.add(xpp.getText());
                        }
                        else if(tag.equals("stationNm")) {
                            xpp.next();
                            stationNm.add(xpp.getText());
                        }
                        else if(tag.equals("seq")){
                            xpp.next();
                            seq.add(xpp.getText());
                        }
                        else if(tag.equals("arsId")){
                            xpp.next();
                            arsId.add(xpp.getText());
                        }
                        else if(tag.equals("direction")){
                            xpp.next();
                            direction.add(xpp.getText());
                        }

                        break;
                }
                eventType=xpp.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(direction);
    }
}
