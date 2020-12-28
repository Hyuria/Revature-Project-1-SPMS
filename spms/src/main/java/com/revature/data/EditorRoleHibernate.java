package com.revature.data;


import com.revature.beans.Editor;
import com.revature.beans.EditorRole;
import com.revature.beans.User;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EditorRoleHibernate implements EditorRoleDAO {
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    // Will not use
    @Override
    public EditorRole add(EditorRole editorRole){
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(editorRole);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return editorRole;
    }



    @Override
    public EditorRole getById(Integer id) {
        Session s = hu.getSession();
        EditorRole editorRole = s.get(EditorRole.class, id);
        s.close();
        return editorRole;
    }

    @Override
    public EditorRole getByAbbrv(String abbrv) {
        String pass_in_value;
        switch (abbrv){
            case "AE": pass_in_value = "Assistant Editor";
                break;
            case "GE": pass_in_value = "General Editor";
                break;
            case "SE":
                pass_in_value = "Senior Editor";
                break;
            default:
                return null;
        }
        Session s = hu.getSession();
        String hql = "FROM EditorRole where editorRole = :role";
        Query query = s.createQuery(hql);
        query.setParameter("role", pass_in_value);
        EditorRole editorRole = (EditorRole) query.getSingleResult();
        s.close();
        return editorRole;
    }

    @Override
    public Set<EditorRole> getAll() {
        Session s = hu.getSession();
        String query = "FROM EditorRole ";
        Query<EditorRole> q = s.createQuery(query, EditorRole.class);
        List<EditorRole> editorRoleList = q.getResultList();
        Set<EditorRole> editorRoleSet = new HashSet<>();
        editorRoleSet.addAll(editorRoleList);
        s.close();
        return editorRoleSet;
    }

    // Will not use
    @Override
    public void update(EditorRole editorRole) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(editorRole);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    // Will not use
    @Override
    public void delete(EditorRole editorRole) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(editorRole);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
            resetSequence();
        }
    }

    // Will not use
    public void resetSequence(){
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            if (getAll().size() > 0){
                s.createSQLQuery("SELECT setval('online_publisher.editor_role_id_seq', max(id)) FROM editor_role").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.editor_role_id_seq RESTART WITH 1").executeUpdate();
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
