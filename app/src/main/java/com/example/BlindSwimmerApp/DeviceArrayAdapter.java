package com.example.BlindSwimmerApp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.IDevice;

import java.util.List;

public class DeviceArrayAdapter extends ArrayAdapter<IDevice> {

    public DeviceArrayAdapter(Context context, List<IDevice> deviceList) {
        super(context, R.layout.device_item_layout, deviceList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.device_item_layout, parent, false);

        TextView nameView = rowView.findViewById(R.id.deviceName);
        TextView infoView = rowView.findViewById(R.id.deviceInfo);
        IDevice device = this.getItem(position);
        String name = device.getName();
        nameView.setText(name == null ? "Unknown" : name);
        infoView.setText(device.getName() + ", " + device.getAddress());
        return rowView;
    }
}
