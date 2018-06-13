package cn.edu.zafu.openfiredemo.im.extension;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.text.TextUtils;

import org.jivesoftware.smack.packet.ExtensionElement;

import cn.edu.zafu.openfiredemo.App;
import cn.edu.zafu.openfiredemo.im.uitl.XmlBuilder;

/**
 * 消息体
 * Created by xrc on 18/5/24.
 */

public class IMMessageExtension implements ExtensionElement {

    private static final String ELEMENT = "userinfo";
    private static final String NAME_SPACE = "cn.ctvonline";
    private static final String UUID = "uuid";
    private static final String TYPE = "type";
    private String type;


    @Override
    public String getNamespace() {
        return NAME_SPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    public void setType(String type) {
        if (TextUtils.isEmpty(type)) {
            type = Type.TEXT;
        }
        this.type = type;
    }

    private String handleString(String string) {
        return TextUtils.isEmpty(string) ? "" : string;
    }

    @Override
    public CharSequence toXML() {
        return new XmlBuilder()
                .addElement(ELEMENT)
                .addInnerElement(ELEMENT, UUID, getUUID())
                .build();
    }

    @SuppressLint("HardwareIds")
    private String getUUID() {
        return Settings.Secure.getString(App.getInstance().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public class Type {
        public static final String TEXT = "text";
        public static final String AUDIO = "audio";
        public static final String PIC = "pic";
    }

}
