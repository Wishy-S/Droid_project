package com.example.express.model;

public class Items

{

    private  String pname,description,image,pid,date,time,price,quantity,dname,dphone;

    public Items(){

    }


    public Items(String pname, String description, String image, String pid, String date, String time, String price,String quantity,String dname,String dphone) {
        this.pname = pname;
        this.dname = dname;
        this.dphone =dphone;
        this.description = description;
        this.image = image;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.price = price;
        this.quantity = quantity;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setQuantity(String quantity){this.quantity = quantity;}
    public String getQuantity(){return quantity;}

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDphone() {
        return dphone;
    }

    public void setDphone(String dphone) {
        this.dphone = dphone;
    }
}
