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

public class StationNameActivity extends AppCompatActivity {

    EditText inputstationlist;
    ListView stationlistview = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_name);

        inputstationlist = (EditText) findViewById(R.id.inputstationlist);

        ArrayAdapter busadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        stationlistview = (ListView) findViewById(R.id.stationlistview);
        stationlistview.setAdapter(busadapter);

        busadapter.add("Adam Smith");
        busadapter.add("Bryan Adams");
        busadapter.add("Chris Martin");
        busadapter.add("Daniel Craig");
        busadapter.add("Eric Clapton");
        busadapter.add("Frank Sinatra");
        busadapter.add("Gary Moore");
        busadapter.add("Helloween");
        busadapter.add("Ian Hunter");
        busadapter.add("Jennifer Lopez");


        inputstationlist.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                // TODO : item filtering
                String filterText = edit.toString();
                if (filterText.length() > 0) {
                    stationlistview.setFilterText(filterText);
                } else {
                    stationlistview.clearTextFilter();
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
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result",clickedname);
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
    }
}
