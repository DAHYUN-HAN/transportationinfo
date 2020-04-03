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
import android.widget.Button;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import android.content.Intent;

public class MainActivity extends AppCompatActivity{

    InputMethodManager imm;

    EditText inputbus;
    TextView inputbusarriveoutput, allbusarriveoutput, subwayarriveoutput, inputstation, inputsubwaystation;

    String bus;
    String buses;
    String subway;
    BusArrive busarrive = new BusArrive();
    AllBusArrive allbusarrive = new AllBusArrive();
    SubwayArrive subwayarrive = new SubwayArrive();
    BusID busid = new BusID();

    String directioninfo="";

    String inputbusnumber, BusID;

    String inputstationname, inputstationline, stationline, inputsubwaystationname, pickbusnumber;

    String StationID, Ord, Arsid;

    Button busbutton, subwaybutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputbus=(EditText)findViewById(R.id.inputbus);
        inputstation=(TextView)findViewById(R.id.inputstation);
        inputbusarriveoutput=(TextView)findViewById(R.id.inputbusarriveoutput);
        allbusarriveoutput=(TextView)findViewById(R.id.allbusarriveoutput);

        inputsubwaystation=(TextView)findViewById(R.id.inputsubwaystation);
        subwayarriveoutput=(TextView)findViewById(R.id.subwayarriveoutput);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        final ArrayList direction = new ArrayList<>();
        direction.add("방향");

        final ArrayList subwayline = new ArrayList<>();
        subwayline.add("지하철 호선 선택");
        subwayline.add("1호선");
        subwayline.add("2호선");
        subwayline.add("3호선");
        subwayline.add("4호선");
        subwayline.add("5호선");
        subwayline.add("6호선");
        subwayline.add("7호선");
        subwayline.add("8호선");
        subwayline.add("9호선");
        subwayline.add("경의 중앙");
        subwayline.add("공항철도");
        subwayline.add("경춘선");
        subwayline.add("수인선");
        subwayline.add("분당선");
        subwayline.add("신분당선");

        Spinner linespinner = (Spinner) findViewById(R.id.subwayspinner);

        ArrayAdapter<String> lineadapter; // ArrayAdapter <String>형의 변수 선언
        lineadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subwayline);
        linespinner.setAdapter(lineadapter);

        linespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                inputstationline = subwayline.get(position).toString();

                if (inputstationline.equals("2호선")) {
                    direction.clear();
                    direction.add("방향");
                    direction.add("외선");
                    direction.add("내선");
                }
                else if(inputstationline != "지하철 호선 선택"){
                    direction.clear();
                    direction.add("방향");
                    direction.add("상행");
                    direction.add("하행");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner directionspinner = (Spinner) findViewById(R.id.directionspinner);

        ArrayAdapter<String> directionadapter; // ArrayAdapter <String>형의 변수 선언
        directionadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, direction);
        directionspinner.setAdapter(directionadapter);


        directionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                directioninfo = direction.get(position).toString();
                if(directioninfo != "방향") {
                    inputsubwaystation.setEnabled(true);
                }
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

                case 2000:
                    inputsubwaystationname = data.getStringExtra("stationname");
                    inputsubwaystation.setText(inputsubwaystationname);
                    stationline = data.getStringExtra("subwayid");
                    subwaybutton = (Button) findViewById(R.id.subwaybutton);
                    subwaybutton.setEnabled(true);
                    break;

                case 3000:
                    pickbusnumber = data.getStringExtra("busnumber");
                    inputbus.setText(pickbusnumber);
                    BusID = data.getStringExtra("busid");
                    busbutton = (Button) findViewById(R.id.busbutton);
                    busbutton.setEnabled(true);
                    break;
            }
        }
    }

    public void mOnClick(View v){
        hideKeyboard();
        switch(v.getId()) {
            case R.id.busnumbersearchbutton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        inputbusnumber = inputbus.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), BusNumberActivity.class);
                        //String tempbusnumber = "303";
                        intent.putExtra("inputbusnumber",inputbusnumber);
                        startActivityForResult(intent, 3000);
                    }
                }).start();
                break;


            case R.id.inputsubwaystation:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), SubwayNameActivity.class);
                        intent.putExtra("inputstationline",inputstationline);
                        startActivityForResult(intent, 2000);
                    }
                }).start();
                break;

            case R.id.inputstation:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //inputbusnumber = inputbus.getText().toString();
                        //BusID = busid.getBusID(inputbusnumber);
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

                        subway = subwayarrive.getSubwayArrive(inputsubwaystationname, stationline, directioninfo, inputstationline);

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
