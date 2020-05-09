package com.hritik.groupandlock;

import android.graphics.drawable.Drawable;

public class ListItem {
    String appname;
    Drawable apppicture;
    String pkgname;

    public ListItem(String appname, Drawable apppicture,String pkgname) {
        super();
        this.appname = appname;
        this.apppicture = apppicture;
        this.pkgname = pkgname;
    }

    public String getappname() {
        return appname;
    }

    public void setappname(String appname) {
        this.appname = appname;
    }

    public String getpkgname() {
        return pkgname;
    }

    public void setpkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public Drawable getapppicture() {
        return apppicture;
    }

    public void setapppicture(Drawable apppicture) {
        this.apppicture = apppicture;
    }
}