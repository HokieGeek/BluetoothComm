package net.hokiegeek.android.btcomm;

import java.io.IOException;

import net.hokiegeek.android.bluetooth.BluetoothItem;
import net.hokiegeek.android.bluetooth.BluetoothManager;
import net.hokiegeek.android.bluetooth.BluetoothSocketListener;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
	
public class BluetoothItemComm extends Activity {
	private BluetoothItem item = null;
	private TextView text = null;
	// private BluetoothSocket socket = null;
	private BluetoothSocketListener listener = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("[HG] BluetoothComm", "onCreate");
        
        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        
        setContentView(R.layout.bluetooth_item_comm);
        Log.d("[HG] BluetoothComm", "setContentView");
        
        Integer id = Integer.parseInt(getIntent().getStringExtra(BluetoothCommActivity.DEVICE_ID));
        Log.d("[HG] BluetoothComm", "got device: "+id);
        
        BluetoothManager btm = BluetoothManager.getInstance();
        if (btm != null) {
       		item = btm.getDevice(id);
       		if (item == null)
       			Log.d("[HG] BluetoothComm", "POS");
        } else {
        	Log.d("[HG] BluetoothComm", "well, crap");
        }
        Log.d("[HG] BluetoothComm", "got device: "+item.getName());
        
        if (customTitleSupported) {
        	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bluetooth_item_comm_titlebar);
        	Log.d("[HG] BluetoothComm", "requested custom feature");
        
        	final TextView titleText = (TextView) findViewById(R.id.title);
        	if (titleText != null) {
        		titleText.setText(item.getName()+": "+item.getAddress());
        		Log.d("[HG] BluetoothComm", "setting title text");
        	}
        }
        
        text = (TextView) findViewById(R.id.log);
        Log.d("[HG] BluetoothComm", "Created view");
        text.setText("Paired: '"+(item.isPaired() ? "Yes" : "No")+"'");
        
        Log.d("[HG] BluetoothComm", "About to create socket");
        listener = new BluetoothSocketListener(item, text);
        Log.d("[HG] BluetoothComm", "About to start the thread");
        listener.start();
        // Log.d("[HG] BluetoothComm", "Writing to socket");
        // listener.write("FOOBAR");
    }
}
