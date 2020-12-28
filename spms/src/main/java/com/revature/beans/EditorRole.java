package com.revature.beans;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "editor_role")
public class EditorRole {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column( name = "editor_role")
    private String editorRole;

    public EditorRole() {
        id = 0;
        editorRole = "";
    }

    public EditorRole(Integer id, String editorRole) {
        this.id = id;
        this.editorRole = editorRole;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEditorRole() {
        return editorRole;
    }

    public void setEditorRole(String editorRole) {
        this.editorRole = editorRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditorRole that = (EditorRole) o;
        return Objects.equals(id, that.id) && Objects.equals(editorRole, that.editorRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, editorRole);
    }

    @Override
    public String toString() {
        return "EditorRole{" +
                "id=" + id +
                ", editorRole='" + editorRole + '\'' +
                '}';
    }
}
