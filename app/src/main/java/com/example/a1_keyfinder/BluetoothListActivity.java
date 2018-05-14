package com.example.a1_keyfinder;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class BluetoothListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Button enableBtn;
    private Button findDevicesBtn;
    BluetoothAdapter mBluetoothAdapter;

    public ArrayList<BluetoothDevice>mBTDevices=new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView listViewDevices;



    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,mBluetoothAdapter.ERROR);

                switch (state)
                {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d("Maps_Activity","State OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d("Maps_Activity","State turning OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d("Maps_Activity","State ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d("Maps_Activity","State turuing ON");
                        break;

                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d("Maps_Activity", "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d("Maps_Activity", "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d("Maps_Activity", "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d("Maps_Activity", "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d("Maps_Activity", "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };


    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d("BT_ACTIVITY", "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);

                Log.d("BluetoothListActivity" , "onReceive: "+ device.getName()+" : "+ device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);

                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                listViewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };



    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d("BTActivity", "BroadcastReceiver: BOND_BONDED.");
                }
                //case2: creating a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d("BTActivity", "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d("BTActivity", "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };


  /*  @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
    }
*/
    @Override
    protected void onPause()
    {
        super.onPause();
        try {
            unregisterReceiver(mBroadcastReceiver1);
            unregisterReceiver(mBroadcastReceiver2);
            unregisterReceiver(mBroadcastReceiver3);
            unregisterReceiver(mBroadcastReceiver4);

        } catch (IllegalArgumentException e) {
            // ignored
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);
       // Intent intent = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enableBtn=findViewById(R.id.enableBtn);
        findDevicesBtn=findViewById(R.id.findDevices);

        listViewDevices=(ListView) findViewById(R.id.listViewDevices);
        mBTDevices=new ArrayList<>();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4,filter);

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        listViewDevices.setOnItemClickListener(BluetoothListActivity.this);

        enableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableDisableBT();
                makeDeviceDiscoverable();
                //showDevices();
            }
        });
        findDevicesBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                showDevices();

            }
        });


    }

    public void enableDisableBT()
    {
        if(mBluetoothAdapter==null)
        {
            Log.d("Maps_activity","No BT capabilities.");
        }
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBTIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1,BTIntent);

        }
        /*if(mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.disable();
            IntentFilter BTIntent=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1,BTIntent);
        }
        */
    }

    public void makeDeviceDiscoverable()
    {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter=new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);
    }



    public void showDevices()
    {
        if(mBluetoothAdapter.isDiscovering())
        {
            mBluetoothAdapter.cancelDiscovery();
            //checkBTPermissions();
            Log.d("BtListActivity", " - CANCEL Discovery");
            mBluetoothAdapter.startDiscovery();

            IntentFilter discoverDevicesIntent=new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering())
        {
            //checkBTPermissions();
            Log.d("BtListActivity - ", "isDiscovering");
            IntentFilter discoverDevicesIntent=new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetoothAdapter.cancelDiscovery();
        Log.d("BTActivity", "onItemClick: you clicked a device");
        String deviceName=mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d("BTActivity","onItem click: " + deviceName + deviceAddress);

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            Log.d("BTActivity", "PAIRING with ----->" + deviceName);
            mBTDevices.get(i).createBond();
        }
    }


   /* private void checkBTPermissions()
    {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP)
        {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck+=this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");

            if(permissionCheck!=0)
            {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1001);
            }
        }
    }
    */
}