package net.hokiegeek.android.btcomm;

import java.util.ArrayList;

import net.hokiegeek.android.bluetooth.BluetoothItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DeviceArrayAdapter extends ArrayAdapter<BluetoothItem> {
	public DeviceArrayAdapter(Context context, ArrayList<BluetoothItem> devices) {
		super(context, android.R.layout.two_line_list_item, devices);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(android.R.layout.two_line_list_item, parent, false);
		
		BluetoothItem device = (BluetoothItem)getItem(position);
		
		if (device == null) {
			return rowView;
		}
		
		// TextView nameTextView = (TextView) rowView.findViewById(R.id.deviceName);
		TextView nameTextView = (TextView) rowView.findViewById(android.R.id.text1);
		String name = "";
		if (device.isPaired()) name = "*";
		name += device.getName();
		nameTextView.setText(name);
		// nameTextView.setText(device.getName());
		
		// TextView addressTextView = (TextView) rowView.findViewById(R.id.deviceAddress);
		TextView addressTextView = (TextView) rowView.findViewById(android.R.id.text2);
		addressTextView.setText(device.getAddress());
 
		return rowView;
	}
}
