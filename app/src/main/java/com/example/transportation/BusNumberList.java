package com.example.transportation;

import android.widget.ArrayAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BusNumberList {
    String key="spIogpzyHT653IuGRUeM7ATfWrT7y25rWagxWFC%2FGX5i4Brt2D1gfv%2B%2BO8RAeQILa61XvGKz1WhM9sqK%2BH9qyw%3D%3D";

    ArrayList busRouteNm = new ArrayList<>();
    ArrayList busRouteId = new ArrayList<>();

    ArrayList getBusNumberList(String busnumber) {
        System.out.println("get Station 들어옴");
        BusNumberManager(busnumber);
        return busRouteNm;
    }

    ArrayList getbusRouteId() {
        return busRouteId;
    }

    ArrayList getbusRouteNm() {
        return busRouteNm;
    }

    void BusNumberManager(String tempbusnumber) {
        System.out.println("BusNumberManager 들어옴");
        String busnumber = URLEncoder.encode(tempbusnumber);

        String queryUrl="http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList" + "?ServiceKey="
                + key + "&strSrch=" + busnumber;

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
                        if(tag.equals("busRouteNm")) {
                            System.out.println("tag station 들어옴");
                            xpp.next();
                            busRouteNm.add(xpp.getText());
                        }
                        else if(tag.equals("busRouteId")) {
                            xpp.next();
                            busRouteId.add(xpp.getText());
                        }
                        break;
                }
                eventType=xpp.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
