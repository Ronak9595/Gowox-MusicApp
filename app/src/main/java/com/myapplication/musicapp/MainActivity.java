package com.myapplication.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] songNames = {
            "Song 1", "Song 2", "Song 3", "Song 4", "Song 5",
            "Song 6", "Song 7", "Song 8", "Song 9", "Song 10"
    };
    int[] songResIds = {
            R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4, R.raw.song5,
            R.raw.song6, R.raw.song7, R.raw.song8, R.raw.song9, R.raw.song10
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewSong);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("songResId", i);
                intent.putExtra("songName", songNames[i]);
                startActivity(intent);
            }
        });
    }
}
