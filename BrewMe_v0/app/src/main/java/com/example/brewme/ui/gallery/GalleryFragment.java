package com.example.brewme.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.brewme.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

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

        return root;
    }

    private void UpdateData(GraphView graphView) throws InterruptedException {
        //Thread for updating data in real time
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {

        });
        graphView.addSeries(series);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(20);
        graphView.getViewport().setMinY(0.0);
        graphView.getViewport().setMaxY(10.0);
        int i = 0;
        while(i<20){
            Random rand = new Random();
            series.appendData(new DataPoint(i, rand.nextInt(10)), true, 50);
            graphView.addSeries(series);
            i++;
        }
    }
}