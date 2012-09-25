package net.hokiegeek.android.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.TextView;

public class BluetoothSocketListener extends Thread {
	private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private BluetoothItem item = null;
	private BluetoothSocket socket = null;
	private TextView view = null;
	private InputStream in = null;
    private OutputStream out = null;
	
	public BluetoothSocketListener(BluetoothItem item, TextView view) {
		this.item = item;
		this.view = view;
		
		try {
			this.socket = this.item.createSocket(SPP_UUID);
			
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
			Log.d("[HG] BluetoothComm", "Running listener");
			if (this.socket == null)
				return;
			
			Log.d("[HG] BluetoothComm", "Connecting...");
            this.socket.connect();

            if (this.view != null) {
            	byte[] buffer = new byte[1024];  // buffer store for the stream
            	int bytes; // bytes returned from read()
            
            	// Keep listening to the InputStream until an exception occurs
            	while (true) {
            		try {
            			bytes = this.in.read(buffer);
            			this.view.setText(bytes);
            		} catch (IOException e) {
            			break;
            		}
            	}
            }
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                this.socket.close();
            } catch (IOException closeException) { }
            return;
        }
	}
	
	public void write(String msg) {
		Log.d("[HG] BluetoothComm", "Converting String to bytes: "+msg);
		write(msg.getBytes());
	}
	
	public void write(byte[] bytes) {
		Log.d("[HG] BluetoothComm", "Writing to output stream");
		try {
            this.out.write(bytes);
        } catch (IOException e) { 
        	Log.e("[HG] BluetoothComm", "Could not write to socket", e);
        }
	}
	
	public void cancel() {
        try {
        	this.socket.close();
        } catch (IOException e) { }
    }
}
