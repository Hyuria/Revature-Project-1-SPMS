package com.revature.beans;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Status {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String status;

    public Status() {
        id = 0;
        status = "";
    }

    public Status(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void changeToPitchApprovalAE(){
        id = 1;
        status = "awaiting pitch approval: AE";
    }

    public void changeToPitchApprovalGE(){
        id = 2;
        status = "awaiting pitch approval: GE";
    }

    public void changeToPitchApprovalSE(){
        id = 3;
        status = "awaiting pitch approval: SE";
    }

    public void changeToDraftApproval(){
        id = 4;
        status = "awaiting draft approval";
    }

    public void changeToPublished(){
        id = 5;
        status = "published";
    }

    // ----------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status1 = (Status) o;
        return Objects.equals(id, status1.id) && Objects.equals(status, status1.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
