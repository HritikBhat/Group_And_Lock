package com.hritik.groupandlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.locks.Lock;

public class LockScreen extends AppCompatActivity {
    EditText pin;
    String pkgnm,ps="";
    Bundle savedInstanceState2;
    boolean lockopen=false;
    Intent i;

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
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pkgnm);
            if(launchIntent!=null){
                startActivity(launchIntent);
                finish();
            }
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
        pin=findViewById(R.id.chkpin);
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