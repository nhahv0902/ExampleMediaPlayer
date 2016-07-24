package com.nhahv.mediaplayer.models;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Nhahv on 7/24/2016.
 * <></>
 */
public class MediaSong {

    private String name;
    private String fileName;
    private String path;
    private String artist;
    private String album;
    private long duration;
    private String time;
    private int lenght;
    private boolean isChose;

    public MediaSong(String name, String fileName, String path,
                     String artist, String album, long duration) {
        this.name = name;
        this.fileName = fileName;
        this.path = path;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        convertDuration();
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public long getDuration() {
        return duration;
    }

    public String getTime() {
        return time;
    }

    public void setChose(boolean chose) {
        isChose = chose;
    }

    public boolean isChose() {
        return isChose;
    }

    private void convertDuration() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        time = simpleDateFormat.format(duration);
    }

}
