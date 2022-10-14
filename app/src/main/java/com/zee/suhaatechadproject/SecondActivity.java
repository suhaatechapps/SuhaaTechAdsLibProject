package com.zee.suhaatechadproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.zee.suhaatechadproject.databinding.ActivitySecondBinding;
import com.zee.suhaatechadslibmodule.mediation.TrueAdManager;

public class SecondActivity extends AppCompatActivity {
    ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}