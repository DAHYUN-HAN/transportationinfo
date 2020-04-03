package com.example.transportation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;
import android.os.Handler;
import android.os.Message;


public class StationNameActivity extends AppCompatActivity {

    EditText inputstationlist;
    ListView stationlistview = null;
    StationNameList stationnamelist = new StationNameList();
    ArrayList stationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_name);

        inputstationlist = (EditText) findViewById(R.id.inputstationlist);

        final ArrayAdapter busadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        final ArrayAdapter newbusadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        stationlistview = (ListView) findViewById(R.id.stationlistview);
        stationlistview.setAdapter(busadapter);

        Intent intent = getIntent(); /*데이터 수신*/

        final String busid = intent.getExtras().getString("inputbusid");
        System.out.println(busid);
        final Handler handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                for(int i = 0; i < stationList.size(); i++) {
                    String x = stationList.get(i).toString();
                    busadapter.add(x);
                }
            }
        };
        //stationlistview.setAdapter(null);
        //stationlistview.setAdapter(busadapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                stationList = stationnamelist.getStationList(busid);

                System.out.println("stationList"+ stationList);
                System.out.println("size" + stationList.size());

                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);

                System.out.println("busadapter" + busadapter);

                inputstationlist.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable edit) {
                        // TODO : item filtering
                        String filterText = edit.toString();
                        System.out.println("filter = " + filterText);
                        System.out.println("filterText.length()" + filterText.length());
                        if (filterText != null && filterText.length() > 0) {
                            //ArrayList<String> filterItems = new ArrayList<String>();
                            newbusadapter.clear();
                            for(int i = 0;i < stationList.size(); i++)
                            {
                                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                                String x = stationList.get(i).toString();

                                if (x.toLowerCase().contains(filterText))
                                {
                                    // 검색된 데이터를 리스트에 추가한다.
                                    String y;
                                    if(i == 0) {
                                        y = "\n종점.\n다음역:" + stationnamelist.getStationName().get(i+1).toString();
                                    }
                                    else if(i == stationList.size()-1) {
                                        y = "\n종점.\n이전역:" + stationnamelist.getStationName().get(i-1).toString();
                                    }
                                    else {
                                        y = "\n이전역:" + stationnamelist.getStationName().get(i-1).toString() + "\n다음역:" + stationnamelist.getStationName().get(i+1).toString();
                                    }
                                    newbusadapter.add(x+y);
                                }
                            }
                            stationlistview.setAdapter(newbusadapter);

                        } else {
                            //stationlistview.setAdapter(null);
                            stationlistview.setAdapter(busadapter);
                        }
                }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                stationlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 클릭한 아이템 이름 가져옴
                        ListView listview = (ListView) parent;
                        String clickedname = (String) listview.getItemAtPosition(position);
                        // 토스트 메세지로 표시
                        Toast.makeText(StationNameActivity.this,
                                clickedname + "을(를) 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
                        String result = clickedname.substring(0,clickedname.lastIndexOf(" "));
                        System.out.println("result" + result);
                        for(int i = 0; i < stationList.size(); i++) {
                            System.out.println("result=" + result);
                            if ((stationnamelist.getStationNm().get(i).toString()).equals(result)) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("stationname", result);
                                resultIntent.putExtra("stationid",stationnamelist.getStationID().get(i).toString());
                                resultIntent.putExtra("stationord",stationnamelist.getStationOrd().get(i).toString());
                                resultIntent.putExtra("stationarsid",stationnamelist.getStationarsID().get(i).toString());
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }
                        }
                    }
                });


            }
        }).start();

    }
}
