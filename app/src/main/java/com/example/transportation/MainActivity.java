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

public class MainActivity extends AppCompatActivity{
    List<String> listview_items;
    ArrayAdapter<String> listview_adapter;

    InputMethodManager imm;

    EditText inputbus, inputstation, inputsubway, inputsubwaystation;
    TextView inputbusarriveoutput, allbusarriveoutput, subwayarriveoutput;

    String bus;
    String buses;
    String subway;
    BusID busid = new BusID();
    BusArrive busarrive = new BusArrive();
    StationInfo stationid = new StationInfo();
    AllBusArrive allbusarrive = new AllBusArrive();
    SubwayArrive subwayarrive = new SubwayArrive();
    SubwaylineID subwaylinid = new SubwaylineID();

    String directioninfo="";

    public static String select_item = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputbus=(EditText)findViewById(R.id.inputbus);
        inputstation=(EditText)findViewById(R.id.inputstation);
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



    public void mOnClick(View v){
        hideKeyboard();
        switch(v.getId()) {
            case R.id.busbutton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String inputbusnumber = inputbus.getText().toString();
                        String inputstationname = inputstation.getText().toString();

                        String BusID = busid.getBusID(inputbusnumber);
                        String StationID = stationid.getStationID(BusID, inputstationname);
                        String Ord = stationid.getOrd();
                        String Arsid = stationid.getArsid();

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
