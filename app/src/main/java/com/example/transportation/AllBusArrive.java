package com.example.transportation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AllBusArrive {
    String key="spIogpzyHT653IuGRUeM7ATfWrT7y25rWagxWFC%2FGX5i4Brt2D1gfv%2B%2BO8RAeQILa61XvGKz1WhM9sqK%2BH9qyw%3D%3D";
    ArrayList output = new ArrayList<>();

    void AllBusArriveManager(String temparsid){
        ArrayList arrmsg1 = new ArrayList<>();
        ArrayList arrmsg2 = new ArrayList<>();
        ArrayList rtNm = new ArrayList<>();

        String arsid = URLEncoder.encode(temparsid);

        String queryUrl="http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid" + "?ServiceKey="
                + key + "&arsId=" + arsid;
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
                        else if(tag.equals("rtNm")) {
                            xpp.next();
                            rtNm.add(xpp.getText());
                        }
                        break;
                }
                eventType=xpp.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList tempoutput = new ArrayList<>();
        for(int i = 0; i < rtNm.size();i++) {
            String text = rtNm.get(i).toString() + " 버스 " + arrmsg1.get(i).toString() + ", \n\t" + arrmsg2.get(i).toString() + " 도착예정";
            tempoutput.add(text);
        }
        output = tempoutput;
    }

    ArrayList getAllBusArrive(String arsid){
        AllBusArriveManager(arsid);
        return output;
    }
}
