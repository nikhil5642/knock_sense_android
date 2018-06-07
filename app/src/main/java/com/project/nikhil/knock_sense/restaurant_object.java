package com.project.nikhil.knock_sense;

import java.io.Serializable;

public class restaurant_object implements Serializable {
    String image,name,tag_line,description,offers,menu,location_head,location_description,location_lat,location_lon,key;
    float review;
    int review_count;
    String owner_id;

    public restaurant_object(String image, String name, String tag_line, String description, String offers, String menu, String location_head, String location_description, String location_lat, String location_lon, String key, float review, int review_count, String owner_id) {
        this.image = image;
        this.name = name;
        this.tag_line = tag_line;
        this.description = description;
        this.offers = offers;
        this.menu = menu;
        this.location_head = location_head;
        this.location_description = location_description;
        this.location_lat = location_lat;
        this.location_lon = location_lon;
        this.key = key;
        this.review = review;
        this.review_count = review_count;
        this.owner_id = owner_id;
    }

    public restaurant_object() {
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public float getReview() {
        return review;
    }

    public void setReview(float review) {
        this.review = review;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation_description() {
        return location_description;
    }

    public void setLocation_description(String location_description) {
        this.location_description = location_description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag_line() {
        return tag_line;
    }

    public void setTag_line(String tag_line) {
        this.tag_line = tag_line;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getLocation_head() {
        return location_head;
    }

    public void setLocation_head(String location_head) {
        this.location_head = location_head;
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(String location_lat) {
        this.location_lat = location_lat;
    }

    public String getLocation_lon() {
        return location_lon;
    }

    public void setLocation_lon(String location_lon) {
        this.location_lon = location_lon;
    }
}
