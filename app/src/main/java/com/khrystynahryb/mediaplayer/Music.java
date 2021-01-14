package com.khrystynahryb.mediaplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Music {
    private int id;
    private String name;
    private String artist;
    private int coverResId;
    private int artistResId;
    private int musicFileResId;

    public int getMusicFileResId() {
        return musicFileResId;
    }

    public void setMusicFileResId(int musicFileResId) {
        this.musicFileResId = musicFileResId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getCoverResId() {
        return coverResId;
    }

    public void setCoverResId(int coverResId) {
        this.coverResId = coverResId;
    }

    public int getArtistResId() {
        return artistResId;
    }

    public void setArtistResId(int artistResId) {
        this.artistResId = artistResId;
    }
    public static List<Music> getList(){
        List<Music> musicList=new ArrayList<>();
        com.khrystynahryb.mediaplayer.Music music1=new com.khrystynahryb.mediaplayer.Music();
        music1.setArtist("Antonio Vivaldi (Spring)");
        music1.setName("Conc No. 1 in E major, La primavera");
        music1.setCoverResId(R.drawable.spring);
        music1.setArtistResId(R.drawable.vivaldi);
        music1.setMusicFileResId(R.raw.spring_allegro);

        com.khrystynahryb.mediaplayer.Music music2=new com.khrystynahryb.mediaplayer.Music();
        music2.setArtist("Antonio Vivaldi (Summer)");
        music2.setName("Conc No. 2 in G minor, L'estate");
        music2.setCoverResId(R.drawable.summer);
        music2.setArtistResId(R.drawable.vivaldi);
        music2.setMusicFileResId(R.raw.spring_allegro);

        com.khrystynahryb.mediaplayer.Music music3=new com.khrystynahryb.mediaplayer.Music();
        music3.setArtist("Antonio Vivaldi (Autumn)");
        music3.setName("Conc No. 3 in F major, L'autunno");
        music3.setCoverResId(R.drawable.autumn);
        music3.setArtistResId(R.drawable.vivaldi);
        music3.setMusicFileResId(R.raw.spring_allegro);

        com.khrystynahryb.mediaplayer.Music music4=new com.khrystynahryb.mediaplayer.Music();
        music4.setArtist("Antonio Vivaldi (Winter)");
        music4.setName("Conc No. 4 in F minor, L'inverno");
        music4.setCoverResId(R.drawable.winter);
        music4.setArtistResId(R.drawable.vivaldi);
        music4.setMusicFileResId(R.raw.spring_allegro);

        musicList.add(music1);
        musicList.add(music2);
        musicList.add(music3);
        musicList.add(music4);
        return  musicList;
    }
    public static String convertMillisToString(long durationInMillis){
        long second = (durationInMillis / 1000) % 60;
        long minute = ((durationInMillis/1000) / 60) % 60;

        return String.format(Locale.US, "%02d:%02d", minute, second);
    }
}
