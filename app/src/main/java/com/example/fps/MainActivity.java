package com.example.fps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    CustomView cv;
    static Game game;
    MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new Game();
        game.reset();

        game.level.addWorldEdge(16, 24, 16);

        game.level.addTileMap(
                new int[][]{
                        {0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0},
                        {0,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,1,0},
                        {0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,1,1,0},
                        {0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0},
                        {0,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0},
                        {0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,1,1,1,0,0,0,0,0,0},
                        {0,0,0,0,1,1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0},
                        {0,0,0,0,1,1,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0},
                        {0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1},
                        {0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0,1,0,1,1,1,1,0,0,0,0,0,0,0,1,1,0},
                        {1,1,1,1,0,0,0,0,1,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0},
                        {0,0,0,1,0,0,0,0,1,0,0,1,1,1,0,0,1,1,1,0,0,0,0,1},
                        {0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,1,1,1},
                        {0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
                }
        );

        setContentView(R.layout.activity_main);

        cv = this.findViewById(R.id.custom_view);

        CustomButton up = this.findViewById(R.id.up_button);
        up.buttonID = 0;
        CustomButton down = this.findViewById(R.id.down_button);
        down.buttonID = 1;
        CustomButton left = this.findViewById(R.id.left_button);
        left.buttonID = 2;
        CustomButton right = this.findViewById(R.id.right_button);
        right.buttonID = 3;
        CustomButton rotationLeft = this.findViewById(R.id.rotation_left);
        rotationLeft.buttonID = 4;
        CustomButton rotationRight = this.findViewById(R.id.rotation_right);
        rotationRight.buttonID = 5;
    }

    protected void onResume() {
        super.onResume();
        myHandler = new MyHandler(this);
        myHandler.sleep(0);
    }

    protected void onPause() {
        super.onPause();
        myHandler = null;
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;
        MyHandler(MainActivity Activity) {
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(Activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                MainActivity activity = mActivity.get();
                if (activity != null) {
                    activity.cv.action();
                    activity.cv.invalidate();
                    if (activity.myHandler != null) activity.myHandler.sleep(10);
                }
            }
        }

        public void sleep(long delayMills) {
            removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMills);
        }
    }
}