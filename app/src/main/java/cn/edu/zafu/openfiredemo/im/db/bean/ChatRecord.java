package cn.edu.zafu.openfiredemo.im.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 聊天记录bean
 * Created by xrc on 18/5/24.
 */
@Entity
public class ChatRecord {

    @Id(autoincrement = true)
    private Long id;
    private boolean isReceive;
    private String content;
    private String oppositeUserId;
    @Generated(hash = 521034911)
    public ChatRecord(Long id, boolean isReceive, String content,
            String oppositeUserId) {
        this.id = id;
        this.isReceive = isReceive;
        this.content = content;
        this.oppositeUserId = oppositeUserId;
    }
    @Generated(hash = 1442974643)
    public ChatRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getIsReceive() {
        return this.isReceive;
    }
    public void setIsReceive(boolean isReceive) {
        this.isReceive = isReceive;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getOppositeUserId() {
        return this.oppositeUserId;
    }
    public void setOppositeUserId(String oppositeUserId) {
        this.oppositeUserId = oppositeUserId;
    }

}
