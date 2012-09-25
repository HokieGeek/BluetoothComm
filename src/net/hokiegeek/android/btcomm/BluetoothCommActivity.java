/**
 * 
 */
package net.hokiegeek.android.btcomm;

import java.util.ArrayList;
import java.util.List;

import net.hokiegeek.android.bluetooth.BluetoothItem;
import net.hokiegeek.android.bluetooth.BluetoothManager;
import net.hokiegeek.android.bluetooth.BluetoothManagerListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author andres
 *
 */
public class BluetoothCommActivity extends Activity implements BluetoothManagerListener {
	
	public final static String DEVICE_ID = "net.hokiegeek.android.btcomm.DEVICEID";

	private List<BluetoothItem> devices;
	private DeviceArrayAdapter dataAdapter;
	private BluetoothManager bluetooth;
	private ListView devicesView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: indeterminate thingy while in discovery
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		
		
		setContentView(R.layout.main);
		
		setProgressBarIndeterminateVisibility(true);
 
		// Create the view and add the adapter
		devices = new ArrayList<BluetoothItem>();
		dataAdapter = new DeviceArrayAdapter(this, (ArrayList<BluetoothItem>)devices);
		
		devicesView = (ListView) findViewById(R.id.list);
		devicesView.setEmptyView(findViewById(R.id.empty));
        devicesView.setAdapter(dataAdapter);
        
        devicesView.setOnItemClickListener(new OnItemClickListener() {
        	  //@Override
        	  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		  // TODO: need to check bounds
        		  BluetoothItem item = devices.get(position);
        		  
        		  viewItem(item);
        		  
        		  Toast.makeText(getApplicationContext(),
        				  "Selected BT Item: " + item.getName(), Toast.LENGTH_LONG)
        			   .show();
        	  }
        });
        
		bluetooth = new BluetoothManager(getApplicationContext());
		bluetooth.addListener(this);
		
		List<BluetoothItem> currentDevices = bluetooth.getDevices();
		if (currentDevices.size() > 0) {
			devices.addAll(currentDevices);
			dataAdapter.notifyDataSetChanged();
		}
		
	}

	public void deviceAdded(BluetoothItem item) {
		Toast.makeText(getApplicationContext(),
				  "Found device: " + item.getName(), Toast.LENGTH_LONG)
			   .show();
		
		devices.add(item);
		dataAdapter.notifyDataSetChanged();
	}

	public void deviceRemoved(BluetoothItem item) {
		devices.remove(item);
		dataAdapter.notifyDataSetChanged();
	}
	
	private void viewItem(BluetoothItem item) {
		Intent btIntent = new Intent(this, BluetoothItemComm.class);
	    btIntent.putExtra(DEVICE_ID, item.getId().toString());
	    startActivity(btIntent);
	}
}
