package com.gitdig.androiddemo;

import android.os.Bundle;

import com.gitdig.helloworld.GreeterGrpc;
import com.gitdig.helloworld.HelloReply;
import com.gitdig.helloworld.HelloRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.logging.Logger;

import golib.Golib;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class MainActivity extends AppCompatActivity {
    public static ManagedChannel newChannel(String host, int port) {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final GreeterGrpc.GreeterStub greeterStub = GreeterGrpc.newStub(newChannel("192.168.0.134", 50051));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.i("demo", "Hello Android");

                String goLib_hello = Golib.greetings("GoLib Hello");
                Log.i("golib", goLib_hello);

                try {
                    long l = Golib.numberError(6);
                    Log.i("golib", Long.toString(l));
                    String directory = Golib.directory(".");
                    Log.i("golib", directory);

                    Golib.walkDirectory("/data/data/com.gitdig.androiddemo");

                    Golib.sqliteCrud();
                }
                catch (Exception e) {
                    Log.e("golib", e.getMessage());
                }

                HelloRequest request = HelloRequest.newBuilder().setName("JayL").build();
                greeterStub.sayHello(request, new StreamObserver<HelloReply>() {
                    @Override
                    public void onNext(HelloReply value) {
                        Log.i("grpc", value.getMessage());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("grpc", t.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
