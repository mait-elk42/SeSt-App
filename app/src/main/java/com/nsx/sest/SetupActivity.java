package com.nsx.sest;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.nsx.sest.PathPicker.PathPicker;
import com.nsx.sest.databinding.ActivitySetupBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SetupActivity extends Activity {
    private ActivitySetupBinding binding;
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupBinding.inflate(getLayoutInflater());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                binding.permissionRequest.setVisibility(View.VISIBLE);
                binding.main.setVisibility(View.GONE);
                setContentView(binding.getRoot());
                binding.grant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                    }
                });
            }else{
                Execute();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Execute();
            }
        }
    }
    private void Execute()
    {
        binding.permissionRequest.setVisibility(View.GONE);
        binding.main.setVisibility(View.VISIBLE);
        setContentView(binding.getRoot());
        binding.howtouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://mbasic.facebook.com/story.php?story_fbid=pfbid02g4EDqgXU9n1yLt2dJu5mtN5kSMdGqcinwxuEEihv3iQx6susyagd8pAK2RG2ptKYl&id=100025154777224")));
            }
        });
        binding.pathSetup.setOnClickListener(view -> {
            Intent i = new Intent();
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setClass(this, PathPicker.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        });
    }
}