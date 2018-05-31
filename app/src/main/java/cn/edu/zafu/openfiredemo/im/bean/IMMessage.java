package cn.edu.zafu.openfiredemo.im.bean;

/**
 * 消息
 * Created by xrc on 18/5/28.
 */

public class IMMessage {

    private String content;
    private String from;
    private String type;

    public IMMessage(String content, String from, String type) {
        this.content = content;
        this.type = type;
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
