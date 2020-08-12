package com.neusoft.qiangzi.aidldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.neusoft.qiangzi.locationrecord.ILocationBinder;

public class MainActivity extends AppCompatActivity {

    private ILocationBinder binder;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到行车记录页面
                Intent i = new Intent();
                //方法1：用component
                i.setComponent(new ComponentName("com.neusoft.qiangzi.locationrecord",
                        "com.neusoft.qiangzi.locationrecord.MainActivity"));
                //方法2：用action启动
//                i.setAction("com.neusoft.qiangzi.locationrecord.MainActivity");
                startActivity(i);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Location location = binder.getLocation();
                    textView.setText("经度："+location.getLongitude()+"，纬度："+location.getLatitude());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent();
        i.setComponent(new ComponentName("com.neusoft.qiangzi.locationrecord",
                "com.neusoft.qiangzi.locationrecord.LocationService"));
        bindService(i,connection,BIND_AUTO_CREATE);
    }
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = ILocationBinder.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}