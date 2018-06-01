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

    private boolean mAutoReceipt;

    private boolean mAutoFriend;

    public InstantMessagingConfig(Builder builder) {
        this.mServer = builder.server;
        this.mPort = builder.port;
        this.mRes = builder.res;
        this.mAutoReceipt = builder.autoReceipt;
        this.mAutoFriend = builder.autoFriend;
    }

    protected InstantMessagingConfig(Parcel in) {
        mTempUserName = in.readString();
        mUserName = in.readString();
        mTempPassword = in.readString();
        mPassword = in.readString();
        mServer = in.readString();
        mRes = in.readString();
        mPort = in.readInt();
        mAutoReceipt = in.readByte() != 0;
        mAutoFriend = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTempUserName);
        dest.writeString(mUserName);
        dest.writeString(mTempPassword);
        dest.writeString(mPassword);
        dest.writeString(mServer);
        dest.writeString(mRes);
        dest.writeInt(mPort);
        dest.writeByte((byte) (mAutoReceipt ? 1 : 0));
        dest.writeByte((byte) (mAutoFriend ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public boolean isAutoReceipt() {
        return mAutoReceipt;
    }

    public boolean isAutoFriend() {
        return mAutoFriend;
    }

    public void saveUserInfo() {
        mUserName = getTempUserName();
        mPassword = getTempPassword();
    }

    public static class Builder {

        private String server;

        private String res;

        private int port;

        private boolean autoReceipt;

        private boolean autoFriend;

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

        public Builder autoReceipt(boolean autoReceipt) {
            this.autoReceipt = autoReceipt;
            return this;
        }

        public Builder autoFriend(boolean autoFriend) {
            this.autoFriend = autoFriend;
            return this;
        }

        public InstantMessagingConfig build() {
            return new InstantMessagingConfig(this);
        }
    }

}
