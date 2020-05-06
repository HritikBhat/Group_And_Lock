package com.hritik.groupandlock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton add;
    ListView list;
    ListAdapterMain adapter;
    ArrayList<String> name;
    final Context context=this;

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void insertGrp(String grp_nm){
        MyHelper dpHelper = new MyHelper(this);
        SQLiteDatabase db = dpHelper.getReadableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("grp_name", grp_nm);
        long rows =db.insert("grp", null, insertValues);
        db.close();
        name.add(grp_nm);
        adapter.notifyDataSetChanged();
    }

    private void addGroups(){
        MyHelper dpHelper = new MyHelper(getApplicationContext());
        try {
            //To get cursor
            Cursor cursor = dpHelper.getAllData("grp");
            while (cursor.moveToNext()) {
                name.add(cursor.getString(cursor.getColumnIndex("grp_name")));
            }
        }
        catch (Exception e){e.printStackTrace();}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isMyServiceRunning(BackgroundServices.class)){
            startService(new Intent(getBaseContext(), BackgroundServices.class));
        }
        name=new ArrayList<String>();
        addGroups();
        add=findViewById(R.id.add_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dialog which will take group name
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Create",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String gpname = userInput.getText().toString();
                                        insertGrp(gpname);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
        try {
            adapter = new ListAdapterMain(this, name);
            list = findViewById(R.id.section_listview);
            list.setAdapter(adapter);
        }
        catch (Exception e){e.printStackTrace();}


    }
}
