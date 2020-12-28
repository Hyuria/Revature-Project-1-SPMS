package com.revature.data;

import com.revature.beans.Editor;
import com.revature.beans.User;
import com.revature.exception.NonUniqueUsernameException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EditorHibernateTest {
    private UserHibernate userHibernate = new UserHibernate();
    private EditorHibernate editorHibernate = new EditorHibernate();
    private EditorRoleHibernate editorRoleHibernate = new EditorRoleHibernate();
    private Editor editor = new Editor();
    private User user = new User();

    @Test
    @Order(1)
    public void testAdd() throws NonUniqueUsernameException {
        user.setUsername("test");
        user.setPassword("pwd");
        user.setId(userHibernate.add(user).getId());
        editor.setFirstName("John");
        editor.setLastName("Smith");
        editor.setUser(user);
        editor.setRole(editorRoleHibernate.getByAbbrv("SE"));
        Editor retEditor = editorHibernate.add(editor);
        editor.setId(retEditor.getId());
        assertEquals(editor,retEditor);
    }

    @Test
    @Order(2)
    public void testGetById(){
        assertEquals(editor, editorHibernate.getById(editor.getId()));
    }

    @Test
    @Order(3)
    public void testGetAll(){
        assertTrue(editorHibernate.getAll().size() > 0);
    }

    @Test
    @Order(4)
    public void testUpdate(){
        editor.setLastName("Jane");
        editorHibernate.update(editor);
        assertEquals(editor, editorHibernate.getById(editor.getId()));
    }

    @Test
    @Order(5)
    public void testDelete(){
        editorHibernate.delete(editor);
        userHibernate.delete(user);
        assertTrue(editorHibernate.getById(editor.getId()) == null);
    }
}
