package net.hokiegeek.android.bluetooth;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class BluetoothManager {
    private enum DeviceEventType {
    	CREATE,
    	UPDATE,
    	DELETE
    }

	private int nextDeviceId = -1;
    private static BluetoothManager me = null;
    private static Context context = null;
    private BluetoothAdapter bluetoothAdapter = null;
    
    private List<BluetoothItem> devices;
    private List<BluetoothManagerListener> listeners;
    
    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                /*if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                	// TODO: flip some flag that states that it was found during discovery
                	addDevice(device, false);
                }*/
                addDevice(device, (device.getBondState() == BluetoothDevice.BOND_BONDED));
            //} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            }
        }
    };
    
    public BluetoothManager(Context c) {
    	context = c;
    	listeners = new ArrayList<BluetoothManagerListener>();
    	devices = new ArrayList<BluetoothItem>();
    	bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	me = this;
    	
    	start();
    }
    
    public static BluetoothManager getInstance() {
    	if (me == null)
    		me = new BluetoothManager(context);
    	return me;
    }
    
    public boolean start() { 	
        // Get local Bluetooth adapter
        if (bluetoothAdapter != null) {
            // Register for broadcasts when a device is discovered
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(broadcastReceiver, filter);

            // Register for broadcasts when discovery has finished
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            context.registerReceiver(broadcastReceiver, filter);
        	
            //filter = new IntentFilter(BluetoothDevice.);
            //context.registerReceiver(broadcastReceiver, filter);
            
        	if (bluetoothAdapter.isEnabled()) {
            	refreshDevices();
            	// TODO: put it on a timer
        	} else {
        		Log.e("[HG] BluetoothComm", "BT device is not on");
            	// If BT is not on, request that it be enabled
        		// TODO
        		// Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        		// context.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        	}
        } else {
        	Log.e("[HG] BluetoothComm", "No BT device available");
        }
        
        return true;
    }
    
    private void refreshDevices() {
    	// refreshPairedDevices();
    	initDiscovery();
    }

    private void initDiscovery() {
        // If we're already discovering, stop it
        if (bluetoothAdapter.isDiscovering()) {
        	bluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        bluetoothAdapter.startDiscovery();
    }
    
    private void refreshPairedDevices() {
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
            	addDevice(device, true);
            }
        }
    }
    
    /**
     * 
     * @param l
     */
    public void addListener(BluetoothManagerListener l) {
    	Log.v("[HG] BluetoothComm", "BluetoothManager: Adding a listener");
    	listeners.add(l);
    }
    
    /**
     * 
     * @param l
     */
    public void removeListener(BluetoothManagerListener l) {
    	listeners.remove(l);
    }
    
    /**
     * 
     * @param event
     * @param item
     */
    private void fireDeviceEvent(DeviceEventType event, BluetoothItem item) {
    	Log.v("[HG] BluetoothComm", "fireDeviceEvent: Alerting "+listeners.size()+" listeners");
    	for (BluetoothManagerListener l : listeners) {
    		switch (event) {
    		case CREATE: l.deviceAdded(item); break;
    		case DELETE: l.deviceRemoved(item); break;
			default: break;
    		}
    	}
    }
    
    /**
     * 
     * @param device
     * @param paired
     */
    private void addDevice(BluetoothDevice device, boolean paired) {
    	if (device == null) return;
    	
    	BluetoothItem item = new BluetoothItem(getNextDeviceId(), device, paired);
    	devices.add(item);
    	fireDeviceEvent(DeviceEventType.CREATE, item);
    	
    	Log.d("[HG] BluetoothComm", "BluetoothManager: Adding a new BT device: "+item.getId());
    }
    
    /**
     * 
     * @return
     */
    private int getNextDeviceId() {
		return ++nextDeviceId;
	}

	/**
     * 
     * @param device
     */
    private void removeDevice(BluetoothDevice device) {
    	BluetoothItem item = devices.get(devices.indexOf(device));
    	if (devices.remove(device))
    		fireDeviceEvent(DeviceEventType.DELETE, item);
    }
    
    /**
     * 
     * @return
     */
    public List<BluetoothItem> getDevices() {
    	return devices;
    }
    
    /**
     * 
     * @param id
     * @return
     */
    public BluetoothItem getDevice(int id) {
    	Log.d("[HG] BluetoothComm", "Have '"+devices.size()+"' devices");
    	for (BluetoothItem item : devices) {
    		Log.d("[HG] BluetoothComm", "Looking for item '"+id+"': "+item.getId());
    		if (item.getId() == id) {
    			return item;
    		}
    	}
    	return null;
    }
}