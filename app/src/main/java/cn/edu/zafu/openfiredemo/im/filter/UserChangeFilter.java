package cn.edu.zafu.openfiredemo.im.filter;

import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;

/**
 * 用户改变的过滤器
 * Created by xrc on 18/6/6.
 */

public class UserChangeFilter implements StanzaFilter {
    @Override
    public boolean accept(Stanza stanza) {
        return false;
    }
}
