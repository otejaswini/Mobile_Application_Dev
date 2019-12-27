package com.example.inclass11_group34;

public class images {
    private String imageDownload;
    private String imageStorageRef;

    public images() {
        super();
    }
    public images(String imageDownload, String imageStorageRef) {
        this.imageDownload = imageDownload;
        this.imageStorageRef = imageStorageRef;
    }
    @Override
    public String toString() {
        return "ImageDetails{" +
                "imageDownload='" + imageDownload + '\'' +
                ", imageStorageRef='" + imageStorageRef + '\'' +
                '}';
    }
    public String getImageDownload() {
        return imageDownload;
    }

    public void setImageDownloadUrl(String imageDownload) {
        this.imageDownload = imageDownload;
    }
    public String getImageStorageRef() {
        return imageStorageRef;
    }
    public void setImageStorageRef(String imageStorageReference) {
        this.imageStorageRef = imageStorageReference;
    }
}
