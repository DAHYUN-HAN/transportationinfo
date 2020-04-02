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

    void BusArriveManager(String tempstationid, String tempbustid, String tempord, String inputbusnumber){
        ArrayList arrmsg1 = new ArrayList<>();
        ArrayList arrmsg2 = new ArrayList<>();

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
        output = inputbusnumber + "버스 " + arrmsg1.get(0).toString() + ", \n\t" + arrmsg2.get(0).toString() + " 도착예정";
    }

    String getBusArrive(String tempstationid, String tempbustid, String tempord, String inputbusnumber){
        BusArriveManager(tempstationid, tempbustid, tempord, inputbusnumber);
        return output;
    }
}
