/**
 * 
 */
package net.hokiegeek.android.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * @author andres
 *
 */
public class BluetoothItem {	
	private BluetoothDevice device;
	private int id;
	private boolean paired;
	
	private String n;
	private String a;

	/**
	 * @param id TODO
	 * @param device TODO
	 */
	public BluetoothItem(int id, BluetoothDevice device) {
		this(id, device, false);
	}

	/**
	 * 
	 * @param id TODO
	 * @param device TODO
	 * @param paired TODO
	 */
	public BluetoothItem(int id, BluetoothDevice device, boolean paired) {
		this.id = id;
		this.device = device;
		this.paired = paired;
		
		if (this.device == null) {
			this.n = "foo";
			this.a = "bar";
		}
	}
	
	public Integer getId() {
		return id;
	}
	
	/**
	 * @return TODO
	 */
	public String getName() {
		if (device != null)
			return device.getName();
		else
			return n;
	}

	/**
	 * @return TODO
	 */
	public String getAddress() {
		if (device != null)
			return device.getAddress();
		else
			return a;
	}
	
	/**
	 * @return TODO
	 */
	public boolean isPaired() {
		return paired;
	}
	
	/**
	 * 
	 * @return
	 */
	public BluetoothSocket createSocket(UUID uuid) throws IOException {
		return device.createRfcommSocketToServiceRecord(uuid);
	}
}
