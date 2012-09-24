package net.hokiegeek.android.btcomm;

import net.hokiegeek.android.bluetooth.BluetoothItem;
import net.hokiegeek.android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
	
public class BluetoothItemComm extends Activity {
	private BluetoothItem item = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        
        setContentView(R.layout.bluetooth_item_comm);
        
        Integer id = -1;
        Intent intent = getIntent();
        if (intent != null)
        	id = Integer.parseInt(intent.getStringExtra(BluetoothCommActivity.DEVICE_ID));
        BluetoothManager btm = BluetoothManager.getInstance();
        
        if (btm != null) {
       		item = btm.getDevice(id);
        } else {
        	Log.d("[HG] BIC", "well, crap");
        }
        
        if (customTitleSupported) {
        	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.bluetooth_item_comm_titlebar);
        
        	final TextView titleText = (TextView) findViewById(R.id.title);
        	if (titleText != null) {
        		titleText.setText(item.getName()+": "+item.getAddress());
        	}
        }
        
        
        // TODO: perform a socket connection and pipe the socket's input (output?) stream into a TextView
        
        
        final TextView test = (TextView) findViewById(R.id.whatever);
        if (test != null) {
            test.setText("Paired: '"+(item.isPaired() ? "Yes" : "No")+"'");
        }
    }
}
