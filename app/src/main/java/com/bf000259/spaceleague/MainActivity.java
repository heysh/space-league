package com.bf000259.spaceleague;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private int level;
    private String name;

    protected static void hideNavigationBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    protected static void animateBackground(Activity activity) {
        final ImageView bg1 = (ImageView) activity.findViewById(R.id.backgroundOne);
        final ImageView bg2 = (ImageView) activity.findViewById(R.id.backgroundTwo);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = bg1.getWidth();
                final float translationX = width * progress;

                bg1.setTranslationX(-translationX);
                bg2.setTranslationX(-(translationX - width));
            }
        });
        animator.start();
    }

    private void launchGame() {
        Intent play = new Intent(MainActivity.this, GameActivity.class);
        play.putExtra("level", level);
        play.putExtra("name", name);
        startActivity(play);
    }

    private void selectDifficultyDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("SELECT DIFFICULTY");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "EASY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                level = 1;
                launchGame();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "MEDIUM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                level = 2;
                launchGame();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "HARD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                level = 3;
                launchGame();
            }
        });

        alertDialog.show();

        Button easy = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        Button medium = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button hard = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) hard.getLayoutParams();
        layoutParams.weight = 10;
        easy.setLayoutParams(layoutParams);
        medium.setLayoutParams(layoutParams);
        hard.setLayoutParams(layoutParams);
    }

    private void updateEnterNameText() {
        TextView enterName = findViewById(R.id.enterName);
        enterName.setText(name);
    }

    private void saveNameLocally() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", name);
        editor.apply();
    }

    private void enterNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter your name");

        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = input.getText().toString();
                updateEnterNameText();
                saveNameLocally();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showHighScores() {
        Intent highScores = new Intent(MainActivity.this, HighScoresActivity.class);
        startActivity(highScores);
    }

    private void showInformation() {
        Intent information = new Intent(MainActivity.this, InformationActivity.class);
        startActivity(information);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigationBar(this);

        prefs = getSharedPreferences("spaceLeague", MODE_PRIVATE);
        name = prefs.getString("name", "Anon");

        setContentView(R.layout.activity_main);

        animateBackground(this);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDifficultyDialog();
            }
        });

        findViewById(R.id.enterName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterNameDialog();
            }
        });

        findViewById(R.id.highScores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHighScores();
            }
        });

        findViewById(R.id.information).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInformation();
            }
        });

        TextView enterName = findViewById(R.id.enterName);
        enterName.setText(prefs.getString("name", "ENTER NAME"));

        TextView highScores = findViewById(R.id.highScores);
        highScores.setText("HIGH SCORE  " + prefs.getInt("highScore", 0));
    }
}