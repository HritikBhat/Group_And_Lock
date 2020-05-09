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
        import java.util.Locale;

public class ListAdapterApps extends ArrayAdapter<ListItem> {
    private final Activity context;
    ArrayList<ListItem> listitem;
    String sect;
    private ArrayList<ListItem> myList;  // for loading main list
    private ArrayList<ListItem> arraylist=null;  // for loading  filter data
    public ListAdapterApps(Activity context,ArrayList<ListItem> lt,String section) {
        super(context, R.layout.app_list,lt);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.listitem=lt;
        this.sect=section;
        this.myList=lt;
        this.arraylist = new ArrayList<ListItem>();
        this.arraylist.addAll(lt);
    }

    private boolean isAppAlreadyExists(int pos){
        MyHelper dpHelper = new MyHelper(context);
        Cursor cursor = dpHelper.getCondnData("app_dts",sect);
        ListItem lt= listitem.get(pos);
        while (cursor.moveToNext()) {
            String appnm=cursor.getString(cursor.getColumnIndex("app_name"));
            if (appnm.equals(lt.getappname())){
                return true;
            }
        }
        return false;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        myList.clear();
        if (charText.length() == 0) {
            myList.addAll(arraylist);
        }
        else
        {
            for (ListItem wp : arraylist) {
                if (wp.getappname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    myList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getAppsCount(){
        MyHelper dbhelper=new MyHelper(context);
        return dbhelper.getAppSectionCount(sect);
    }

    public  void insertAppDB(int pos){
        ListItem lt= listitem.get(pos);
        String appname=lt.getappname();
        String pkgname=lt.getpkgname();
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
        ListItem lt= listitem.get(position);
        TextView nameText = rowView.findViewById(R.id.appname);
        nameText.setText(lt.getappname());
        ImageView img= rowView.findViewById(R.id.imgview);
        img.setImageDrawable(lt.getapppicture());
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

