package com.cebbus.slicegame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;


public class GameMain extends Activity {

    public boolean sound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_main);

        addListenerOnButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void addListenerOnButton() {
        View infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> {
            Dialog informationDialog = new Dialog(GameMain.this);
            informationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            informationDialog.setContentView(R.layout.info);
            informationDialog.show();
        });

        View startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            Intent gameInt = new Intent(GameMain.this, GameActivity.class);
            gameInt.putExtra("sound", sound);
            startActivity(gameInt);
        });

        final View soundOnButton = findViewById(R.id.soundOnButton);
        final View soundOffButton = findViewById(R.id.soundOffButton);
        soundOnButton.setOnClickListener(v -> {
            sound = false;
            soundOffButton.setVisibility(View.VISIBLE);
            soundOnButton.setVisibility(View.GONE);
        });

        soundOffButton.setOnClickListener(v -> {
            sound = true;
            soundOffButton.setVisibility(View.GONE);
            soundOnButton.setVisibility(View.VISIBLE);
        });

    }
}
