package com.example.inclass06;


public class newsApi {
        String title;
        String description;
        String urlToImage;
        String publishedAt;

        public newsApi() {
        }

        @Override
        public String toString() {
            return "newsApi{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", urlToImage='" + urlToImage + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    '}';
        }
}

