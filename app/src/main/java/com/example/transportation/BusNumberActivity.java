package com.example.transportation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;
import android.os.Handler;
import android.os.Message;


public class BusNumberActivity extends AppCompatActivity {

    EditText inputbusnumberlist;
    ListView busnumberlistview = null;
    BusNumberList busnumberlist = new BusNumberList();
    ArrayList busList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_number);

        inputbusnumberlist = (EditText) findViewById(R.id.inputbusnumberlist);

        final ArrayAdapter busnumberadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        final ArrayAdapter newbusnumberadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        busnumberlistview = (ListView) findViewById(R.id.busnumberlistview);
        busnumberlistview.setAdapter(busnumberadapter);

        Intent intent = getIntent(); /*데이터 수신*/

        final String busnumber = intent.getExtras().getString("inputbusnumber");
        System.out.println(busnumber);
        final Handler handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                for(int i = 0; i < busList.size(); i++) {
                    String x = busList.get(i).toString();
                    String y;
                    busnumberadapter.add(x);
                }
            }
        };
        //stationlistview.setAdapter(null);
        //stationlistview.setAdapter(busadapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                busList = busnumberlist.getBusNumberList(busnumber);

                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);

                inputbusnumberlist.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable edit) {
                        // TODO : item filtering
                        String filterText = edit.toString();
                        System.out.println("filter = " + filterText);
                        System.out.println("filterText.length()" + filterText.length());
                        if (filterText != null && filterText.length() > 0) {
                            //ArrayList<String> filterItems = new ArrayList<String>();
                            newbusnumberadapter.clear();
                            for(int i = 0;i < busList.size(); i++)
                            {
                                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                                String x = busList.get(i).toString();

                                if (x.toLowerCase().contains(filterText))
                                {
                                    // 검색된 데이터를 리스트에 추가한다.
                                    newbusnumberadapter.add(x);
                                }
                            }
                            busnumberlistview.setAdapter(newbusnumberadapter);

                        } else {
                            //stationlistview.setAdapter(null);
                            busnumberlistview.setAdapter(busnumberadapter);
                        }
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                busnumberlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 클릭한 아이템 이름 가져옴
                        ListView listview = (ListView) parent;
                        String clickedname = (String) listview.getItemAtPosition(position);
                        // 토스트 메세지로 표시
                        Toast.makeText(BusNumberActivity.this,
                                clickedname + "을(를) 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
                        for(int i = 0; i < busList.size(); i++) {
                            if ((busnumberlist.getbusRouteNm().get(i).toString()).equals(clickedname)) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("busnumber", clickedname);
                                resultIntent.putExtra("busid",busnumberlist.getbusRouteId().get(i).toString());
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
