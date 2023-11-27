package com.example.butcheryapp_ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EtalaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EtalaseFragment extends Fragment {

    RelativeLayout etalaseList001, etalaseList002, etalaseList003;

    public EtalaseFragment() {
        // Required empty public constructor
    }

    public static EtalaseFragment newInstance() {
        EtalaseFragment fragment = new EtalaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etalase, container, false);
//        etalaseList001 = view.findViewById(R.id.etalase_list001);
//        etalaseList002 = view.findViewById(R.id.etalase_list002);
//        etalaseList003 = view.findViewById(R.id.etalase_list003);


        return view;
    }
}