package com.revature.beans;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Approval {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "story")
    private Story story;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "editor")
    private Editor editor;
    private Integer approval;

    public Approval() {
        id = 0;
        story = null;
        editor = null;
        approval = 0;
    }

    public Approval(Integer id, Story story, Editor editor, Integer approval) {
        this.id = id;
        this.story = story;
        this.editor = editor;
        this.approval = approval;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public Integer getApproval() {
        return approval;
    }

    public void setApproval(Integer approval) {
        this.approval = approval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Approval that = (Approval) o;
        return Objects.equals(id, that.id) && Objects.equals(story, that.story) && Objects.equals(editor, that.editor) && Objects.equals(approval, that.approval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, story, editor, approval);
    }

    @Override
    public String toString() {
        return "FinalApproval{" +
                "id=" + id +
                ", story=" + story +
                ", editor=" + editor +
                ", approval=" + approval +
                '}';
    }
}
