package com.hritik.groupandlock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AppSection extends AppCompatActivity {
    String sect;
    ArrayList<String> pkgnm,appnm;
    ArrayList<FloatingActionButton> ft;
    ArrayList<TextView> txt;
    boolean dataFlag=false;
    FloatingActionButton add;

    private void openApp(String pkg){
        Toast.makeText(getApplicationContext(),pkg,Toast.LENGTH_LONG);
        //Here it opens app
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pkg);
        if(launchIntent!=null){
            startActivity(launchIntent);
        }
    }

    private void deleteApp(String pkg){
        MyHelper hp=new MyHelper(getApplicationContext());
        SQLiteDatabase db = hp.getWritableDatabase();
        String query = "DELETE FROM app_dts WHERE pkg_name = '" + pkg + "' AND grp_name='"+sect+"'";
        db.execSQL(query);
        hp.close();
        Toast.makeText(getApplicationContext(),"Delete Successful",Toast.LENGTH_LONG).show();
        notifyFABDataChanged();
    }
    private void sureDeleteApp(final int pos){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Delete App");
        builder1.setMessage("Selected App will get deleted.Delete?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteApp(pkgnm.get(pos));
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

    private void notifyFABDataChanged(){
        getSectionData();
        for (int i=0;i<txt.size();i++)
        {
            txt.get(i).setText("");
            ft.get(i).setVisibility(View.INVISIBLE);
        }
        txt.clear();
        ft.clear();
        if (dataFlag==true) {
            for (int i = 0; i < pkgnm.size(); i++) {
                int index = i + 1;
                String text = "textView" + String.valueOf(index);
                String icon = "icon_btn" + String.valueOf(index);
                TextView tt = findViewById(getResources().getIdentifier(text, "id", getPackageName()));
                tt.setText(appnm.get(i));
                txt.add(tt);
                FloatingActionButton fbtn = findViewById(getResources().getIdentifier(icon, "id", getPackageName()));
                try {
                    Drawable appIcon = getPackageManager().getApplicationIcon(pkgnm.get(i));
                    fbtn.setImageDrawable(appIcon);
                    fbtn.setVisibility(View.VISIBLE);
                    final int pos = i;
                    fbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApp(pkgnm.get(pos));
                        }
                    });
                    fbtn.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            sureDeleteApp(pos);
                            return true;
                        }
                    });
                    ft.add(fbtn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void getSectionData(){
        appnm.clear();
        pkgnm.clear();
        dataFlag=false;
        MyHelper dpHelper = new MyHelper(getApplicationContext());
        try {
            //To get cursor
            Cursor cursor = dpHelper.getCondnData("app_dts",sect);
            if (cursor.getCount()<1){
            }
            else{
            while (cursor.moveToNext()) {
                appnm.add(cursor.getString(cursor.getColumnIndex("app_name")));
                pkgnm.add(cursor.getString(cursor.getColumnIndex("pkg_name")));
            }
                dataFlag=true;
            }
        }
        catch (Exception e){e.printStackTrace();}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_section);
        Intent intent=getIntent();
        sect = intent.getStringExtra("section");
        add=findViewById(R.id.sadd_btn);
        pkgnm=new ArrayList<>();
        appnm=new ArrayList<>();
        ft=new ArrayList<>();
        txt=new ArrayList<>();
        notifyFABDataChanged();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AppInsertion.class);
                i.putExtra("sect",sect);
                startActivity(i);

            }
        });
    }
}
