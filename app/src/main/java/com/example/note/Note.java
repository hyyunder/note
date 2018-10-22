package com.example.note;

public class Note {
    String text=null;
    String title="无标题";
    String date;
    String picture=null;
    String video=null;
    String music=null;
    int id;
    public Note(int id,String title,String text,String date,String picture,String video,String music){
        this.title=title;
        this.text=text;
        this.date=date;
        this.picture=picture;
        this.video=video;
        this.music=music;
        this.id=id;
    }
    public Note(){

    }

    public void setId(int id){
        this.id=id;
    }

    public int getId(){
        return id;
    }

    public void setText(String text){
        this.text=text;
    }

    public String getText(){
        return text;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getTitle(){
        return title;
    }

    public void setDate(String date){
        this.date=date;
    }

    public String getDate(){
        return date;
    }

    public void setPicture(String picture){
        this.picture=picture;
    }

    public String getPicture(){
        return picture;
    }

    public void setVideo(String video){
        this.video=video;
    }

    public String getVideo(){
        return video;
    }

    public void setMusic(String music){
        this.video=video;
    }

    public String getMusic(){
        return music;
    }
}
