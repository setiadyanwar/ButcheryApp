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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TokoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TokoFragment extends Fragment {

    public TokoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TokoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TokoFragment newInstance(String param1, String param2) {
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

        return view;
    }
}