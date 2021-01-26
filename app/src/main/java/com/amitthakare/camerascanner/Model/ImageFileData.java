package com.amitthakare.camerascanner.Model;

public class ImageFileData {

    String image,imageName;


    public ImageFileData() {
    }

    public ImageFileData(String image, String imageName) {
        this.image = image;
        this.imageName = imageName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
