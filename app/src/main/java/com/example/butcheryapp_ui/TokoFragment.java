package com.example.butcheryapp_ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class TokoFragment extends Fragment {

    public TokoFragment() {
        // Required empty public constructor
    }

    public static TokoFragment newInstance() {
        TokoFragment fragment = new TokoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toko, container, false);

        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner1,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner2,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner3,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner4,null, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels);

        return view;
    }
}