package com.zee.suhaatechadproject;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zee.suhaatechadproject.databinding.ActivitySetPrefValueBinding;
import com.zee.suhaatechadslibmodule.limits.TruePrefUtils;

public class SetPrefValueAct extends AppCompatActivity {

    ActivitySetPrefValueBinding binding;
    String unitIdMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetPrefValueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.updateBtn.setOnClickListener(view -> {
            limitationFun(this, "ca-app-pub-3940256099942544/1033173712", true, true,
                    Integer.parseInt(binding.clickET.getText().toString()), Integer.parseInt(binding.impressionsET.getText().toString()), Long.valueOf(binding.delayMsET.getText().toString()),
                    Integer.parseInt(binding.banHoursET.getText().toString()), true);
        });
    }

    public void limitationFun(
            Context context,
            String unitId,
            Boolean limitActivated,
            Boolean adActivated,
            int clicks,
            int impressions,
            Long delayMs,
            int banHours,
            Boolean hideOnClick
    ) {
        unitIdMain = unitId;
        if (unitIdMain.contains("/")) {
            unitIdMain = unitIdMain.substring(unitIdMain.lastIndexOf("/") + 1);
        } else if (unitIdMain.contains("_")) {
            unitIdMain = unitIdMain.substring(unitIdMain.lastIndexOf("_") + 1);
        }
        TruePrefUtils.getInstance().init(context, TruePrefUtils.PREF_NAME)
                .zUpdateNetworksData();
        TruePrefUtils.getInstance().init(context, unitIdMain).zUpdateUnitsData(
                limitActivated,
                adActivated,
                clicks,
                impressions,
                delayMs,
                banHours,
                hideOnClick
        );
        Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}