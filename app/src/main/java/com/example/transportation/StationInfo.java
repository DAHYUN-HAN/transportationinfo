package com.example.transportation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class StationInfo {
    String key="spIogpzyHT653IuGRUeM7ATfWrT7y25rWagxWFC%2FGX5i4Brt2D1gfv%2B%2BO8RAeQILa61XvGKz1WhM9sqK%2BH9qyw%3D%3D";
    String rightstationid = "정류장 이름을 잘못 입력하였습니다.";
    String rightord = "정보를 찾지 못하였습니다.";
    String rightarsid = "정보를 찾지 못하였습니다.";

    String getOrd() {
        return rightord;
    }

    String getArsid() {
        return rightarsid;
    }

    String getStationID(String tempbusid, String inputstationname){
        StationIDManager(tempbusid, inputstationname);
        return rightstationid;
    }

    void StationIDManager(String tempbusid, String inputstationname) {
        ArrayList station = new ArrayList<>();
        ArrayList stationNm = new ArrayList<>();
        ArrayList seq = new ArrayList<>();
        ArrayList arsId = new ArrayList<>();
        ArrayList direction = new ArrayList<>();

        String busid = URLEncoder.encode(tempbusid);

        String queryUrl="http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute" + "?ServiceKey="
                + key + "&busRouteId=" + busid;

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
                        if(tag.equals("station")) {
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
        for(int i = 0; i < station.size();i++) {
            if((stationNm.get(i).toString()).equals(inputstationname)) {
                System.out.println(inputstationname);
                rightstationid = station.get(i).toString();
                rightord = seq.get(i).toString();
                rightarsid = arsId.get(i).toString();
            }
        }
    }
}
