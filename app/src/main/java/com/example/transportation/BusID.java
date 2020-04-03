package com.example.transportation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BusID {
    String key="spIogpzyHT653IuGRUeM7ATfWrT7y25rWagxWFC%2FGX5i4Brt2D1gfv%2B%2BO8RAeQILa61XvGKz1WhM9sqK%2BH9qyw%3D%3D";
    String rightbusnumber = "버스 번호를 잘못 입력하였습니다.";

    String getBusID(String inputbusnumber){
        System.out.println("in bus id1" + inputbusnumber);
        BusIDManager(inputbusnumber);
        System.out.println("in bus id2" + inputbusnumber);
        return rightbusnumber;
    }

    void BusIDManager(String inputbusnumber) {
        StringBuffer buffer = new StringBuffer();
        ArrayList busRouteId = new ArrayList<>();
        ArrayList busRouteNm = new ArrayList<>();

        String busnumber = URLEncoder.encode(inputbusnumber);
        System.out.println("busnumber = " + busnumber);
        String queryUrl="http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList" + "?ServiceKey="
                + key + "&strSrch=" + busnumber;
        System.out.println("queryUrl = " + queryUrl);
        try {
            URL url = new URL(queryUrl);//
            System.out.println("url = " + url);
            InputStream is = url.openStream();
            //System.out.println("is = " + is);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            String tv=buffer.toString();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));
            System.out.println("xpp = " + xpp);
            String tag;

            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch(eventType) {
                    case XmlPullParser.START_TAG://2
                        tag = xpp.getName();
                        System.out.println("tag = " + tag);
                        if(tag.equals("itemList"))
                            System.out.println("if tag = " + tag);
                        else if(tag.equals("busRouteId")) {
                            xpp.next();
                            busRouteId.add(xpp.getText());
                        }
                        else if(tag.equals("busRouteNm")){
                            xpp.next();
                            busRouteNm.add(xpp.getText());
                        }
                        break;
                }
                eventType=xpp.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < busRouteNm.size();i++) {
            if((busRouteNm.get(i).toString()).equals(inputbusnumber)) {
                rightbusnumber = busRouteId.get(i).toString();
            }
        }
    }
}
