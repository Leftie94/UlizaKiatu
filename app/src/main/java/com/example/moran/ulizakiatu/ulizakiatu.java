package com.example.moran.ulizakiatu;


public class ulizakiatu {
    private String image,title,price,describe,username,profPic;

    public ulizakiatu(){
    }
    public ulizakiatu(String image, String title, String price, String describe, String username, String profPic){

        this.image=image;
        this.title=title;
        this.price=price;
        this.describe=describe;
        this.username=username;
        this.profPic=profPic;
    }

    public String getTitle() {
        return title;
    }

    public String getDescribe() {
        return describe;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getProfPic() {
        return profPic;
    }

    public String getUsername() {
        return username;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void setImage(String image) {

        this.image = image;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setProfPic(String profPic) {
        this.profPic = profPic;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
