package com.amitthakare.camerascanner.Model;

public class FolderData {

    String folderName, folderDate, folderTime, folderPages,folderImage;

    public FolderData() {
    }

    public FolderData(String folderName, String folderDate, String folderTime, String folderPages, String folderImage) {
        this.folderName = folderName;
        this.folderDate = folderDate;
        this.folderTime = folderTime;
        this.folderPages = folderPages;
        this.folderImage = folderImage;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderDate() {
        return folderDate;
    }

    public void setFolderDate(String folderDate) {
        this.folderDate = folderDate;
    }

    public String getFolderTime() {
        return folderTime;
    }

    public void setFolderTime(String folderTime) {
        this.folderTime = folderTime;
    }

    public String getFolderPages() {
        return folderPages;
    }

    public void setFolderPages(String folderPages) {
        this.folderPages = folderPages;
    }

    public String getFolderImage() {
        return folderImage;
    }

    public void setFolderImage(String folderImage) {
        this.folderImage = folderImage;
    }
}
