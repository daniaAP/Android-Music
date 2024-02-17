package com.example.daniaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageViewSplash);

    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);

        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeIn);

        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // This method is called when the animation starts
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setAlpha(1f);

                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // This method is called when the animation repeats
            }
        };

        animation.setAnimationListener(listener);

        imageView.setAnimation(animation);
        animation.start();

    }
}

