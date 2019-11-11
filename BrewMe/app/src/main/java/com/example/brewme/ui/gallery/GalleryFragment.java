package com.example.brewme.ui.gallery;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.brewme.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    final byte delimiter = 33;
    int readBufferPosition = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plot, container, false);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //do something
            }
        });

        final GraphView graph = root.findViewById(R.id.graph);

        try {
            UpdateData(graph);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Button btButton = root.findViewById(R.id.btButton);
        btButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                BTConnect();
            }
        });

        return root;
    }

    private void UpdateData(GraphView graphView) throws InterruptedException {
        //Thread for updating data in real time
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

        });
        graphView.addSeries(series);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(20);
        graphView.getViewport().setMinY(0.0);
        graphView.getViewport().setMaxY(10.0);
        int i = 0;
        while (i < 20) {
            Random rand = new Random();
            series.appendData(new DataPoint(i, rand.nextInt(10)), true, 50);
            graphView.addSeries(series);
            i++;
        }
    }


    //Unable to get this to work - connecting bluetooth manually for now
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void BTConnect() {

        final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        Log.d("BT", "UUID");
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket socket = null;
        String RPi_MAC = "B8:27:EB:4D:09:F5";

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {

            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equals(RPi_MAC)) {
                    try {
                        socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                    } catch (IOException e0) {
                        Log.d("BT_TEST", "Cannot create socket");
                        e0.printStackTrace();
                    }

                    try {
                        socket.connect();
                    } catch (IOException e1) {
                        try {
                            socket.close();
                            Log.d("BT_TEST", "Cannot connect");
                            e1.printStackTrace();
                        } catch (IOException e2) {
                            Log.d("BT_TEST", "Socket not closed");
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}