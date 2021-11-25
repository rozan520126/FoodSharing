package models;

public class Post {
    String pId,pTitle,pLocation,pDes,pdaytime,pImage,pTime,uid,uName,uImage;

    public Post() {}

    public Post(String pDes,String pId,String pImage,String pLocation ,String pTime,String pTitle,String pdaytime, String uid,String uName,String uImage) {
        this.pId = pId;
        this.pTitle = pTitle;
        this.pDes = pDes;
        this.pLocation = pLocation;
        this.pdaytime = pdaytime;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uName = uName;
        this.uImage = uImage;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpLocation() {
        return pLocation;
    }

    public void setpLocation(String pLocation) {
        this.pLocation = pLocation;
    }

    public String getpdaytime() {
        return pdaytime;
    }

    public void setpdaytime(String pdaytime) {
        this.pdaytime = pdaytime;
    }

    public String getpDes() {
        return pDes;
    }

    public void setpDes(String pDes) {
        this.pDes = pDes;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuImage() { return uImage;}

    public void setuImage(String uImage) {
        this.uImage = uImage;
    }

}
