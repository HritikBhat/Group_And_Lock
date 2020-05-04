package com.hritik.groupandlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GetPassScreen extends AppCompatActivity {
    Button submit;
    EditText pin,pin_confirm;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String flag_var = "passFlag";

    private void insertPass(){
        MyHelper dpHelper = new MyHelper(this);
        SQLiteDatabase db = dpHelper.getReadableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("ps", pin.getText().toString());
        long rows =db.insert("pass", null, insertValues);
        db.close();
    }
    private void insertMainApp(){
        MyHelper dpHelper = new MyHelper(this);
        SQLiteDatabase db = dpHelper.getReadableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("app_name",getString(R.string.app_name));
        insertValues.put("grp_name","Main");
        insertValues.put("pkg_name","com.hritik.groupandlock");
        long rows =db.insert("app_dts", null, insertValues);
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pass_screen);

        pin=findViewById(R.id.grppin);
        pin_confirm=findViewById(R.id.grppincnf);
        submit=findViewById(R.id.sbt_btn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pin.getText().toString().equals(pin_confirm.getText().toString()))
                {
                    insertPass();
                    insertMainApp();
                    SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(flag_var, true);
                    editor.commit();
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Both pin must be same.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
