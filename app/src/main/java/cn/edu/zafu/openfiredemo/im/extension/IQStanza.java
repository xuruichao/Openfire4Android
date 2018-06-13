package cn.edu.zafu.openfiredemo.im.extension;

import org.jivesoftware.smack.packet.Stanza;

import cn.edu.zafu.openfiredemo.im.uitl.XmlBuilder;

/**
 * IQStanza
 * Created by xrc on 18/6/6.
 */

public class IQStanza extends Stanza {

    private static final String ELEMENT = "iq";

    @Override
    public CharSequence toXML() {
        return new XmlBuilder()
                .addElement(ELEMENT)
                .build();
    }
}
