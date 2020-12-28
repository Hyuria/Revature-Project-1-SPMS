package com.revature.data;

import com.revature.beans.Approval;
import com.revature.beans.Editor;
import com.revature.beans.Story;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApprovalHibernateTest {
    private StoryHibernate storyHibernate = new StoryHibernate();
    private EditorHibernate editorHibernate = new EditorHibernate();
    private ApprovalHibernate approvalHibernate = new ApprovalHibernate();
    private Approval approval = new Approval();
    private Story story = new Story();

    private Approval approval2 = new Approval();
    private Story story2 = new Story();

    @Test
    @Order(1)
    public void testAdd(){
        approval.setApproval(0);
        story.setTitle("TEST TITLE");
        story.setTagline("TEST TAGLINE");
        story.setDescription("TEST DESCRIPTION");
        approval.setStory(story);
        approval.setEditor(editorHibernate.getById(1));
        storyHibernate.add(story);
        Approval retApproval = approvalHibernate.add(approval);
        approval.setId(retApproval.getId());
        assertEquals(approval, retApproval);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(approval, approvalHibernate.getById(approval.getId()));
    }

    @Test
    @Order(3)
    public void testGetAll(){
        assertTrue(approvalHibernate.getAll().size() > 0);
    }

    @Test
    @Order(4)
    public void testGetApprovalByEditor(){
        approval2.setApproval(0);
        story2.setTitle("TEST TITLE");
        story2.setTagline("TEST TAGLINE");
        story2.setDescription("TEST DESCRIPTION");
        approval2.setStory(story);
        approval2.setEditor(editorHibernate.getById(2));
        storyHibernate.add(story2);
        approval2 = approvalHibernate.add(approval2);
        HashSet<Approval> approvalHashSet = new HashSet<>();
        approvalHashSet.add(approval);
        assertEquals(approvalHashSet, approvalHibernate.getApprovalByEditor(1));
    }

    @Test
    @Order(5)
    public void testUpdate(){
        approval.setApproval(1);
        approvalHibernate.update(approval);
        assertEquals(approval, approvalHibernate.getById(approval.getId()));
    }

    @Test
    @Order(6)
    public void testDelete(){
        approvalHibernate.delete(approval);
        approvalHibernate.delete(approval2);
        storyHibernate.delete(story);
        storyHibernate.delete(story2);

        assertTrue(approvalHibernate.getById(approval.getId()) == null);
    }
}
