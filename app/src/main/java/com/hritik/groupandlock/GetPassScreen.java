package com.hritik.groupandlock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class GetPassScreen extends AppCompatActivity {
    Button submit;
    EditText pin,pin_confirm;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String flag_var = "passFlag";
    int passCount=0;


    private void showPermissionDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Permission Needed");
        builder1.setMessage("App Usage Stats is necessary to detect running of apps.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "PERMIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //System.out.println("DeleteFlag is false!!!");
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS,android.os.Process.myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }

    private void insertPass() {
        if (passCount == 0) {
            MyHelper dpHelper = new MyHelper(this);
            SQLiteDatabase db = dpHelper.getReadableDatabase();
            ContentValues insertValues = new ContentValues();
            insertValues.put("ps", pin.getText().toString());
            long rows = db.insert("pass", null, insertValues);
            db.close();
            passCount+=1;
        }
    }
    /*
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

     */

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
                    //insertMainApp();
                    if (!checkForPermission(getApplicationContext())){
                        showPermissionDialog();
                    }
                    else {
                        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(flag_var, true);
                        editor.commit();
                        startService(new Intent(getBaseContext(), BackgroundServices.class));
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Both pin must be same.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
