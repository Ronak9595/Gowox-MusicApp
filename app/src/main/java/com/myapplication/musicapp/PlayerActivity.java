package com.myapplication.musicapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    Button btnplay, btnnext, btnprev, btnff, btnfr;
    TextView txtname, txtstart, txtstop;
    SeekBar seekmusic;
    ImageView imageView;

    String sname;
    MediaPlayer mediaPlayer;
    int position;
    int[] songResIds = {
            R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4, R.raw.song5,
            R.raw.song6, R.raw.song7, R.raw.song8, R.raw.song9, R.raw.song10
    };
    String[] songNames = {
            "Song 1", "Song 2", "Song 3", "Song 4", "Song 5",
            "Song 6", "Song 7", "Song 8", "Song 9", "Song 10"
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnprev = findViewById(R.id.btnprev);
        btnnext = findViewById(R.id.btnnext);
        btnplay = findViewById(R.id.playbtn);
        btnff = findViewById(R.id.btnff);
        btnfr = findViewById(R.id.btnfr);
        txtname = findViewById(R.id.txtsn);
        txtstart = findViewById(R.id.txtstart);
        txtstop = findViewById(R.id.txtstop);
        seekmusic = findViewById(R.id.seekbar);
        imageView = findViewById(R.id.imageview);

        position = getIntent().getIntExtra("songResId", 0);
        sname = getIntent().getStringExtra("songName");
        txtname.setText(sname);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(getApplicationContext(), songResIds[position]);
        mediaPlayer.start();

        seekmusic.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnnext.performClick();
            }
        });

        updateSeekBar();

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    btnplay.setBackgroundResource(R.drawable.baseline_play);
                    mediaPlayer.pause();
                } else {
                    btnplay.setBackgroundResource(R.drawable.baseline_pause);
                    mediaPlayer.start();
                }
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position + 1) % songResIds.length;
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songResIds[position]);
                sname = songNames[position];
                txtname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.baseline_pause);
                startAnimation(imageView);
            }
        });

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position - 1 < 0) ? (songResIds.length - 1) : (position - 1);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songResIds[position]);
                sname = songNames[position];
                txtname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.baseline_pause);
                startAnimation(imageView);
            }
        });

        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                }
            }
        });

        btnfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });
    }

    private void updateSeekBar() {
        Thread updateSeekBarThread = new Thread() {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentPosition);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateSeekBarThread.start();

        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtstart.setText(createTime(mediaPlayer.getCurrentPosition()));
                txtstop.setText(createTime(mediaPlayer.getDuration()));
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void startAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration) {
        String time = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        time += min + ":";
        if (sec < 10) {
            time += "0";
        }
        time += sec;
        return time;
    }
}
