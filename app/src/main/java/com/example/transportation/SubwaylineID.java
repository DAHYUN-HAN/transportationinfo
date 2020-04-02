package com.example.transportation;

public class SubwaylineID {
    String subwaylineid;

    void SubwaylineIDManager(String inputstationline){
        String[][] directions = {{"1001","1호선"},
                                {"1002","2호선"},
                                {"1003","3호선"},
                                {"1004","4호선"},
                                {"1005","5호선"},
                                {"1006","6호선"},
                                {"1007","7호선"},
                                {"1008","8호선"},
                                {"1009","9호선"},
                                {"1063","경의중앙"},
                                {"1065","공항철도"},
                                {"1067","경춘선"},
                                {"1071","수인선"},
                                {"1075","분당선"},
                                {"1077","신분당선"}};
        System.out.println("size = " + directions.length);
        for(int i = 0; i < directions.length; i++) {
            if((directions[i][1]).equals(inputstationline)) {
                subwaylineid = directions[i][0];
            }
        }
    }

    String getSubwaylineID(String inputstationline){
        SubwaylineIDManager(inputstationline);
        System.out.println("subid = " + subwaylineid);
        return subwaylineid;
    }
}
