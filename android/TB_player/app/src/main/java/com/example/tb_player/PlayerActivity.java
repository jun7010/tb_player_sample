package com.example.tb_player;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity{

    private PlayerView localPlayerView;
    private ExoPlayer exoPlayer;

    private Button captureButton;
    private Button clearButton;

    private RecyclerView recyclverView;
    private ArrayList<RecyclerViewItem> mList;
    private RecyclerViewAdapter mAdapter;
    PlayerActivity activity;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("Process", "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        localPlayerView = findViewById(R.id.local_player_view);

        captureButton = findViewById(R.id.exo_capture);
        clearButton = findViewById(R.id.exo_clear);
        activity = this;


        recyclverView = (RecyclerView) findViewById(R.id.player_recyclerView);
        mList = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(mList);
        recyclverView.setAdapter(mAdapter);
        recyclverView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

    }

    public void setJson(String json) {
        Log.d("Process", "set json : " + json);
        if(json != null && json.length() > 0) {
            createListFromJson(json);
        }
    }

    private void addItem(String image, String mainText, String subText, String link_url) {
        RecyclerViewItem item = new RecyclerViewItem();
        item.setImgName(image);
        item.setMainText(mainText);
        item.setSubText(subText);
        item.setmLinkUrl(link_url);

        mList.add(item);
    }

    private void clearItems() {
        mList.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        if(exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }


    private void initPlayer() {
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        localPlayerView.setPlayer(exoPlayer);

        if(getIntent().getStringExtra("play_url") == null) {
            Log.d("Process", "Wrong URL");
            Toast.makeText(getApplicationContext(), "잘못된 URL입니다.", Toast.LENGTH_LONG).show();
        } else {
            String url = getIntent().getStringExtra("play_url").toString();
            Log.d("Process", "URL : " + url);
            try {
                MediaItem mediaItem = new MediaItem.Builder()
                        .setUri(url)
                        .setMediaMetadata(new com.google.android.exoplayer2.MediaMetadata.Builder()
                                .setTitle("TB AI 샘플")
                                .build())
                        .setMimeType(MimeTypes.VIDEO_MP4).build();
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.seekTo(0, 0);
                exoPlayer.prepare();
                exoPlayer.setPlayWhenReady(true);
                Log.d("Process", "exoplayer play");
            } catch (Exception e) {
                Log.d("Process", "exoplayer Exception");
                Log.d("Process", e.getMessage());

            }

        }


        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearItems();
                Toast.makeText(getApplicationContext(), "캡쳐되었습니다 : " + exoPlayer.getCurrentPosition(), Toast.LENGTH_SHORT).show();
                if ((TextureView) localPlayerView.getVideoSurfaceView() == null) {
                    Log.d("Process", "view is null");
                }
                ScreenCaptureUtil.captureAndSave(activity, (TextureView) localPlayerView.getVideoSurfaceView());

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearItems();
            }
        });
    }


    private void createListFromJson(String jsonResponse) {
        try {
            JSONArray jsonList = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject jsonObject = jsonList.getJSONObject(i);

                String imgName = jsonObject.getString("img_name");
                String mainText = jsonObject.getString("text1");
                String subText = jsonObject.getString("text2");
                String link_url = jsonObject.getString("link_url");

                addItem(imgName, mainText, subText, link_url);
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            Log.d("Process", e.getMessage());
            e.printStackTrace();
        }
    }
}
