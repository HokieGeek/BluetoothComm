/**
 * 
 */
package net.hokiegeek.android.bluetooth;

/**
 * @author andres
 *
 */
public interface BluetoothManagerListener {
	public void deviceAdded(BluetoothItem item);
	public void deviceRemoved(BluetoothItem item);
}
