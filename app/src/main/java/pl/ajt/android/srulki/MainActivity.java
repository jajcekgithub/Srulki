package pl.ajt.android.srulki;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    SrulkiView srulkiView = null;
    private Drawable mTiles = null;
    Bitmap bitmap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resources; resources = getResources();
        //bitmap1 = BitmapFactory.decodeResource( resources,  R.drawable.index );
        srulkiView = new SrulkiView(this);
        setContentView(srulkiView);


//        setContentView(R.layout.activity_main);
    }

    class SrulkiView extends SurfaceView implements SurfaceHolder.Callback {

        GameThread gameThread = null;

        private Ball[][] board = new Ball[9][9];
        Paint paint;
        int screenX;
        int screenY;

        public SrulkiView(Context context) {
            super(context);

            paint = new Paint();

            // get initial screen size
            Point l_size = new Point();
            Display l_display = getWindowManager().getDefaultDisplay();
            l_display.getSize(l_size);
            screenX = l_size.x;
            screenY = l_size.y;

            fillBoard(screenX, screenY);

            SurfaceHolder l_holder = getHolder();
            l_holder.addCallback(this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            gameThread = new GameThread(holder);
            gameThread.setRunning(true);
            gameThread.start();
            Log.d("DEBUG", "surfaceCreated");

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            screenX = width;
            screenY = height;
            Log.d("DEBUG", "surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            gameThread.setRunning(false);
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float l_x = event.getX();
            float l_y = event.getY();
            int action = event.getAction();

            return true;
        }

        class GameThread extends Thread{
            private boolean m_isRunning = false;
            private SurfaceHolder m_holder;

            public GameThread(SurfaceHolder p_holder) {
                m_holder = p_holder;
            }

            public void setRunning(boolean p_isRunning){
                m_isRunning = p_isRunning;
            }

            @Override
            public void run() {
                while(m_isRunning){
                    Canvas l_canvas=null;
                    try {
                        l_canvas = m_holder.lockCanvas();
                        synchronized(m_holder) {
                            l_canvas.drawColor(Color.CYAN);
                            for (int x = 0; x < 9; x++){
                                //Log.d("DEBUG", "ROW = " + x);
                                for (int y = 0; y < 9; y++){
                                    //Log.d("DEBUG", "KOL = " + y);

                                    Ball l_ball = board[x][y];
                                  //  Log.d("DEBUG", "x" + l_ball.cx + ", y=" + l_ball.cy);
                                    paint.setColor(Color.RED);
                                    l_canvas.drawCircle(l_ball.cx, l_ball.cy, l_ball.r, paint);

                                    //l_canvas.drawPaint(paint);
                                }

                            }
                        }
                    } finally {
                        if (l_canvas != null) { m_holder.unlockCanvasAndPost(l_canvas); }
                    }

                }
            }
        }

        void fillBoard(int screenX, int screenY) {
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    board[x][y] = new Ball();
                }
            }

            for (int x = 0; x < 9; x++){
                for (int y = 0; y < 9; y++){
                    board[x][y].cx = ((screenX / 9.0f)*x);
                    board[x][y].cy = ((screenY / 9.0f)*y);
                    Log.d("DEBUG", "x=" + board[x][y].cx + ", y=" + board[x][y].cy);
                }
            }

            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    Ball l_ball = board[x][y];
                    Log.d("DEBUG", "x" + l_ball.cx + ", y=" + l_ball.cy);

                }
            }
        }
    }
}
