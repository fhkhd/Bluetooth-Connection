package com.example.bluetoothconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button lisen_btn , send_btn , deviseslist_btn ;
    private EditText mssg_ed;
    private TextView connection_mode_btn , txt_message;
    private RecyclerView recycler_devices;


    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice[] btArray;

    private SendReceive sendReceive;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    private int REQUEST_ENABLE_BLUETOOTH=1;

    private static final String APP_NAME = "bluetooth_connection";
    private static final UUID MY_UUID=UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66");

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }

        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case STATE_LISTENING:
                    connection_mode_btn.setText("Listening...");
                    break;
                case STATE_CONNECTING:
                    connection_mode_btn.setText("Connecting...");
                    break;
                case STATE_CONNECTED:
                    connection_mode_btn.setText("Connected");
//                    if (status.getText().toString() == "Connected"){
//
//                        status.setTextColor(getResources().getColor(R.color.connected));
//                    }
                    break;
                case STATE_CONNECTION_FAILED:
                    connection_mode_btn.setText("Connection Failed");
//                    status.setTextColor(getResources().getColor(R.color.connection_failed));
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg=new String(readBuff,0,msg.arg1);
                    txt_message.setText(tempMsg);
                    break;
            }
            return true;
        }
    });

}