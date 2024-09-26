package com.nsx.sest.init_page;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nsx.sest.R;
import com.nsx.sest.databinding.ActivityInitBinding;
import com.nsx.sest.databinding.ActivitySetupBinding;

public class InitActivity extends Activity {

    private ActivityInitBinding binding;
    private final String[]      messages = {
            "Welcome :)\nI Hope You Doing Well\nFrom Today You Will Not Care About Viruses\nPlease Let Me Show You \n-My App-\n-How Does It Works-\n...\nPress Continue",
            "This App Was Made\n To Make Your Storage Secure\nFrom 3rd Person..",
            "BETA BETA\nBETA BLA jajsjdj\nBLA BLA BLA :) :) :)",
            "BETA BETA\nBETA BLA asdasd\nBLA BLA BLA\nSo Enjoy :)"
    };
    private int                 message_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInitBinding.inflate(getLayoutInflater());
        this.setContentView(binding.getRoot());
//        ShowAnimatedMessage();
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAnimatedMessage();
            }
        });
    }
    Thread ShowAnimatedMessage()
    {
        binding.continueBtn.setVisibility(View.GONE);
        Thread t = new Thread(
                new Runnable() {
                    final Handler handler = new Handler(getMainLooper());
                    @Override
                    public void run() {
                        if (message_id >= messages.length) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.message.setText("");
                                    Toast.makeText(InitActivity.this, "Enjoy...", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return ;
                        }
                        for (int i = 0;i < messages[message_id].length(); i++) {
                            int finalI = i;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.message.setText(messages[message_id].substring(0, finalI +1));
                                }
                            });
                            try {
                                if (messages[message_id].charAt(finalI) == '\n')
                                    Thread.sleep(250);
                                else
                                    Thread.sleep(30);
                            }
                            catch (Exception e) {}
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                message_id++;
                                binding.continueBtn.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
        );
        t.start();
        return  t;
    }
}