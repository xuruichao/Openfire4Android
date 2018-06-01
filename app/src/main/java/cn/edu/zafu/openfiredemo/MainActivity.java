package cn.edu.zafu.openfiredemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.zafu.openfiredemo.im.ChatListener;
import cn.edu.zafu.openfiredemo.im.ConnectionListener;
import cn.edu.zafu.openfiredemo.im.InstantMessaging;
import cn.edu.zafu.openfiredemo.im.bean.IMMessage;
import cn.edu.zafu.openfiredemo.im.db.DBService;
import cn.edu.zafu.openfiredemo.im.db.bean.ChatRecord;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText account, password, to, content, registeraccount, registerpsd, et_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        verifyStoragePermissions();
    }

    private void initView() {
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        to = findViewById(R.id.to);
        content = findViewById(R.id.content);
        registeraccount = findViewById(R.id.registeraccount);
        registerpsd = findViewById(R.id.registerpsd);
        et_friend = findViewById(R.id.et_friend);

        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
        findViewById(R.id.relogin).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);
        findViewById(R.id.record).setOnClickListener(this);

        findViewById(R.id.startRecord).setOnClickListener(this);
        findViewById(R.id.stopRecord).setOnClickListener(this);
        findViewById(R.id.cancelRecord).setOnClickListener(this);
        findViewById(R.id.listenRecord).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.check).setOnClickListener(this);
        findViewById(R.id.addFriend).setOnClickListener(this);
        findViewById(R.id.deleteFriend).setOnClickListener(this);
        findViewById(R.id.friendsList).setOnClickListener(this);

        InstantMessaging.with().addConnectionListener(new ConnectionListener() {
            @Override
            public void connected() {
                Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void authenticated() {
                Toast.makeText(MainActivity.this, "authenticated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void connectionClosed() {
                Toast.makeText(MainActivity.this, "connectionClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void connectionClosedOnError() {
                Toast.makeText(MainActivity.this, "connectionClosedOnError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void reconnectionSuccessful() {
                Toast.makeText(MainActivity.this, "reconnectionSuccessful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void reconnectingIn() {
                Toast.makeText(MainActivity.this, "reconnectingIn", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void reconnectionFailed() {
                Toast.makeText(MainActivity.this, "reconnectionFailed", Toast.LENGTH_SHORT).show();
            }
        });
        InstantMessaging.with().addChatListener(new ChatListener() {

            @Override
            public void onReceiveText(IMMessage message) {
                Toast.makeText(getApplicationContext(), "收到:" + message.getContent() + "  来自:" + message.getFrom() + "消息类型：TEXT", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceiveAudio(IMMessage message) {
                Toast.makeText(getApplicationContext(), "收到:" + message.getContent() + "  来自:" + message.getFrom() + "消息类型：AUDIO", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivePic(IMMessage message) {
                Toast.makeText(getApplicationContext(), "收到:" + message.getContent() + "  来自:" + message.getFrom() + "消息类型：PIC", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteFriend:
                InstantMessaging.with().deleteFriend(et_friend.getText().toString());
                break;
            case R.id.friendsList:
                startActivity(new Intent(this, FriendsActivity.class));
                break;
            case R.id.addFriend:
                InstantMessaging.with().addFriend(et_friend.getText().toString());
                break;
            case R.id.check:
                boolean isConnected = InstantMessaging.with().isConnected();
                Toast.makeText(this, isConnected ? "连上了" : " 没连上", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relogin: {
                String a = account.getText().toString();
                String p = password.getText().toString();
                if (TextUtils.isEmpty(a) || TextUtils.isEmpty(p)) {
                    Toast.makeText(getApplicationContext(), "账号或密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                InstantMessaging.with().reLogin(a, p);
                break;
            }
            case R.id.record:
                Intent intent = new Intent(this, RecordActivity.class);
                intent.putExtra("oppositeUserId", to.getText().toString() + "@" + InstantMessaging.with().getConnectionConfig().getServer());
                startActivity(intent);
                break;
            case R.id.register:
                InstantMessaging.with().register(registeraccount.getText().toString(), registerpsd.getText().toString());
                break;
            case R.id.listenRecord:
                InstantMessaging.with().listenRecord("/storage/emulated/0/SoundRecorder/qd_record.amr");
                break;
            case R.id.startRecord:
                InstantMessaging.with().startRecord();
                break;
            case R.id.stopRecord:
                InstantMessaging.with().stopRecord();
                break;
            case R.id.cancelRecord:
                InstantMessaging.with().cancelRecord();
                break;
            case R.id.login: {
                String a = account.getText().toString();
                String p = password.getText().toString();
                if (TextUtils.isEmpty(a) || TextUtils.isEmpty(p)) {
                    Toast.makeText(getApplicationContext(), "账号或密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                InstantMessaging.with().login(a, p);
                break;
            }
            case R.id.logout:
                InstantMessaging.with().disconnect();
                break;
            case R.id.send:
                String t = to.getText().toString();
                String c = content.getText().toString();
                content.setText(c);
                if (TextUtils.isEmpty(t) || TextUtils.isEmpty(c)) {
                    Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                InstantMessaging.with().sendMessage(t, c);
                ChatRecord chatRecord = new ChatRecord(null, false, c, t + "@" + InstantMessaging.with().getConnectionConfig().getServer());
                DBService.getChatRecordDao().insert(chatRecord);
                break;
        }

    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public void verifyStoragePermissions() {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限", Toast.LENGTH_SHORT).show();
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "cuowu", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
