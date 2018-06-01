package cn.edu.zafu.openfiredemo.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 好友
 * Created by xrc on 18/6/1.
 */

public class Contacts implements Parcelable {

    //昵称  abc
    private String name;
    //好友关系 to from both none remove
    private String type;
    //用户  abc@192.168.1.1...
    private String user;

    public Contacts() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    protected Contacts(Parcel in) {
        name = in.readString();
        type = in.readString();
        user = in.readString();
    }

    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel in) {
            return new Contacts(in);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(user);
    }
}
