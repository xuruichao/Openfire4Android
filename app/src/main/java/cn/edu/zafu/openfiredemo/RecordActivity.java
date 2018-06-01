package cn.edu.zafu.openfiredemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import cn.edu.zafu.openfiredemo.R;
import cn.edu.zafu.openfiredemo.RecordAdapter;
import cn.edu.zafu.openfiredemo.im.InstantMessaging;
import cn.edu.zafu.openfiredemo.im.db.bean.ChatRecord;

/**
 * Created by xrc on 18/5/24.
 */

public class RecordActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        String oppositeUserId = getIntent().getStringExtra("oppositeUserId");
        RecyclerView recyclerView = findViewById(R.id.recycler);
        List<ChatRecord> list = InstantMessaging.with().queryChatRecord(oppositeUserId);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new RecordAdapter(list));
    }
}
