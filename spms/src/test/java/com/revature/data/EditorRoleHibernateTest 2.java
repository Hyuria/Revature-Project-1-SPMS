package com.revature.data;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EditorRoleHibernateTest {
    private EditorRoleHibernate editorRoleHibernate = new EditorRoleHibernate();

    @Test
    public void testGetById(){
        assertEquals("Assistant Editor", editorRoleHibernate.getById(1).getEditorRole());
    }

    @Test
    public void testGetByAbbrv(){
        assertEquals("Assistant Editor", editorRoleHibernate.getByAbbrv("AE").getEditorRole());
    }

    @Test
    public void testGetAll(){
        assertTrue(editorRoleHibernate.getAll().size() == 3);
    }
}
