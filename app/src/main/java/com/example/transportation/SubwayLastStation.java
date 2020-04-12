package com.example.transportation;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.net.URLEncoder;

public class SubwayLastStation {

    String key = "75d9gomb8g7ZXJw1hygKdahNqhc2rFM5mOXiwu2pwdeyPUGM%2FCLBw%2B8HPDvX6Hd9KHOSxLpLk1J3VwfiDWa%2FFA%3D%3D";
    //ArrayList LaststaList
    //String[] subwayName = {"SUB100", "SUB160", "SUB1175", "SUB1187", "SUB1416", "SUB264", "SUB253", "SUB310", "SUB352", "SUB456", "SUB409",
    //        "SUB510", "SUB575", "SUB553", "SUB610", "SUB613", "SUB647", "SUB709", "SUB759", "SUB810", "SUB826", "SUB901", "SUB1629", "SUB1610", "SUB1299",
    //        "SUB1806", "SUB1830", "SUB4001", "SUB4013", "SUB1545", "SUB11121", "SUB11134", "SUB1910", "SUB1922"};
    //String[] dailyCode = {"01", "02", "03"};
   // String[] UDCode = {"U", "D"};

    ArrayList arrTime = new ArrayList<>();
    ArrayList depTime = new ArrayList<>();

    int count;

    ArrayList getArrTime(){
        return arrTime;
    }

    ArrayList getdepTime(){
        return depTime;
    }

    void LastArriveManager(String Name, String daily, String UD){

                    String queryUrl = "http://openapi.tago.go.kr/openapi/service/SubwayInfoService/getSubwaySttnAcctoSchdulList?serviceKey=" + key
                            + "&subwayStationId=" + Name + "&dailyTypeCode=" + daily + "&upDownTypeCode=" + UD + "&numOfRows=400";
                    System.out.println(Name + "," + daily + "," + UD + ",");
                    System.out.println(queryUrl);
                    boolean check = false;
                    while(!check) {
                        try {
                            URL url = new URL(queryUrl);
                            InputStream is = url.openStream();
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            factory.setNamespaceAware(true);
                            XmlPullParser xpp = factory.newPullParser();
                            xpp.setInput(new InputStreamReader(is, "UTF-8"));
                            String tag;
                            int eventType = xpp.getEventType();
                            count++;
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        tag = xpp.getName();
                                        if (tag.equals("arrTime")) {
                                            xpp.next();
                                            arrTime.add(xpp.getText());
                                            check = true;
                                        } else if (tag.equals("depTime")) {
                                            xpp.next();
                                            depTime.add(xpp.getText());
                                        }
                                        break;
                                }
                                eventType = xpp.next();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //System.out.println(check);
                    }

        System.out.println("size=====>" + count);
        System.out.println(arrTime.size());
        System.out.println(depTime.size());

//총 204개
//        for(int i = 0; i< subwayStationId.size()-1; i++) {
  //          System.out.println(subwayStationId.get(i).toString() + " ");
 //           System.out.println(dailyTypeCode.get(i).toString() + " ");
   //         System.out.println(upDownTypeCode.get(i).toString() + " ");
   //         System.out.println(arrTime.get(i).toString() + " ");
   //         System.out.println(depTime.get(i).toString() + " ");
   //         System.out.println("\n---------------------------\n");
  //      }

}

}
