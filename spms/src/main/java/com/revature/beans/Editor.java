package com.revature.beans;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Editor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "editor_role")
    private EditorRole role;

    public Editor() {
        id = 0;
        user = null;
        firstName = "";
        lastName = "";
        role = null;
    }

    public Editor(Integer id, User user, String firstName, String lastName, EditorRole role) {
        this.id = id;
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EditorRole getRole() {
        return role;
    }

    public void setRole(EditorRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Editor editor = (Editor) o;
        return Objects.equals(id, editor.id) && Objects.equals(user, editor.user) && Objects.equals(firstName, editor.firstName) && Objects.equals(lastName, editor.lastName) && Objects.equals(role, editor.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, firstName, lastName, role);
    }

    @Override
    public String toString() {
        return "Editor{" +
                "id=" + id +
                ", user=" + user +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }
}
