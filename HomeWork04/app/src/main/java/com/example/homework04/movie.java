package com.example.homework04;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.Year;

public class movie implements Parcelable {
    private String Name;
    private String Description;
    private String Genre;
    private int Rating;
    private java.time.Year Year;
    private String Imdb;

    public movie(String name, String description, String genre, int rating, java.time.Year year, String IMDB) {
        Name = name;
        Description = description;
        Genre = genre;
        Rating = rating;
        Year = year;
        Imdb = IMDB;
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

    public java.time.Year getYear() {
        return Year;
    }

    public void setYear(java.time.Year year) {
        Year = year;
    }

    public String getImdb() {
        return Imdb;
    }

    public void setImdb(String IMDB) {
        Imdb = IMDB;
    }

    // Parcelling part
    public movie(Parcel in) {
        String[] movie = new String[6];

        in.readStringArray(movie);
        this.Name = movie[0];
        this.Description = movie[1];
        this.Genre = movie[2];
        this.Rating = Integer.parseInt(movie[3]);
        this.Year = java.time.Year.parse(movie[4]);
        this.Imdb = movie[5];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.Name,
                this.Description,
                this.Genre,
                String.valueOf(this.Rating),
                String.valueOf(this.Year),
                this.Imdb});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public com.example.homework04.movie createFromParcel(Parcel in) {
            return new com.example.homework04.movie(in);
        }

        public com.example.homework04.movie[] newArray(int size) {
            return new com.example.homework04.movie[size];
        }
    };
}



