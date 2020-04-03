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


public class SubwayNameActivity extends AppCompatActivity {

    EditText inputsubwaylist;
    ListView subwaylistview = null;
    SubwayNameList subwaynamelist = new SubwayNameList();
    ArrayList subwayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_name);

        inputsubwaylist = (EditText) findViewById(R.id.inputsubwaylist);

        final ArrayAdapter subwayadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        final ArrayAdapter newsubwayadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        subwaylistview = (ListView) findViewById(R.id.subwaylistview);
        subwaylistview.setAdapter(subwayadapter);

        Intent intent = getIntent(); /*데이터 수신*/

        final String stationline = intent.getExtras().getString("inputstationline");
        final Handler handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                for(int i = 0; i < subwayList.size(); i++) {
                    String x = subwayList.get(i).toString();
                    subwayadapter.add(x);
                }
            }
        };
        //stationlistview.setAdapter(null);
        //subwaylistview.setAdapter(subwayadapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                subwayList = subwaynamelist.getSubwayList(stationline);

                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);

                inputsubwaylist.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable edit) {
                        // TODO : item filtering
                        String filterText = edit.toString();
                        if (filterText != null && filterText.length() > 0) {
                            //ArrayList<String> filterItems = new ArrayList<String>();
                            newsubwayadapter.clear();
                            for(int i = 0;i < subwayList.size(); i++)
                            {
                                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                                String x = subwayList.get(i).toString();

                                if (x.toLowerCase().contains(filterText))
                                {
                                    // 검색된 데이터를 리스트에 추가한다.
                                    newsubwayadapter.add(x);
                                }
                            }
                            subwaylistview.setAdapter(newsubwayadapter);

                        } else {
                            //stationlistview.setAdapter(null);
                            subwaylistview.setAdapter(subwayadapter);
                        }
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                subwaylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 클릭한 아이템 이름 가져옴
                        ListView listview = (ListView) parent;
                        String clickedname = (String) listview.getItemAtPosition(position);
                        System.out.println("click" + clickedname);
                        // 토스트 메세지로 표시
                        Toast.makeText(SubwayNameActivity.this,
                                clickedname + "을(를) 클릭하셨습니다.", Toast.LENGTH_SHORT).show();
                        for(int i = 0; i < subwayList.size(); i++) {
                            if ((subwaynamelist.getSubwayName().get(i).toString()).equals(clickedname)) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("stationname", clickedname);
                                resultIntent.putExtra("subwayid",subwaynamelist.getSubwayID().get(i).toString());
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
