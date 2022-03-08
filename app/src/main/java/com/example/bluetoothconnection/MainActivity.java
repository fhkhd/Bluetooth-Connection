package com.example.bluetoothconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button lisen_btn , send_btn , deviseslist_btn ;
    private EditText mssg_ed;
    private TextView connection_mode_btn , txt_message;
    private RecyclerView recycler_devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initID();

    }

    private void initID(){
        lisen_btn = findViewById(R.id.lisen_btn);
        send_btn = findViewById(R.id.send_btn);
        deviseslist_btn = findViewById(R.id.deviseslist_btn);
        mssg_ed = findViewById(R.id.mssg_ed);
        connection_mode_btn = findViewById(R.id.connection_mode_btn);
        txt_message = findViewById(R.id.txt_message);
        recycler_devices = findViewById(R.id.recycler_devices);

    }
}