package com.example.transportation;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import android.widget.Button;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import android.content.Intent;

import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity{

    InputMethodManager imm;

    EditText inputbus;
    EditText stationid, daily, updown;
    TextView inputbusarriveoutput, allbusarriveoutput, subwayarriveoutput, inputstation, inputsubwaystation, subwaylastarrive;

    String lastresult="", result;

    String bus;
    String buses;
    String subway;
    BusArrive busarrive = new BusArrive();
    AllBusArrive allbusarrive = new AllBusArrive();
    SubwayArrive subwayarrive = new SubwayArrive();

    SubwayLastStation subwaylaststation = new SubwayLastStation();

    String directioninfo="";

    String inputbusnumber, BusID;

    String inputstationname, inputstationline, stationline, inputsubwaystationname, pickbusnumber;

    String StationID, Ord, Arsid, subwaystationname;

    String subwaylast;

    Button busbutton, subwaybutton;

    int bustime1, bustime2, subwaytime1, subwaytime2, x;

    private LastStationDBOpenHelper laststationdbopenhelper;

    String sort = "subwaystationid";
    int size;
    String arrtime, deptime;

    ArrayList arr;
    ArrayList dep;

    String id;
    String dailytype;
    String updowntype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stationid=(EditText)findViewById(R.id.stationid);
        daily=(EditText)findViewById(R.id.daily);
        updown=(EditText)findViewById(R.id.updown);

        inputbus=(EditText)findViewById(R.id.inputbus);
        inputstation=(TextView)findViewById(R.id.inputstation);
        inputbusarriveoutput=(TextView)findViewById(R.id.inputbusarriveoutput);
        allbusarriveoutput=(TextView)findViewById(R.id.allbusarriveoutput);

        inputsubwaystation=(TextView)findViewById(R.id.inputsubwaystation);
        subwayarriveoutput=(TextView)findViewById(R.id.subwayarriveoutput);

        subwaylastarrive=(TextView)findViewById(R.id.subwaylastarrive);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        laststationdbopenhelper = new LastStationDBOpenHelper(this);
        laststationdbopenhelper.open();
        laststationdbopenhelper.create();

        inputbus.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                Button bt = (Button)findViewById(R.id.busnumbersearchbutton);
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    bt.performClick();
                    return true;
                }
                return false;
            }
        });

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

    public String getDatabase(String sort){
        System.out.println("getDatabase들어옴");
        Cursor iCursor = laststationdbopenhelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
 //       arrayData.clear();
//        arrayIndex.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempSubwayStationId = iCursor.getString(iCursor.getColumnIndex("subwaystationid"));
//            tempID = setTextLength(tempID,10);
            String tempDaily = iCursor.getString(iCursor.getColumnIndex("dailytype"));
//            tempName = setTextLength(tempName,10);
            String tempUpDown = iCursor.getString(iCursor.getColumnIndex("updowntype"));
//            tempAge = setTextLength(tempAge,10);
            String tempArrtime = iCursor.getString(iCursor.getColumnIndex("arrtime"));
//            tempGender = setTextLength(tempGender,10);
            String tempDeptime = iCursor.getString(iCursor.getColumnIndex("deptime"));

            String result = tempIndex +", " + tempSubwayStationId + ", " + tempDaily + ", " + tempUpDown + ", " + tempArrtime + ", " + tempDeptime;
            lastresult += result;
            lastresult += "\n";

//            String Result = tempID + tempName + tempAge + tempGender;
//            arrayData.add(result);
//            arrayIndex.add(tempIndex);
        }
//        arrayAdapter.clear();
//        arrayAdapter.addAll(arrayData);
//        arrayAdapter.notifyDataSetChanged();
        return lastresult;
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
                    subwaystationname = inputsubwaystationname;
                    stationline = data.getStringExtra("subwayid");
                    subwaybutton = (Button) findViewById(R.id.subwaybutton);
                    subwaybutton.setEnabled(true);
                    break;

                case 3000:
                    pickbusnumber = data.getStringExtra("busnumber");
                    inputbus.setText(pickbusnumber);
                    inputbusnumber = pickbusnumber;
                    BusID = data.getStringExtra("busid");
                    busbutton = (Button) findViewById(R.id.busbutton);
                    busbutton.setEnabled(true);
                    inputstation.setEnabled(true);
                    break;
            }
        }
    }
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            bustime1--;
            bustime2--;
            x++;
            bus = inputbusnumber + "버스" + bustime1 / 60 + "분" + bustime1 % 60 + "초후, \n\t" + bustime2 / 60 + "분" + bustime2 % 60 + "초후 도착예정";
            System.out.println(x);
            if(bustime1 < 20 || bustime2 < 60 || x > 10) {
                busbutton.performClick();
                this.removeMessages(0);
            }

            inputbusarriveoutput.setText(bus);

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            handler.sendEmptyMessageDelayed(0,1000);
        }
    };

    final Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            bustime2--;
            x++;
            bus = inputbusnumber + "버스 " + busarrive.getarrmsg1string() +", \n\t" + bustime2 / 60 + "분" + bustime2 % 60 + "초후 도착예정";
            System.out.println(x);
            if(bustime2 < 20 || x > 10) {
                busbutton.performClick();
                this.removeMessages(0);
            }

            inputbusarriveoutput.setText(bus);

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            handler2.sendEmptyMessageDelayed(0,1000);
        }
    };

    final Handler handler3 = new Handler() {
        public void handleMessage(Message msg) {
            bustime1--;
            x++;
            bus = inputbusnumber + "버스" + bustime1 / 60 + "분" + bustime1 % 60 + "초후, \n\t" + busarrive.getarrmsg2string();
            System.out.println(x);
            if(bustime1 < 20 || x > 10) {
                busbutton.performClick();
                this.removeMessages(0);
            }

            inputbusarriveoutput.setText(bus);

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            handler3.sendEmptyMessageDelayed(0,1000);
        }
    };

    final Handler subwayhandler = new Handler() {
        public void handleMessage(Message msg) {
            subwaytime1--;
            subwaytime2--;
            x++;
            subway = subwaystationname + "역 " + inputstationline + " " + subwaytime1 / 60 + "분" + subwaytime1 % 60 + "초후, \n\t" + subwaytime2 / 60 + "분" + subwaytime2 % 60 + "초후 도착예정";
            System.out.println(x);
            if(subwaytime1 < 20 || subwaytime2 < 60 || x > 10) {
                subwaybutton.performClick();
                this.removeMessages(0);
            }

            subwayarriveoutput.setText(subway);

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            subwayhandler.sendEmptyMessageDelayed(0,1000);
        }
    };

    final Handler subwayhandler2 = new Handler() {
        public void handleMessage(Message msg) {
            subwaytime2--;
            x++;
            subway = subwaystationname + "역 " + inputstationline + " " + subwayarrive.getarrmsg1string() +", \n\t" + subwaytime2 / 60 + "분" + subwaytime2 % 60 + "초후 도착예정";
            System.out.println(x);
            if(subwaytime2 < 20 || x > 10) {
                subwaybutton.performClick();
                this.removeMessages(0);
            }

            subwayarriveoutput.setText(subway);

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            subwayhandler2.sendEmptyMessageDelayed(0,1000);
        }
    };

    final Handler subwayhandler3 = new Handler() {
        public void handleMessage(Message msg) {
            subwaytime1--;
            x++;
            subway = subwaystationname + "역 " + inputstationline + " " + subwaytime1 / 60 + "분" + subwaytime1 % 60 + "초후, \n\t" + subwayarrive.getarrmsg2string();
            subwayarriveoutput.setText(subway);
            System.out.println(x);
            if(subwaytime1 < 20 || x > 10) {
                subwaybutton.performClick();
                this.removeMessages(0);
            }

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            subwayhandler3.sendEmptyMessageDelayed(0,1000);
        }
    };

    final Handler subwayhandler4 = new Handler() {
        public void handleMessage(Message msg) {
            subwaytime1--;
            x++;
            subway = subwaystationname + "역 " + inputstationline + " " + subwaytime1 / 60 + "분" + subwaytime1 % 60 + "초후 도착예정";
            subwayarriveoutput.setText(subway);
            System.out.println(x);
            if(subwaytime1 < 20 || x > 10) {
                subwaybutton.performClick();
                this.removeMessages(0);
            }

            // 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
            subwayhandler4.sendEmptyMessageDelayed(0,1000);
        }
    };

    final Handler lastsubwayhandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            laststationdbopenhelper.open();
            for(int i = 0; i < size; i++) {
                arrtime = arr.get(i).toString();
                deptime = dep.get(i).toString();
                laststationdbopenhelper.insertColumn(id, dailytype, updowntype, arrtime, deptime);
            }
            //laststationdbopenhelper.insertColumn(id, dailytype, updowntype, arr.get(0).toString(), dep.get(0).toString());
            result = getDatabase(sort);
            System.out.println(result);
        }
    };

    public void mOnClick(View v){
        hideKeyboard();
        switch(v.getId()) {
            case R.id.subwaylastbutton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        id = stationid.getText().toString();
                        dailytype= daily.getText().toString();
                        updowntype = updown.getText().toString();
                        subwaylaststation.LastArriveManager(id, dailytype, updowntype);

                        arr = new ArrayList<>();
                        dep = new ArrayList<>();

                        arr = subwaylaststation.getArrTime();
                        dep = subwaylaststation.getdepTime();

                        System.out.println(arr);
                        System.out.println(dep);

                        size=arr.size();
                        if(arr.size()>dep.size()) {
                            size = dep.size();
                        }
                        else {
                            size = arr.size();
                        }

                        Message msg = lastsubwayhandler.obtainMessage();
                        lastsubwayhandler.sendMessage(msg);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                subwaylastarrive.setText(result);
                            }
                        });
                    }
                }).start();
                break;


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
                        x=0;
                        busarrive.getBusArrive(StationID, BusID, Ord);
                        boolean check = busarrive.getcheck1() && busarrive.getcheck2();
                        if(check) {
                            //숫자
                            bustime1 = busarrive.getTime1();
                            bustime2 = busarrive.getTime2();


                            handler.removeMessages(0);
                            handler2.removeMessages(0);
                            handler3.removeMessages(0);

                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
                        }
                        else if(busarrive.getcheck1()){
                            System.out.println("busarrive.getcheck1()"+busarrive.getTime1());
                            bustime1 = busarrive.getTime1();


                            handler.removeMessages(0);
                            handler2.removeMessages(0);
                            handler3.removeMessages(0);

                            Message msg = handler3.obtainMessage();
                            handler3.sendMessage(msg);
                        }
                        else if(busarrive.getcheck2()){
                            bustime2 = busarrive.getTime2();


                            handler.removeMessages(0);
                            handler2.removeMessages(0);
                            handler3.removeMessages(0);

                            Message msg = handler2.obtainMessage();
                            handler2.sendMessage(msg);
                        }
                        else {


                            handler.removeMessages(0);
                            handler2.removeMessages(0);
                            handler3.removeMessages(0);

                            bus = inputbusnumber + "버스 "+busarrive.getoutput();
                        }


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
                        x=0;
                        subwayhandler.removeMessages(0);
                        subwayhandler2.removeMessages(0);
                        subwayhandler3.removeMessages(0);
                        subwayhandler3.removeMessages(0);

                        ArrayList subwayoutput = new ArrayList<>();
                        subwayarrive.SubwayArriveManager(inputsubwaystationname, stationline, directioninfo);

                        subwayoutput = subwayarrive.getoutputarray();
                        System.out.println("subwayoutput은" + subwayoutput);

                        if(subwayoutput.size() == 1) {
                            if(subwayarrive.getcheck1()) {
                                subwaytime1 = subwayarrive.getTime1();

                                subwayhandler.removeMessages(0);
                                subwayhandler2.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                Message msg = subwayhandler4.obtainMessage();
                                subwayhandler4.sendMessage(msg);
                            }
                            else {
                                subway = subwaystationname + "역 " + inputstationline + " " + subwayarrive.getoutput();
                            }
                        }
                        else{
                            boolean check2 = subwayarrive.getcheck1() && subwayarrive.getcheck2();

                            if(check2) {
                                //숫자
                                subwaytime1 = subwayarrive.getTime1();
                                subwaytime2 = subwayarrive.getTime2();

                                subwayhandler.removeMessages(0);
                                subwayhandler2.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                Message msg = subwayhandler.obtainMessage();
                                subwayhandler.sendMessage(msg);
                            }
                            else if(subwayarrive.getcheck1()){
                                subwaytime1 = subwayarrive.getTime1();

                                subwayhandler.removeMessages(0);
                                subwayhandler2.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                Message msg = subwayhandler3.obtainMessage();
                                subwayhandler3.sendMessage(msg);
                            }
                            else if(subwayarrive.getcheck2()){
                                subwaytime2 = subwayarrive.getTime2();

                                subwayhandler.removeMessages(0);
                                subwayhandler2.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                Message msg = subwayhandler2.obtainMessage();
                                subwayhandler2.sendMessage(msg);
                            }
                            else {
                                subwayhandler.removeMessages(0);
                                subwayhandler2.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                subway = subwaystationname + "역 " + inputstationline + " " + subwayarrive.getoutput();
                            }

                        }

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
