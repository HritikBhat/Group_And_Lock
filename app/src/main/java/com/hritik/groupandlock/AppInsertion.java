package com.hritik.groupandlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AppInsertion extends AppCompatActivity {
    ListAdapterApps adapter;
    ListView list;
    ArrayList<String> name,pkg;
    ArrayList<Drawable> dw;
    String sect;

    private void getAllApps(){
        Intent mainIntent= new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> pkgAppList=this.getPackageManager().queryIntentActivities(mainIntent,0);
        System.out.println(pkgAppList.size());
        for(ResolveInfo resolve_info : pkgAppList) {
            try {
                String package_name = resolve_info.activityInfo.packageName;
                pkg.add(package_name);
                String app_name = (String) getPackageManager().getApplicationLabel(
                        getPackageManager().getApplicationInfo(package_name
                                , PackageManager.GET_META_DATA));
                name.add(app_name);
                Drawable app_icon= getPackageManager().getApplicationIcon(package_name);
                dw.add(app_icon);
            }catch (Exception e){e.printStackTrace();}
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_insertion);
        name=new ArrayList<String>();
        pkg=new ArrayList<String>();
        dw=new ArrayList<Drawable>();
        Intent intn= getIntent();
        sect=intn.getStringExtra("sect");
        getAllApps();
        System.out.println("Installed Apps: "+name.size());
        try {
            adapter = new ListAdapterApps(this, name,dw,pkg,sect);
            list = findViewById(R.id.app_listview);
            list.setAdapter(adapter);
        }catch (Exception e){e.printStackTrace();}
    }
}
