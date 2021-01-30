package com.amitthakare.camerascanner;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;

public class PageSetting extends AppCompatActivity {

    DrawerLayout drawerLayoutPageSetting;
    Toolbar toolbarPageSetting;

    RadioButton poor, normal, best;
    RadioGroup optionsQuality;

    SharedPreferences pageSetting;
    SharedPreferences.Editor editor;

    EditText left, top, right, bottom;

    Button setMarginBtn;
    String checked="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_setting);
        ini();
        checkOptionRadio();
        getPageSetting("Normal");
    }

    private void checkOptionRadio() {
        optionsQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.poorPageSet) {
                    getPageSetting("Poor");
                    checked = "Poor";
                } else if (id == R.id.normalPageSet) {
                    getPageSetting("Normal");
                    checked = "Normal";
                } else if (id == R.id.bestPageSet) {
                    getPageSetting("Best");
                    checked = "Best";
                }
            }
        });
    }

    private void setPageSetting(int l, int t, int r, int b, String quality) {
        editor.putInt(quality+"Start",l);
        editor.putInt(quality+"End",r);
        editor.putInt(quality+"Top",t);
        editor.putInt(quality+"Bottom",b);
        if (editor.commit())
        {
            Toast.makeText(this, "Page Setting Updated!", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }

    }

    private void getPageSetting(String quality) {
        left.setText(pageSetting.getInt(quality + "Start", -1) + "");
        top.setText(pageSetting.getInt(quality + "Top", -1) + "");
        right.setText(pageSetting.getInt(quality + "End", -1) + "");
        bottom.setText(pageSetting.getInt(quality + "Bottom", -1) + "");
    }

    private void ini() {
        //------------General Hooks---------//
        poor = findViewById(R.id.poorPageSet);
        normal = findViewById(R.id.normalPageSet);
        best = findViewById(R.id.bestPageSet);
        optionsQuality = findViewById(R.id.qualityPageSet);
        pageSetting = getSharedPreferences("pageSetting", MODE_PRIVATE);
        editor = pageSetting.edit();
        left = findViewById(R.id.marginLeft);
        top = findViewById(R.id.marginTop);
        right = findViewById(R.id.marginRight);
        bottom = findViewById(R.id.marginBottom);
        setMarginBtn = findViewById(R.id.serMarginBtn);
        setMarginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int l = Integer.parseInt(left.getText().toString());
                int t = Integer.parseInt(top.getText().toString());
                int r = Integer.parseInt(right.getText().toString());
                int b = Integer.parseInt(bottom.getText().toString());

                setPageSetting(l,t,r,b,checked);
            }
        });

        //------------Hooks-------------//
        drawerLayoutPageSetting = findViewById(R.id.drawerLayoutPageSetting);
        toolbarPageSetting = findViewById(R.id.navigationToolbarPageSetting);

        //---------Toolbar---------// set toolbar as action bar
        setSupportActionBar(toolbarPageSetting);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Pdf Page Setting");

        toolbarPageSetting.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        toolbarPageSetting.setTitleTextColor(getResources().getColor(R.color.white));

        //--------Navigation Toggle--------//
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PageSetting.this, drawerLayoutPageSetting, toolbarPageSetting, R.string.open, R.string.close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_24);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //drawerLayoutStudent.addDrawerListener(toggle);
        toggle.syncState();
    }
}