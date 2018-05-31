package cn.edu.zafu.openfiredemo.im;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 即时通讯配置类
 * Created by xrc on 18/5/22.
 */

@SuppressWarnings("WeakerAccess")
public class InstantMessagingConfig implements Parcelable {

    private String mTempUserName;

    private String mUserName;

    private String mTempPassword;

    private String mPassword;

    private String mServer;

    private String mRes;

    private int mPort;

    public InstantMessagingConfig(Builder builder) {
        this.mServer = builder.server;
        this.mPort = builder.port;
        this.mRes = builder.res;
    }

    public String getTempUserName() {
        return mTempUserName;
    }

    public void setTempUserName(String mTempUserName) {
        this.mTempUserName = mTempUserName;
    }

    public String getTempPassword() {
        return mTempPassword;
    }

    public void setTempPassword(String mTempPassword) {
        this.mTempPassword = mTempPassword;
    }

    public String getServer() {
        return mServer;
    }

    public String getRes() {
        return mRes;
    }

    public int getPort() {
        return mPort;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void saveUserInfo() {
        mUserName = getTempUserName();
        mPassword = getTempPassword();
    }

    protected InstantMessagingConfig(Parcel in) {
        mServer = in.readString();
        mRes = in.readString();
        mPort = in.readInt();
    }

    public static final Creator<InstantMessagingConfig> CREATOR = new Creator<InstantMessagingConfig>() {
        @Override
        public InstantMessagingConfig createFromParcel(Parcel in) {
            return new InstantMessagingConfig(in);
        }

        @Override
        public InstantMessagingConfig[] newArray(int size) {
            return new InstantMessagingConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mServer);
        dest.writeString(mRes);
        dest.writeInt(mPort);
    }

    public static class Builder {

        private String server;

        private int port;

        private String res;

        public Builder server(String server) {
            this.server = server;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder res(String res) {
            this.res = res;
            return this;
        }

        public InstantMessagingConfig build() {
            return new InstantMessagingConfig(this);
        }
    }

}
