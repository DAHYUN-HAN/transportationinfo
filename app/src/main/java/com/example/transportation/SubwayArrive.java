package com.example.transportation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SubwayArrive {
    String key="534d6b4767676b73313239724152544e";
    String output;

    void SubwayArriveManager(String inputname, String inputline, String inputdirection, String inputstationline){
        ArrayList arvlMsg2 = new ArrayList<>();
        ArrayList subwayId = new ArrayList<>();
        ArrayList updnLine = new ArrayList<>();
        ArrayList outputarray = new ArrayList<>();

        boolean check = false;

        String name = URLEncoder.encode(inputname);

        String queryUrl="http://swopenapi.seoul.go.kr/api/subway/" + key +
                        "/xml/realtimeStationArrival/0/20/" + name;

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
                        if(tag.equals("subwayId")) {
                            xpp.next();
                            subwayId.add(xpp.getText());
                        }
                        else if(tag.equals("updnLine")) {
                            xpp.next();
                            updnLine.add(xpp.getText());
                        }
                        else if(tag.equals("arvlMsg2")) {
                            xpp.next();
                            arvlMsg2.add(xpp.getText());
                        }
                        break;
                }
                eventType=xpp.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < arvlMsg2.size();i++) {
            if((subwayId.get(i).toString()).equals(inputline)) {
                if((updnLine.get(i).toString()).equals(inputdirection)) {
                    check = true;
                    outputarray.add(arvlMsg2.get(i));
                }
            }
        }

        if(!check) {
            output = "방향을 잘못 입력하였습니다. 확인 후 다시 시도하세요.";
        }
        else {
            output = inputname + "역 " + inputstationline + " ";
            for (int i = 0; i < outputarray.size(); i++) {
                output += outputarray.get(i).toString() + ", \n\t";
            }
            output = output.substring(0, output.length()-4);
            output += "도착예정";
        }

    }

    String getSubwayArrive(String inputname, String inputline, String inputdirection, String inputstationline){
        SubwayArriveManager(inputname, inputline, inputdirection, inputstationline);
        return output;
    }
}
