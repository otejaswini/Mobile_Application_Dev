package com.example.firebasetest;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class movie implements Serializable {
   String Name;
   String Description;
    String Genre;
    int Rating;
    String Year;
   String Imdb;
    public movie(String name, String description, String genre, int rating, String year, String IMDB) {
        this.Name = name;
        this.Description = description;
        this.Genre = genre;
        this.Rating = rating;
        this.Year = year;
        this.Imdb = IMDB;
    }
    public movie(Map movieMap){
        this.setName((String) movieMap.get("Name"));
        this.setDescription((String) movieMap.get("Description"));
        this.setGenre((String) movieMap.get("Genre"));
        this.setRating((int)(long) movieMap.get("Rating"));
        this.setYear((String) movieMap.get("Year"));
        this.setImdb((String) movieMap.get("Imdb"));
    }
    public Map toMovieMap(){
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("Name",this.Name);
        objectMap.put("Description",this.Description);
        objectMap.put("Genre",this.Genre);
        objectMap.put("Rating",this.Rating);
        objectMap.put("Year",this.Year);
        objectMap.put("Imdb",this.Imdb);

        return objectMap;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getImdb() {
        return Imdb;
    }

    public void setImdb(String IMDB) {
        Imdb = IMDB;
    }
}



