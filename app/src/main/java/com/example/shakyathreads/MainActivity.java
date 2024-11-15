package com.example.shakyathreads;

import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.shakyathreads.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int rabbitProgress = 0, turtleProgress = 0;

    private final Handler handler = new Handler(Looper.myLooper(), message -> {
        switch (message.what) {
            case 1:
                binding.sbRabbit.setProgress(rabbitProgress);
            case 2:
                binding.sbTurtle.setProgress(turtleProgress);
        }

        if (rabbitProgress >= 100 && turtleProgress < 100) {
            Toast.makeText(MainActivity.this, "兔子勝", Toast.LENGTH_SHORT).show();
            binding.btnStart.setEnabled(true);
        } else if (turtleProgress >= 100 && rabbitProgress < 100) {
            Toast.makeText(MainActivity.this, "烏龜勝", Toast.LENGTH_SHORT).show();
            binding.btnStart.setEnabled(true);
        }

        return false;
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnStart.setOnClickListener(view -> {
            binding.btnStart.setEnabled(false);
            rabbitProgress = turtleProgress = 0;
            binding.sbRabbit.setProgress(0);
            binding.sbTurtle.setProgress(0);
            runRabbit();
            runTurtle();
        });
    }

    private void runRabbit() {
        new Thread(() -> {
            while (rabbitProgress <= 100 && turtleProgress < 100) {
                try {
                    Thread.sleep(100);
                    if (Math.random() > ((double) 1 / 3)) {
                        Thread.sleep(300);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                rabbitProgress += 3;
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void runTurtle() {
        new Thread(() -> {
            while (turtleProgress <= 100 && rabbitProgress < 100) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                turtleProgress++;
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
