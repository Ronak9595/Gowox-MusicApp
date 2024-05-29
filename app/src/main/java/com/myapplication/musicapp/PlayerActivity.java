package com.myapplication.musicapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
     Button btnplay, btnnext, btnprev,btnff,btnfr;
     TextView txtname, txtstart, txtstop;
     SeekBar seekmusic;
     public static final String EXTRA_NAME = "song_name";
     static MediaPlayer mediaPlayer;
     int position;
     ArrayList<File> mySongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    btnprev=findViewById(R.id.btnprev);
    btnnext=findViewById(R.id.btnnext);
    btnplay=findViewById(R.id.playbtn);
    btnff=findViewById(R.id.btnff);
    btnfr=findViewById(R.id.btnfr);
    txtname=findViewById(R.id.txtsn);
    txtstart=findViewById(R.id.txtstart);
    txtstop=findViewById(R.id.txtstop);
    seekmusic=findViewById(R.id.seekbar);

    if(mediaPlayer !=null)
    {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    Intent i=getIntent();
    Bundle bundle=i.getExtras();

    mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
    String songName=i.getStringExtra("songname");
    position=bundle.getInt("pos",0);
    Uri uri =Uri.parse(mySongs.get(position).toString());
    sname=mySongs.get(position).getName();
    txtname.setText(sname);

    mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
    mediaPlayer.start();

    btnplay.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            if (mediaPlayer.isPlaying())
            {
                btnplay.setBackgroundResource(R.drawable.baseline_play);
                mediaPlayer.pause();
            }
            else
            {
                btnplay.setBackgroundResource(R.drawable.baseline_pause);
                mediaPlayer.start();
            }
        }
    });
    }
}