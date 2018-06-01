package cn.edu.zafu.openfiredemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import cn.edu.zafu.openfiredemo.im.InstantMessaging;
import cn.edu.zafu.openfiredemo.im.bean.Contacts;

/**
 * Created by xrc on 18/6/1.
 */

public class FriendsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        List<Contacts> friendsList = InstantMessaging.with().getContactsList();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new FriendsAdapter(friendsList));
    }
}
