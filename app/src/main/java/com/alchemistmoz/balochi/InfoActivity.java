package com.alchemistmoz.balochi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alchemistmoz.balochi.misc.CustomToolbar;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_info_activity);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(InfoActivity.this);
        toolbar.initCategoryToolbar();
    }
}
