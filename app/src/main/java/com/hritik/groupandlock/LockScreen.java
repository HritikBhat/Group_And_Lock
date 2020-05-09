package com.hritik.groupandlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

public class LockScreen extends AppCompatActivity {
    EditText pin;
    String pkgnm,ps="";
    ArrayList<Button> btn;
    int text_index=0;
    Integer[] btn_id={R.id.zero,R.id.one,R.id.two,R.id.three,R.id.four,R.id.five,R.id.six,R.id.seven,R.id.eight,R.id.nine,R.id.back};
    Bundle savedInstanceState2;
    boolean lockopen=false;
    Intent i;

    public void setText_index(int text_index) {
        this.text_index = text_index;
    }

    public int getText_index() {
        return text_index;
    }

    private  void addBtnListeners(){
        for (int i=0;i<11;i++)
        {
            final String nm;
            if (i==10){
                 nm="back";
            }
            else{ nm=String.valueOf(i);}
            Button bt=findViewById(btn_id[i]);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nm.equals("back")){
                        String ps=pin.getText().toString();
                        if (ps.length()!=0)
                        {pin.setText(ps.substring(0,ps.length()-1));
                        setText_index(pin.length());
                        pin.setSelection(getText_index());}
                    }
                    else{
                        pin.setText(pin.getText()+nm);
                        setText_index(pin.length());
                        pin.setSelection(getText_index());
                    }

                }
            });
            btn.add(bt);
        }
    }

    private boolean isPassValid(){
        String pn=pin.getText().toString();
        if (ps.length()==4){}
        else {
            MyHelper dpHelper = new MyHelper(this);
            Cursor cursor = dpHelper.getAllData("pass");
            cursor.moveToFirst();
            ps = cursor.getString(cursor.getColumnIndex("ps"));
        }
        if(ps.equals(pn)){return true;}
        return false;
    }

    private void isOtherAppExist(){
        //lockopen=true;
        if (pkgnm!=null){
            if (pkgnm.equals("NAP")){
                finish();
            }
            /*
            else {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pkgnm);
            if(launchIntent!=null){
                startActivity(launchIntent);
                finish();
            }}

             */
        }
        else{
            Intent i2 = new Intent(this, MainActivity.class);
            startActivity(i2);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        savedInstanceState2=savedInstanceState;
        btn=new ArrayList<Button>();
        pin=findViewById(R.id.chkpin);
        addBtnListeners();
        i = getIntent();
        pkgnm=i.getStringExtra("pack");
        pin.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (isPassValid())
                {
                    isOtherAppExist();
                }
                else {

                    if (pin.getText().toString().length()==4)
                    {Toast.makeText(getApplicationContext(),"Invalid Pin.Try Again",Toast.LENGTH_LONG).show();
                    pin.setText("");
                }
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }
    @Override
    public void onBackPressed() {
    }
    /*
    @Override
    protected void onPause(){
        super.onPause();
        if (!lockopen){
            Intent settingsIntent = new Intent(this, LockScreen.class);
            settingsIntent.putExtra("pack",pkgnm);
            startActivity(settingsIntent);
            //finish();
            }
    }

     */



}
/*
if((!printForegroundTask().equals("com.hritik.groupandlock"))  &&  flag2 == 0 ) {
                    if ((!printForegroundTask().equals(current_app))) {
                        flag = 0;
                    }
                }

                // if security provider is running                // then dont lanuch lock screen
                // only one time lock screen will appear
                if (printForegroundTask().equals("com.hritik.groupandlock")) {
                    flag = 2;
                }



 */