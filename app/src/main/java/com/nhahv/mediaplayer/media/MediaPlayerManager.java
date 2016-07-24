package com.nhahv.mediaplayer.media;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.nhahv.mediaplayer.models.MediaSong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nhahv on 7/24/2016.
 * <></>
 */
public class MediaPlayerManager {

    public static final int IDLE = 1;
    public static final int PLAY = 2;
    public static final int STOP = 3;
    public static final int PAUSE = 4;

    private final String TAG = getClass().getSimpleName();

    private List<MediaSong> mListMediaSongs = new ArrayList<>();
    private Context mContext;


    private MediaPlayer mMediaPlayer;
    public static int mediaState = IDLE;


    public MediaPlayerManager(Context context, MediaPlayer.OnCompletionListener event) {
        mContext = context;
        setListMediaSongs();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(event);
    }


    private void setListMediaSongs() {

        String[] projection = new String[]{
                MediaStore.MediaColumns.TITLE,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION
        };

        Cursor cursor = mContext.getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        null,
                        null);

        if (cursor == null) {
            return;
        }
        cursor.moveToFirst();

        String[] nameColumn = cursor.getColumnNames();


        int indexTitle = cursor.getColumnIndex(projection[0]);
        int indexDisplayName = cursor.getColumnIndex(projection[1]);
        int indexData = cursor.getColumnIndex(projection[2]);
        int indexARTIST = cursor.getColumnIndex(projection[3]);
        int indexALBUM = cursor.getColumnIndex(projection[4]);
        int indexDURATION = cursor.getColumnIndex(projection[5]);

        while (!cursor.isAfterLast()) {

            for (String string : nameColumn) {
                String datas = cursor.getString(cursor.getColumnIndex(string));
                Log.d(TAG, string + " = " + datas);
            }

            String title = cursor.getString(indexTitle);
            String name = cursor.getString(indexDisplayName);
            String data = cursor.getString(indexData);
            String artist = cursor.getString(indexARTIST);
            String album = cursor.getString(indexALBUM);
            String duration = cursor.getString(indexDURATION);

            mListMediaSongs.add(new MediaSong(title, name, data, artist, album, Integer.parseInt(duration)));

            cursor.moveToNext();
        }

        cursor.close();
    }

    public List<MediaSong> getListMediaSong() {
        return mListMediaSongs;
    }


    public void playOrPause(String pathMusic, boolean isPlayContinous) {
        if (mediaState == IDLE || mediaState == STOP) {
            Uri uri = Uri.parse(pathMusic);
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mContext, uri);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaState = PLAY;
        }
        if (mediaState == PLAY && isPlayContinous) {
            try {
                Uri uri = Uri.parse(pathMusic);
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mContext, uri);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mediaState = PLAY;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mediaState == PLAY && !isPlayContinous) {
            mMediaPlayer.pause();
            mediaState = PAUSE;
        }
        if (mediaState == PAUSE) {
            mMediaPlayer.start();
            mediaState = PLAY;
        }
    }


    public int getCurrentTime() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getTotalTime() {
        return mMediaPlayer.getDuration();
    }

    public void seekTo(int time) {
        mMediaPlayer.seekTo(time);
    }

    public void setLoop(boolean isLoop) {
        mMediaPlayer.setLooping(isLoop);
    }

    public boolean getLoop() {
        return mMediaPlayer.isLooping();
    }
}
