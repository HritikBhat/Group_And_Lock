package com.hritik.groupandlock;

        import android.app.Activity;
        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.graphics.drawable.Drawable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.widget.AppCompatButton;
        import androidx.cardview.widget.CardView;

        import java.util.ArrayList;

public class ListAdapterApps extends ArrayAdapter<String> {
    private final Activity context;
    ArrayList<String> name,pkgnm;
    ArrayList<Drawable> icon;
    String sect;
    public ListAdapterApps(Activity context,ArrayList<String> name,ArrayList<Drawable> icon,ArrayList<String> pkgnm,String section) {
        super(context, R.layout.app_list,name);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.name=name;
        this.icon=icon;
        this.pkgnm=pkgnm;
        this.sect=section;
        /*System.out.println("Installed inside adapter: "+icon.size());
        System.out.println("App Name: "+name.get(0));
        System.out.println("Package Name: "+pkgnm.get(0));
         */
    }

    private boolean isAppAlreadyExists(int pos){
        MyHelper dpHelper = new MyHelper(context);
        Cursor cursor = dpHelper.getCondnData("app_dts",sect);
        while (cursor.moveToNext()) {
            String appnm=cursor.getString(cursor.getColumnIndex("app_name"));
            if (appnm.equals(name.get(pos))){
                return true;
            }
        }
        return false;
    }

    public int getAppsCount(){
        MyHelper dbhelper=new MyHelper(context);
        return dbhelper.getAppSectionCount(sect);
    }

    public  void insertAppDB(int pos){
        String appname=name.get(pos);
        String pkgname=pkgnm.get(pos);
        String grpname=sect;

        MyHelper dpHelper = new MyHelper(context);
        SQLiteDatabase db = dpHelper.getReadableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("app_name", appname);
        insertValues.put("grp_name", grpname);
        insertValues.put("pkg_name", pkgname);
        long rows =db.insert("app_dts", null, insertValues);
        db.close();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.app_list, null,true);
        final View vw=view;
        TextView nameText = rowView.findViewById(R.id.appname);
        nameText.setText(name.get(position));
        ImageView img= rowView.findViewById(R.id.imgview);
        img.setImageDrawable(icon.get(position));
        CardView cardv = rowView.findViewById(R.id.card2);
        cardv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,,Toast.LENGTH_SHORT).show();
                boolean existFlag=isAppAlreadyExists(position);
                //System.out.println("ExistFlag:::  "+existFlag);
                if (existFlag){
                    Toast.makeText(context,"The App Already Exists In The Group.",Toast.LENGTH_LONG).show();
                }
                else{
                    if (getAppsCount()>=15){
                        Toast.makeText(context,"Group can take maximum 15 apps.",Toast.LENGTH_LONG).show();
                    }
                    else{
                    insertAppDB(position);
                    Intent i = new Intent(context,AppSection.class);
                    i.putExtra("section",sect);
                    context.startActivity(i);
                    context.finish();
                }
                }
            }
        });
        return rowView;
    }
}

