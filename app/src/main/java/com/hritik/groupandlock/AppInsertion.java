package com.hritik.groupandlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AppInsertion extends AppCompatActivity {
    ListAdapterApps adapter;
    ListView list;
    ArrayList<ListItem> lt;
    EditText search;
    String sect;

    private void getAllApps(){
        Intent mainIntent= new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager ps=this.getPackageManager();
        List<ResolveInfo> pkgAppList=ps.queryIntentActivities(mainIntent,0);
        System.out.println(pkgAppList.size());
        for(ResolveInfo resolve_info : pkgAppList) {
            try {
                String package_name = resolve_info.activityInfo.packageName;
                String app_name = (String) ps.getApplicationLabel(
                        ps.getApplicationInfo(package_name
                                , PackageManager.GET_META_DATA));
                Drawable app_icon= ps.getApplicationIcon(package_name);
                ListItem inf= new ListItem(app_name,app_icon,package_name);
                lt.add(inf);
            }catch (Exception e){e.printStackTrace();}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_insertion);
        search = findViewById(R.id.search);
        lt = new ArrayList<ListItem>();
        Intent intn= getIntent();
        sect=intn.getStringExtra("sect");
        getAllApps();
        try {
            adapter = new ListAdapterApps(this,lt,sect);
            list = findViewById(R.id.app_listview);
            list.setAdapter(adapter);
        }catch (Exception e){e.printStackTrace();}
        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                adapter.filter(search.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }
}
