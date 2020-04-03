package com.example.transportation;

import android.widget.ArrayAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SubwayNameList {
    String key="534d6b4767676b73313239724152544e";

    ArrayList subwayId = new ArrayList<>();
    ArrayList statnNm = new ArrayList<>();

    ArrayList subwayNameList = new ArrayList<>();

    ArrayList getSubwayList(String stationline) {
        System.out.println("getSubwayList 들어옴");
        SubwayListManager(stationline);
        return statnNm;
    }

    ArrayList getSubwayID() {
        return subwayId;
    }

    ArrayList getSubwayName() {
        return statnNm;
    }

    void SubwayListManager(String tempstationline) {
        System.out.println("SubwayListManager 들어옴");
        String stationline = URLEncoder.encode(tempstationline);

        String queryUrl="http://swopenapi.seoul.go.kr/api/subway/" + key +
                "/xml/stationByLine/0/100/" + stationline;

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
                        if(tag.equals("subwayId")) {
                            System.out.println("subwayId 들어옴");
                            xpp.next();
                            subwayId.add(xpp.getText());
                        }
                        else if(tag.equals("statnNm")) {
                            xpp.next();
                            statnNm.add(xpp.getText());
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
