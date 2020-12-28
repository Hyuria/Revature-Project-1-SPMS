package com.revature.data;

import com.revature.beans.Committee;
import com.revature.beans.Editor;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EditorHibernate implements EditorDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    @Override
    public Editor add(Editor editor){
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(editor);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return editor;
    }

    @Override
    public Editor getById(Integer id) {
        Session s = hu.getSession();
        Editor editor = s.get(Editor.class, id);
        s.close();
        return editor;
    }

    @Override
    public Set<Editor> getAll() {
        Session s = hu.getSession();
        String query = "FROM Editor";
        Query<Editor> q = s.createQuery(query, Editor.class);
        List<Editor> editorList = q.getResultList();
        Set<Editor> editorSet = new HashSet<>();
        editorSet.addAll(editorList);
        s.close();
        return editorSet;
    }

    //TODO: Maybe this needs to be implemented
    @Override
    public Set<Committee> getAllCommittee(Editor editor) {
        Set<Committee> committeeSet = new HashSet<>();


        return committeeSet;
    }

    @Override
    public void update(Editor editor) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(editor);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(Editor editor) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(editor);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
            resetSequence();
        }
    }
    public void resetSequence(){
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            if (getAll().size() > 0){
                s.createSQLQuery("SELECT setval('online_publisher.editor_id_seq', max(id)) FROM editor").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.editor_id_seq RESTART WITH 1").executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }


}
