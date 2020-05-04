package com.hritik.groupandlock;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class ListAdapterMain extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> name;

    public ListAdapterMain(Activity context, ArrayList<String> name) {
        super(context, R.layout.section_list,name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.name=name;
    }

    private  void deleteGrp(int pos){
        String grpnm=name.get(pos);
        MyHelper hp=new MyHelper(context);
        hp.onDeleteOne("app_dts","grp_name",grpnm);
        hp.onDeleteOne("grp","grp_name",grpnm);
        name.remove(pos);
        notifyDataSetChanged();
        hp.close();
    }

    private  void sureDeleteGrp(final int pos){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete Group");
        builder1.setMessage("Selected Group will get deleted.Delete?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteGrp(pos);
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

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.section_list, null,true);
        final View vw=view;
        TextView nameText = rowView.findViewById(R.id.name);
        nameText.setText(name.get(position));
        CardView cardv = rowView.findViewById(R.id.card);
        cardv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context,AppSection.class);
                i.putExtra("section",name.get(position));
                context.startActivity(i);
            }
        });
        cardv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sureDeleteGrp(position);
                return true;
            }
        });
        return rowView;
    }
}
