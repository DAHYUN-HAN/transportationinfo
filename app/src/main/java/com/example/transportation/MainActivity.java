package com.example.transportation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import android.content.Intent;

public class MainActivity extends AppCompatActivity{

    InputMethodManager imm;

    EditText inputbus, inputsubway, inputsubwaystation;
    TextView inputbusarriveoutput, allbusarriveoutput, subwayarriveoutput, inputstation;

    String bus;
    String buses;
    String subway;
    BusID busid = new BusID();
    BusArrive busarrive = new BusArrive();
    AllBusArrive allbusarrive = new AllBusArrive();
    SubwayArrive subwayarrive = new SubwayArrive();
    SubwaylineID subwaylinid = new SubwaylineID();

    String directioninfo="";

    String inputbusnumber, BusID;

    String inputstationname;

    String StationID, Ord, Arsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputbus=(EditText)findViewById(R.id.inputbus);
        inputstation=(TextView)findViewById(R.id.inputstation);
        inputbusarriveoutput=(TextView)findViewById(R.id.inputbusarriveoutput);
        allbusarriveoutput=(TextView)findViewById(R.id.allbusarriveoutput);

        inputsubway=(EditText)findViewById(R.id.inputsubway);
        inputsubwaystation=(EditText)findViewById(R.id.inputsubwaystation);
        subwayarriveoutput=(TextView)findViewById(R.id.subwayarriveoutput);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //final String[] direction = {"","상행", "하행", "외선", "내선"};
        final ArrayList direction = new ArrayList<>();
        direction.add("방향");
        direction.add("상행");
        direction.add("하행");
        direction.add("외선");
        direction.add("내선");
        Spinner spinner = (Spinner) findViewById(R.id.directionspinner);

        ArrayAdapter<String> adapter; // ArrayAdapter <String>형의 변수 선언
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, direction);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                directioninfo = direction.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
// MainActivity 에서 요청할 때 보낸 요청 코드 (1000)
                case 1000:
                    inputstationname = data.getStringExtra("stationname");
                    inputstation.setText(inputstationname);
                    StationID = data.getStringExtra("stationid");
                    Ord = data.getStringExtra("stationord");
                    Arsid = data.getStringExtra("stationarsid");
                    break;
            }
        }
    }

    public void mOnClick(View v){
        hideKeyboard();
        switch(v.getId()) {
            case R.id.inputstation:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        inputbusnumber = inputbus.getText().toString();
                        BusID = busid.getBusID(inputbusnumber);

                        Intent intent = new Intent(getApplicationContext(), StationNameActivity.class);
                        intent.putExtra("inputbusid",BusID);
                        startActivityForResult(intent, 1000);
                    }
                }).start();
                break;

            case R.id.busbutton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        bus = busarrive.getBusArrive(StationID, BusID, Ord,inputbusnumber);

                        ArrayList output = new ArrayList<>();
                        output = allbusarrive.getAllBusArrive(Arsid);

                        buses="";
                        for(int i = 0; i < output.size();i++){
                            buses += output.get(i).toString();
                            buses += "\n\n";
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                inputbusarriveoutput.setText(bus);
                                allbusarriveoutput.setText(buses);
                            }
                        });
                    }
                }).start();
                break;
            case R.id.subwaybutton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("subway 버튼 눌림");
                        String inputstationline = inputsubway.getText().toString();
                        String inputstationname = inputsubwaystation.getText().toString();
                        String stationline = subwaylinid.getSubwaylineID(inputstationline);
                        subway = subwayarrive.getSubwayArrive(inputstationname, stationline, directioninfo, inputstationline);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                subwayarriveoutput.setText(subway);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    private void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(inputbus.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(inputstation.getWindowToken(), 0);
    }

}
