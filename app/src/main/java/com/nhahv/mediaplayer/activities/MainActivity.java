package com.nhahv.mediaplayer.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nhahv.mediaplayer.R;
import com.nhahv.mediaplayer.adapter.MediaSongAdapter;
import com.nhahv.mediaplayer.interfaces.OnClick;
import com.nhahv.mediaplayer.interfaces.OnClickRecyclerView;
import com.nhahv.mediaplayer.media.MediaPlayerManager;
import com.nhahv.mediaplayer.models.MediaSong;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, MediaPlayer.OnCompletionListener,
        SeekBar.OnSeekBarChangeListener {

    private static final long TIME_DELAY = 100;
    private static final int TIME_SPEED = 5000;
    private final String TAG = getClass().getSimpleName();
    private List<MediaSong> mListMediaPlayer;

    private MediaPlayerManager mPlayerManager;

    private ImageView mImagePlay;
    private SeekBar mSeekBar;
    private TextView mTextCurrentTime, mTextTotalTime;


    private int mPosition;

    private MediaSongAdapter adapter;
    private boolean isPlayContinue;
    private boolean isLoop;

    private int mTimeCurrent;
    private int mTimeEnd;

    private Handler mHandler;


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTimeCurrent = mPlayerManager.getCurrentTime();
            String timeCurrent = convertTime(mTimeCurrent);
            mTextCurrentTime.setText(timeCurrent);
            mHandler.postDelayed(this, TIME_DELAY);
            mSeekBar.setProgress(mTimeCurrent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPlayerManager = new MediaPlayerManager(this, this);
        mListMediaPlayer = mPlayerManager.getListMediaSong();

        mHandler = new Handler();
        for (MediaSong song : mListMediaPlayer) {
            Log.d(TAG, song.getName());
        }

        initViews();
        addEvents();
    }

    private void initViews() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new MediaSongAdapter(this, mListMediaPlayer);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnClickRecyclerView(this, recyclerView, new OnClick() {
            @Override
            public void onClick(View view, int position) {
                if (position != mPosition) {
                    mPosition = position;
                    isPlayContinue = true;
                    onClickPlayMedia(mPosition);
                }
            }
        }

        ));

        mImagePlay = (ImageView) findViewById(R.id.btn_play);
        mTextCurrentTime = (TextView) findViewById(R.id.text_current_time);
        mTextTotalTime = (TextView) findViewById(R.id.text_total_time);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {
        findViewById(R.id.btn_loop).setOnClickListener(this);
        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_prev).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_prev_time).setOnClickListener(this);
        findViewById(R.id.btn_next_time).setOnClickListener(this);
        findViewById(R.id.btn_list_song).setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loop:
                if (mPlayerManager.getLoop()) {
                    mPlayerManager.setLoop(false);
                    ((ImageView) findViewById(R.id.btn_loop))
                            .setImageResource(R.drawable.ic_loop_white_24dp);
                } else {
                    mPlayerManager.setLoop(true);
                    ((ImageView) findViewById(R.id.btn_loop))
                            .setImageResource(R.drawable.ic_loop_white_select);
                }
                break;
            case R.id.btn_play:
                onClickPlayMedia(mPosition);
                break;
            case R.id.btn_prev:
                if (mPosition > 0) {
                    mPosition--;
                } else {
                    mPosition = mListMediaPlayer.size() - 1;
                }
                isPlayContinue = true;
                onClickPlayMedia(mPosition);
                break;
            case R.id.btn_next:
                if (mPosition < mListMediaPlayer.size() - 1) {
                    mPosition++;
                } else {
                    mPosition = 0;
                }
                isPlayContinue = true;
                onClickPlayMedia(mPosition);
                break;
            case R.id.btn_list_song:
                break;
            case R.id.btn_prev_time:
                int timeCurrent1 = mTimeCurrent - TIME_SPEED;
                if (timeCurrent1 <= TIME_SPEED) {
                    timeCurrent1 = 0;
                }
                mPlayerManager.seekTo(timeCurrent1);

                break;
            case R.id.btn_next_time:
                int timeCurrent = mTimeCurrent + TIME_SPEED;
                if (timeCurrent >= mTimeEnd - TIME_SPEED) {
                    timeCurrent = mTimeEnd;
                }
                mPlayerManager.seekTo(timeCurrent);
                break;

            default:
                break;
        }
    }

    private void onClickPlayMedia(int mPosition) {

        if (isPlayContinue) {
            mPlayerManager.playOrPause(mListMediaPlayer.get(mPosition).getPath(), isPlayContinue);

            mTimeEnd = mPlayerManager.getTotalTime();
            mTimeCurrent = mPlayerManager.getCurrentTime();

            mHandler.postDelayed(mRunnable, TIME_DELAY);
            String timeCurrent = convertTime(mTimeCurrent);
            mTextCurrentTime.setText(timeCurrent);

            String timeEnd = convertTime(mTimeEnd);
            mTextTotalTime.setText(timeEnd);

            mSeekBar.setProgress(0);
            mSeekBar.setMax(mTimeEnd);


        } else {
            mPlayerManager.playOrPause(mListMediaPlayer.get(mPosition).getPath(), false);
        }
        isPlayContinue = false;
        mListMediaPlayer.get(mPosition).setChose(true);

        int size = mListMediaPlayer.size();
        for (int i = 0; i < size; i++) {
            if (mPosition != i) {
                mListMediaPlayer.get(i).setChose(false);
            }
        }

        adapter.notifyDataSetChanged();
        if (MediaPlayerManager.mediaState == MediaPlayerManager.PLAY) {
            mImagePlay.setImageResource(R.drawable.ic_pause_white_24dp);
        }
        if (MediaPlayerManager.mediaState == MediaPlayerManager.PAUSE
                || MediaPlayerManager.mediaState == MediaPlayerManager.STOP) {
            mImagePlay.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

    @Override
    protected void onStop() {
        MediaPlayerManager.mediaState = MediaPlayerManager.IDLE;
        super.onStop();
    }


    private String convertTime(int time) {

        long minute = TimeUnit.MILLISECONDS.toMinutes((long) time);
        long second = TimeUnit.MILLISECONDS.toSeconds((long) time)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                .toMinutes((long) time));

        return (minute + ":" + second);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mPosition < mListMediaPlayer.size() - 1) {
            mPosition++;
        } else {
            mPosition = 0;
        }
        isPlayContinue = true;
        onClickPlayMedia(mPosition);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        mHandler.removeCallbacks(mRunnable);

        mTimeCurrent = seekBar.getProgress();
        mTextCurrentTime.setText(convertTime(mTimeCurrent));
        mPlayerManager.seekTo(mTimeCurrent);
        mSeekBar.setProgress(mTimeCurrent);
        mHandler.postDelayed(mRunnable, TIME_DELAY);
    }
}
