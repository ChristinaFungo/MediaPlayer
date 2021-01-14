package com.khrystynahryb.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.slider.Slider;
import com.khrystynahryb.mediaplayer.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MusicAdapter.OnMusicClickListener{
    private List<Music> musicList=Music.getList();
    private ActivityMainBinding binding;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private int cursor;
    private MusicAdapter musicAdapter;
    private boolean isDragging;
    private MusicState musicState= MusicState.STOPPED;

    private float currentPosition = 8;
    private float videoState = 0;

    @Override
    public void onClick(Music music, int position) {
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
        cursor=position;
        onMusicChange(musicList.get(cursor));
    }

    enum MusicState{
        PLAYING,PAUSED,STOPPED
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RecyclerView recyclerView=findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        musicAdapter=new MusicAdapter(musicList,this);
        recyclerView.setAdapter(musicAdapter);
        onMusicChange(musicList.get(0));
        if (savedInstanceState !=null){
            videoState = savedInstanceState.getFloat("key");
            mediaPlayer.seekTo((int)videoState);
        }

        binding.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (musicState){
                    case PLAYING:
                        mediaPlayer.pause();
                        musicState= MusicState.PAUSED;
                        binding.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        break;
                    case PAUSED:
                    case STOPPED:
                        mediaPlayer.start();
                        musicState= MusicState.PLAYING;
                        binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                }
            }
        });
        binding.musicSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                binding.positionTv.setText(Music.convertMillisToString((long) value));
            }
        });
        binding.musicSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                isDragging=true;
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                isDragging=false;
                mediaPlayer.seekTo((int) slider.getValue());

            }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext();
            }
        });
        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPrev();
            }
        });
    }
    private void onMusicChange(Music music){
        musicAdapter.notifyMusicChange(music);
        binding.musicSlider.setValue(0);
        mediaPlayer=MediaPlayer.create(this,music.getMusicFileResId());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isDragging)
                                    binding.positionTv.setText(Music.convertMillisToString(mediaPlayer.getCurrentPosition()));
                                binding.musicSlider.setValue(mediaPlayer.getCurrentPosition());
                            }
                        });
                    }
                },1000,1000);
                binding.durationTv.setText(Music.convertMillisToString(mediaPlayer.getDuration()));
                binding.musicSlider.setValueTo(mediaPlayer.getDuration());
                musicState= MusicState.PLAYING;
                binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        goNext();

                    }
                });
            }
        });

        binding.coverIv.setActualImageResource(music.getCoverResId());
        binding.artistIv.setActualImageResource(music.getArtistResId());
        binding.artistTv.setText(music.getArtist());
        binding.musicNameTv.setText(music.getName());
    }


    private void goNext(){
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
        if (cursor<musicList.size()-1){
            cursor++;
        }else
            cursor=0;
        onMusicChange(musicList.get(cursor));
    }
    private void goPrev(){
        timer.cancel();
        timer.purge();
        mediaPlayer.release();
        if (cursor==0){
            cursor=musicList.size()-1;
        }else
            cursor--;
        onMusicChange(musicList.get(cursor));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "start",Toast.LENGTH_SHORT).show();
        mediaPlayer.seekTo((int)currentPosition);
        mediaPlayer.start();
        binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "play",Toast.LENGTH_SHORT).show();
        mediaPlayer.seekTo((int)currentPosition);
        mediaPlayer.start();
        binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "pause",Toast.LENGTH_SHORT).show();
        currentPosition=mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
        binding.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        currentPosition=binding.musicSlider.getValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "stop",Toast.LENGTH_SHORT).show();
        mediaPlayer.pause();
        binding.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
        currentPosition=binding.musicSlider.getValue();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        float state=currentPosition;
        outState.putFloat("key", state);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentPosition=binding.musicSlider.getValue();
    }
}