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
    boolean check1, check2;

    ArrayList arvlMsg2;
    ArrayList subwayId;
    ArrayList updnLine;
    ArrayList outputarray;

    int arrmsg1toint, arrmsg2toint;

    void SubwayArriveManager(String inputname, String inputline, String inputdirection) {
        check1 = false;
        check2 = false;

        boolean check = false;

        arvlMsg2 = new ArrayList<>();
        subwayId = new ArrayList<>();
        updnLine = new ArrayList<>();
        outputarray = new ArrayList<>();


        System.out.println("outputarray 크기" + outputarray.size());
        while(outputarray.size() == 0){

            String name = URLEncoder.encode(inputname);

            String queryUrl = "http://swopenapi.seoul.go.kr/api/subway/" + key +
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
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();
                            if (tag.equals("subwayId")) {
                                xpp.next();
                                subwayId.add(xpp.getText());
                            } else if (tag.equals("updnLine")) {
                                xpp.next();
                                updnLine.add(xpp.getText());
                            } else if (tag.equals("arvlMsg2")) {
                                xpp.next();
                                arvlMsg2.add(xpp.getText());
                            }
                            break;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            for (int i = 0; i < arvlMsg2.size(); i++) {
                if ((subwayId.get(i).toString()).equals(inputline)) {
                    if ((updnLine.get(i).toString()).equals(inputdirection)) {
                        check = true;
                        outputarray.add(arvlMsg2.get(i));
                    }
                }
            }
            System.out.println("outputarray 크기" + outputarray.size());
            System.out.println("outputarray=" + outputarray);
        }

        if(!check) {
            output = "방향을 잘못 입력하였습니다. 확인 후 다시 시도하세요.";
        }
        else {
            if(outputarray.size() == 1) {
                if(isStringDouble((outputarray.get(0).toString()).substring(0,1))) {
                    //숫자이면
                    arrmsg1toint = setTime(outputarray.get(0).toString());
                    System.out.println("arrmsg1toint" + arrmsg1toint);
                    check1 = true;
                }
                else {
                    output = outputarray.get(0).toString();
                }
            }
            else {
                if(isStringDouble((outputarray.get(0).toString()).substring(0,1))) {
                    //숫자이면
                    arrmsg1toint = setTime(outputarray.get(0).toString());
                    System.out.println("arrmsg1toint" + arrmsg1toint);
                    check1 = true;
                }
                if(isStringDouble((outputarray.get(1).toString()).substring(0,1))){
                    check2 = true;
                    arrmsg2toint = setTime(outputarray.get(1).toString());
                }
                if(!check2&&!check1){
                    output = outputarray.get(0).toString() + ", \n\t" + outputarray.get(1).toString();
                    check1 = false;
                    check2 = false;
                }
            }

        }
    }

    String getarrmsg1string() {
        return outputarray.get(0).toString();
    }
    String getarrmsg2string() {
        return outputarray.get(1).toString();
    }

    ArrayList getoutputarray() {
        return outputarray;
    }

    String getoutput() {
        return output;
    }

    int getTime1() {
        return arrmsg1toint;
    }

    int getTime2() {
        return arrmsg2toint;
    }


    int setTime(String arrmsg) {
        String minute, second;
        System.out.println("arrmsg=" + arrmsg);

        if(arrmsg.contains("분")) {
            minute =arrmsg.substring(0,arrmsg.lastIndexOf("분"));
        }
        else {
            minute = "0";
        }

        System.out.println("minute= " + minute);

        if(arrmsg.contains("초")) {
            if(arrmsg.contains("분")) {
                second =arrmsg.substring(arrmsg.lastIndexOf("분")+2,arrmsg.lastIndexOf("초"));
            }
            else {
                second =arrmsg.substring(0,arrmsg.lastIndexOf("초"));
            }
        }

        else {
            second = "0";
        }
        int intminute = Integer.parseInt(minute);
        int intsecond = Integer.parseInt(second);
        System.out.println("arrmsg " + arrmsg);
        System.out.println("time은 " + intminute);
        System.out.println("second는 " + intsecond);
        System.out.println("int니?" + (intminute*60 + intsecond));
        return intminute*60 + intsecond;
    }

    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    boolean getcheck1(){
        System.out.println("check1"+check1);
        return check1;
    }
    boolean getcheck2(){
        return check2;
    }
}
