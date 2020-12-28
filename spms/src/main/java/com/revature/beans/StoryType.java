package com.revature.beans;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "story_type")
public class StoryType {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "story_type")
    private String type;
    @Column(name = "point_value")
    private Integer pointValue;

    public StoryType() {
        id = 0;
        type = "";
        pointValue = 0;
    }

    public StoryType(String string){
        switch (string){
            case "novel":
                id = 1;
                type = string;
                pointValue = 50;
                break;
            case "novella":
                id = 2;
                type = string;
                pointValue = 25;
                break;
            case "short story":
                id = 3;
                type = string;
                pointValue = 20;
                break;
            case "article":
                id = 4;
                type = string;
                pointValue = 10;
                break;
            default:
                id = 0;
                type = "";
                pointValue = 0;
                break;
        }
    }


    public StoryType(Integer id, String type, Integer pointValue) {
        this.id = id;
        this.type = type;
        this.pointValue = pointValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPointValue() {
        return pointValue;
    }

    public void setPointValue(Integer pointValue) {
        this.pointValue = pointValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoryType storyType = (StoryType) o;
        return Objects.equals(id, storyType.id) && Objects.equals(type, storyType.type) && Objects.equals(pointValue, storyType.pointValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, pointValue);
    }

    @Override
    public String toString() {
        return "StoryType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", pointValue=" + pointValue +
                '}';
    }
}
