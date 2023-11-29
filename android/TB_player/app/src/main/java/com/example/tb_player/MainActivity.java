package com.example.tb_player;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button playButton;
    private Button sampleButtion;
    private EditText textUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        playButton = findViewById(R.id.button_url);
        sampleButtion = findViewById(R.id.button_sample);
        textUrl = findViewById(R.id.text_url);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = textUrl.getText().toString();
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("play_url", url);
                startActivity(intent);
            }
        });

        sampleButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("play_url", url);
                startActivity(intent);
            }
        });
    }
}