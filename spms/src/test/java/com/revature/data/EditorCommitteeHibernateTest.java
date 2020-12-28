package com.revature.data;

import com.revature.beans.Committee;
import com.revature.beans.Editor;
import com.revature.beans.User;
import com.revature.exception.NonUniqueUsernameException;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EditorCommitteeHibernateTest {
    private CommitteeHibernate committeeHibernate = new CommitteeHibernate();
    private GenreHibernate genreHibernate = new GenreHibernate();
    private UserHibernate userHibernate = new UserHibernate();
    private EditorRoleHibernate editorRoleHibernate = new EditorRoleHibernate();
    private EditorHibernate editorHibernate = new EditorHibernate();

    private Committee committee = new Committee();
    private Editor editor = new Editor();
    private User user = new User();
    private Set<Editor> editorSet = new HashSet<>();

    @Test
    @Order(1)
    public void makesEntryAfterInsertion() throws NonUniqueUsernameException {
        user.setUsername("test");
        user.setPassword("password");
        user = userHibernate.add(user);
        System.out.println("User Created(ID #): " + user.getId());

        editor.setUser(user);
        editor.setRole(editorRoleHibernate.getByAbbrv("GE"));
        editor.setFirstName("John");
        editor.setLastName("Smith");
        Editor retEditor = editorHibernate.add(editor);
        editor.setId(retEditor.getId());
        System.out.println("Editor Created(ID #): " + editor.getId());

        committee.setGenre(genreHibernate.getById(1));
        editorSet.add(editor);
        committee.setEditorSet(editorSet);
        committee = committeeHibernate.add(committee);
        System.out.println("Committee Created(ID #): " + committee.getId());

    }

    @Test
    @Order(2)
    public void checkCommittee(){
        Committee retCommittee = committeeHibernate.getById(committee.getId());
        System.out.println(retCommittee.getEditorSet());
    }


    @Test
    @Order(3)
    public void deleteEntry(){
        editorSet = new HashSet<>();
        committee.setEditorSet(editorSet);
        committeeHibernate.update(committee);
        editorHibernate.delete(editor);
        userHibernate.delete(user);
        Committee retCommittee = committeeHibernate.getById(committee.getId());
        System.out.println(retCommittee.getEditorSet());
        committeeHibernate.delete(committee);
    }

}
