package com.example.transportation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;

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
    private SaveBusDBOpenHelper savebusdbopenhelper;

    int size;
    String arrtime, deptime;

    ArrayList arr;
    ArrayList dep;

    boolean start;
    boolean checkarrtime = false;

    long nowIndex;
    static ArrayList<String> busarrayIndex =  new ArrayList<String>();
    static ArrayList<String> busarrayData = new ArrayList<String>();
    static ArrayList<Integer> subwayarrtimedata = new ArrayList<Integer>();
    static ArrayList<Integer> subwaydeptimedata = new ArrayList<Integer>();

    String subwaystationid;
    String[][] subwayName = {
            {"1호선", "소요산", "하행", "SUB100"},//하행
            {"1호선","인천", "상행", "SUB160"},//상행
            {"1호선","광명", "둘다", "SUB1175"},//
            {"1호선","서동탄", "둘다", "SUB1187"},
            {"1호선","신창", "상행", "SUB1416"},//상행
            {"2호선","까치산", "외선", "SUB264"},//외선
            {"2호선","신설동", "내선", "SUB253"},
            {"3호선","대화", "하행", "SUB310"},
            {"3호선","오금", "상행", "SUB352"},
            {"4호선","오이도", "둘다", "SUB456"},
            {"4호선","당고개", "하행", "SUB409"},
            {"5호선","방화", "하행", "SUB510"},
            {"5호선","마천", "상행", "SUB575"},
            {"5호선","상일동", "상행", "SUB553"},
            {"6호선","봉화산", "상행", "SUB647"},//상행
            {"7호선","장암", "하행", "SUB709"},//하행
            {"7호선","부평구청", "상행", "SUB759"},
            {"8호선","암사", "하행", "SUB810"},//하행
            {"8호선","모란", "상행", "SUB826"},
            {"9호선","개화", "하행", "SUB901"},//하행
            {"경의중앙선","문산", "하행", "SUB1629"},//하행
            {"경의중앙선","지평", "둘다", "SUB1299"},
            {"경의중앙선","용문", "상행", "SUB1300"},//상행
            {"경춘선","청량리", "둘다", "SUB1806"},
            {"경춘선","회기", "둘다", "SUB1317"},
            {"경춘선","중랑", "둘다", "SUB1316"},
            {"경춘선","춘천", "상행", "SUB1830"},//상행
            {"공항철도","서울", "상행", "SUB4001"},//상행
            {"공항철도","인천공항2터미널", "하행", "SUB4013"},//하행
            {"분당선","왕십리", "하행", "SUB1510"},//하행
            {"분당선","수원", "상행", "SUB1545"},//상행
            {"수인선","오이도", "둘다", "SUB11121"},//둘다
            {"수인선","인천", "둘다", "SUB11134"},
            {"수인선","신포", "둘다", "SUB11133"},
            {"수인선","숭의", "둘다", "SUB11132"},
            {"수인선","인하대", "둘다", "SUB11131"},
            {"신분당선","강남", "하행", "SUB1910"},//하행
            {"신분당선","광교", "하행", "SUB1922"}//상행
    };

    ArrayAdapter<String> arrayAdapter;

    String dailyCode, UDCode;
    long mNow;
    Date mDate;

    Spinner directionspinner;

    static ArrayList<Integer> SUBWAYArrivetime = new ArrayList<Integer>();
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

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.savebusinfo);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        laststationdbopenhelper = new LastStationDBOpenHelper(this);
        laststationdbopenhelper.open();
        laststationdbopenhelper.create();

        savebusdbopenhelper = new SaveBusDBOpenHelper(this);
        savebusdbopenhelper.open();
        savebusdbopenhelper.create();

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
        subwayline.add("경의중앙선");
        subwayline.add("공항철도");
        subwayline.add("경춘선");
        subwayline.add("수인선");
        subwayline.add("분당선");
        subwayline.add("신분당선");

        Spinner linespinner = (Spinner) findViewById(R.id.subwayspinner);

        ArrayAdapter<String> lineadapter; // ArrayAdapter <String>형의 변수 선언
        lineadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subwayline);
        linespinner.setAdapter(lineadapter);

        directionspinner = (Spinner) findViewById(R.id.directionspinner);

        ArrayAdapter<String> directionadapter; // ArrayAdapter <String>형의 변수 선언
        directionadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, direction);
        directionspinner.setAdapter(directionadapter);

            linespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                directionspinner.setSelection(0);
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

        directionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                directioninfo = direction.get(position).toString();
                if(directioninfo != "방향") {
                    inputsubwaystation.setEnabled(true);
                }
                start = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        busbutton = (Button) findViewById(R.id.busbutton);

        getBUSDatabase();

    }

    public void getSUBWAYDatabase(){
        System.out.println("getSubwayDatabase들어옴");
        Cursor iCursor = laststationdbopenhelper.exist(subwaystationid, dailyCode, UDCode);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());

        while(iCursor.moveToNext()) {
            String temparrtime = iCursor.getString(iCursor.getColumnIndex("arrtime"));
            String tempDeptime = iCursor.getString(iCursor.getColumnIndex("deptime"));

            subwayarrtimedata.add(Integer.parseInt(temparrtime));
            subwaydeptimedata.add(Integer.parseInt(tempDeptime));
        }

        if(subwayarrtimedata.get(0) == 0) {
            checkarrtime = false;
        }
        else{
            checkarrtime = true;
        }
    }

    public ArrayList getSUBWAYArrive(){
        int firstarr, secondarr, vhour, vminute, vsecond, a1hour, a1minute, a1second, a2hour, a2minute, a2second, finalsecond1, finalsecond2;
        ArrayList<Integer> tempSUBWAYArrivetime = new ArrayList<Integer>();
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        String[] time = mDate.toString().split(" ");
        String[] nowtime = time[3].split(":");
        int viewtime = Integer.parseInt(nowtime[0]+nowtime[1]+nowtime[2]);
        System.out.println("viewtime=" + viewtime);
        int i;
        System.out.println("checkarrtime은 " + checkarrtime);
        if(checkarrtime) {
            for(i = 0; i < subwayarrtimedata.size(); i++) {
                System.out.println(subwayarrtimedata.get(i));
                if(viewtime<subwayarrtimedata.get(i)) {
                    break;
                }
                else {
                    continue;
                }
            }

            System.out.println("뒤에 올 시간 = " +subwayarrtimedata.get(i) + ", " + subwayarrtimedata.get(i+1));
            vhour = viewtime/10000;
            vminute = viewtime%10000;
            vsecond = vminute%100;
            vminute = vminute/100;
            vsecond = (vhour*3600)+(vminute*60)+vsecond;
            System.out.println("vsecond = " + vsecond);
            firstarr = subwayarrtimedata.get(i);
            a1hour = firstarr/10000;
            a1minute = firstarr%10000;
            a1second = a1minute%100;
            a1minute = a1minute/100;
            a1second = (a1hour*3600)+(a1minute*60)+a1second;
            System.out.println("a1second = " + a1second);
            finalsecond1 = a1second-vsecond;
            System.out.println("finalsecond1=" + finalsecond1);
            secondarr = subwayarrtimedata.get(i+1);
            a2hour = secondarr/10000;
            a2minute = secondarr%10000;
            a2second = a2minute%100;
            a2minute = a2minute/100;
            a2second = (a2hour*3600)+(a2minute*60)+a2second;
            System.out.println("asecond = " + a2second);
            finalsecond2 = a2second-vsecond;
            System.out.println("finalsecond2=" + finalsecond2);

            tempSUBWAYArrivetime.add(finalsecond1);
            tempSUBWAYArrivetime.add(finalsecond2);
            return tempSUBWAYArrivetime;
        }
        else {
            System.out.println("deptime쪽 들어옴");
            for(i = 0; i < subwaydeptimedata.size(); i++) {

                System.out.println(subwaydeptimedata.get(i));
                if(viewtime<subwaydeptimedata.get(i)) {
                    break;
                }
                else {
                    continue;
                }
            }

            System.out.println("뒤에 올 시간 = " +subwaydeptimedata.get(i) + ", " + subwaydeptimedata.get(i+1));
            vhour = viewtime/10000;
            vminute = viewtime%10000;
            vsecond = vminute%100;
            vminute = vminute/100;
            vsecond = (vhour*3600)+(vminute*60)+vsecond;
            System.out.println("vsecond = " + vsecond);
            firstarr = subwaydeptimedata.get(i);
            a1hour = firstarr/10000;
            a1minute = firstarr%10000;
            a1second = a1minute%100;
            a1minute = a1minute/100;
            a1second = (a1hour*3600)+(a1minute*60)+a1second;
            System.out.println("a1second = " + a1second);
            finalsecond1 = a1second-vsecond;
            System.out.println("finalsecond1=" + finalsecond1);
            secondarr = subwaydeptimedata.get(i+1);
            a2hour = secondarr/10000;
            a2minute = secondarr%10000;
            a2second = a2minute%100;
            a2minute = a2minute/100;
            a2second = (a2hour*3600)+(a2minute*60)+a2second;
            System.out.println("asecond = " + a2second);
            finalsecond2 = a2second-vsecond;
            System.out.println("finalsecond2=" + finalsecond2);

            tempSUBWAYArrivetime.add(finalsecond1);
            tempSUBWAYArrivetime.add(finalsecond2);
            return tempSUBWAYArrivetime;
        }
    }

    public void getBUSDatabase(){
        System.out.println("getBUSDatabase들어옴");
        Cursor iCursor = savebusdbopenhelper.sortColumn();
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        busarrayData.clear();
        busarrayIndex.clear();
        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempStationid = iCursor.getString(iCursor.getColumnIndex("stationid"));
            String tempBusid = iCursor.getString(iCursor.getColumnIndex("busid"));
            String tempOrd = iCursor.getString(iCursor.getColumnIndex("ord"));
            String tempArsid = iCursor.getString(iCursor.getColumnIndex("arsid"));
            String tempBusnumber = iCursor.getString(iCursor.getColumnIndex("busnumber"));
            String tempStainname = iCursor.getString(iCursor.getColumnIndex("stationname"));
            String result = tempIndex +"," + tempStationid + "," + tempBusid + "," + tempOrd + "," + tempArsid + "," + tempBusnumber + "," +tempStainname;
            lastresult += result;
            lastresult += "\n";
            busarrayData.add(result);
            busarrayIndex.add(tempIndex);
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(busarrayData);
        arrayAdapter.notifyDataSetChanged();
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
                    start = true;
                    break;

                case 3000:
                    pickbusnumber = data.getStringExtra("busnumber");
                    inputbus.setText(pickbusnumber);
                    inputbusnumber = pickbusnumber;
                    BusID = data.getStringExtra("busid");
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
                laststationdbopenhelper.insertColumn(subwaystationid, dailyCode, UDCode, arrtime, deptime);
            }
            getSUBWAYDatabase();
            SUBWAYArrivetime = getSUBWAYArrive();

            subwaytime1 = SUBWAYArrivetime.get(0);
            subwaytime2 = SUBWAYArrivetime.get(1);

            subwayhandler.removeMessages(0);
            subwayhandler2.removeMessages(0);
            subwayhandler3.removeMessages(0);
            subwayhandler3.removeMessages(0);
            Message msg2 = subwayhandler.obtainMessage();
            subwayhandler.sendMessage(msg2);

        }
    };

    final Handler savebushandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            savebusdbopenhelper.open();
            System.out.println("Arsid=" + Arsid);
            savebusdbopenhelper.insertColumn(StationID, BusID, Ord, Arsid, inputbusnumber, inputstationname);
            getBUSDatabase();
        }
    };

    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("On Click", "position = " + position);
            nowIndex = Long.parseLong(busarrayIndex.get(position));
            Log.e("On Click", "nowIndex = " + nowIndex);
            Log.e("On Click", "Data: " + busarrayData.get(position));
            String[] tempData = busarrayData.get(position).split(",");
            System.out.println("tempData는" + tempData);
            Log.e("On Click", "Split Result = " + tempData);
            StationID = tempData[1].trim();
            BusID = tempData[2].trim();
            Ord = tempData[3].trim();
            Arsid= tempData[4].trim();
            inputbusnumber = tempData[5].trim();
            subwaystationname = tempData[6].trim();
            System.out.println("StationID "+StationID);
            System.out.println("BusID "+BusID);
            System.out.println("Ord "+Ord);
            System.out.println("Arsid "+Arsid);
            System.out.println("inputbusnumber "+inputbusnumber);

            inputbus.setText(inputbusnumber);
            inputstation.setText(subwaystationname);

            busbutton.setEnabled(true);
            busbutton.performClick();

        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            nowIndex = Long.parseLong(busarrayIndex.get(position));
            String[] nowData = busarrayData.get(position).split("\\s+");
            System.out.println("nowData는"+ nowData);
            String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3];
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("데이터 삭제")
                    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" + viewData)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                            savebusdbopenhelper.deleteColumn(nowIndex);
                            getBUSDatabase();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };

    public void mOnClick(View v){
        hideKeyboard();
        switch(v.getId()) {
            case R.id.savebus:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Message msg = savebushandler.obtainMessage();
                        savebushandler.sendMessage(msg);
                    }
                }).start();
                break;

/*
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

*/
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
                        System.out.println("arsid는"+Arsid);
                        output = allbusarrive.getAllBusArrive(Arsid);
                        System.out.println("output은" + output);
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

                        boolean apicheck = true;
                        System.out.println(start);
                        if(start) {
                            System.out.println("start들어옴");
                            for (int i = 0; i < 38; i++) {
                                System.out.println("inputstationline" + inputstationline);
                                System.out.println("subwaystationname" + subwaystationname);
                                System.out.println("directioninfo" + directioninfo);
                                System.out.println("subwayName[i][0]" + subwayName[i][0]);
                                System.out.println("subwayName[i][1]" + subwayName[i][1]);
                                System.out.println("subwayName[i][2]" + subwayName[i][2]);

                                if (((subwayName[i][0].equals(inputstationline)) && (subwayName[i][1].equals(subwaystationname)))) {
                                    System.out.println("첫번째 if 실행");
                                    if (subwayName[i][2].equals("둘다")) {
                                        System.out.println("둘다임");
                                        apicheck = true;
                                    } else if (subwayName[i][2] == directioninfo) {
                                        System.out.println("똑같습니다.");
                                        apicheck = true;
                                    }
                                    else {
                                        break;
                                    }

                                    subwaystationid = subwayName[i][3];

                                    mNow = System.currentTimeMillis();
                                    mDate = new Date(mNow);
                                    String[] time = mDate.toString().split(" ");
                                    if(time[0].equals("Sun")) {dailyCode = "03";}
                                    else if(time[0].equals("Sat")) {dailyCode = "02";}
                                    else {dailyCode = "01";}

                                    if (directioninfo == "상행" || directioninfo == "외선") {
                                        UDCode = "U";
                                    } else {
                                        UDCode = "D";
                                    }
                                    i = 39;
                                }

                                else {
                                    apicheck = false;
                                }

                            }
                            start = false;
                        }

                        if(apicheck) {
                            System.out.println("공공api 들어옴");
                            Cursor iCursor = laststationdbopenhelper.exist(subwaystationid, dailyCode, UDCode);
                            Log.d("showDatabase", "DB Size: " + iCursor.getCount());
                            if(iCursor.getCount()==0){
                                subwaylaststation.LastArriveManager(subwaystationid, dailyCode, UDCode);

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
                            }
                            else{
                                getSUBWAYDatabase();
                                SUBWAYArrivetime = getSUBWAYArrive();

                                subwaytime1 = SUBWAYArrivetime.get(0);
                                subwaytime2 = SUBWAYArrivetime.get(1);

                                subwayhandler.removeMessages(0);
                                subwayhandler2.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                subwayhandler3.removeMessages(0);
                                Message msg = subwayhandler.obtainMessage();
                                subwayhandler.sendMessage(msg);
                            }

                        }
                        else {
                            System.out.println("실시간 api 시작");
                            ArrayList subwayoutput = new ArrayList<>();
                            subwayarrive.SubwayArriveManager(inputsubwaystationname, stationline, directioninfo);

                            subwayoutput = subwayarrive.getoutputarray();
                            System.out.println("subwayoutput은" + subwayoutput);
                            if(subwayoutput.size() == 0) {
                                subwaybutton.performClick();
                            }
                            else if(subwayoutput.size() == 1) {
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
