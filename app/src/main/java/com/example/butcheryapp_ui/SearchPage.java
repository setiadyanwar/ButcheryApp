package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SearchPage extends AppCompatActivity {

    RadioButton Viewsemua, Viewterakhir, Viewpopuler, Viewbersertifikat, Viewtermurah;
    RadioGroup sectionsNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        Viewsemua = findViewById(R.id.semua);
        Viewterakhir = findViewById(R.id.terakhir);
        Viewpopuler = findViewById(R.id.populer);
        Viewbersertifikat = findViewById(R.id.bersertifikat);
        Viewtermurah = findViewById(R.id.termurah);
        sectionsNav = findViewById(R.id.sections_nav);

        Fragment semuaFragment = new SemuaFragment();
        Fragment terakhirFragment = new TerakhirFragment();
        Fragment populerFragment = new PopulerFragment();
        Fragment bersertifikatFragment = new BersertifikatFragment();
        Fragment termurahFragment = new TermurahFragment();

        addFragment(semuaFragment, "SemuaFragment");

        sectionsNav.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.semua:
                    toggleFragment(semuaFragment);
                    break;
                case R.id.terakhir:
                    toggleFragment(terakhirFragment);
                    break;
                case R.id.populer:
                    toggleFragment(populerFragment);
                    break;
                case R.id.bersertifikat:
                    toggleFragment(bersertifikatFragment);
                    break;
                case R.id.termurah:
                    toggleFragment(termurahFragment);
                    break;
            }
        });
    }

    private void addFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.toko_views_fragment_container, fragment, tag);
            fragmentTransaction.commit();
        }
    }

    private void toggleFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.toko_views_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
