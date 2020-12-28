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

public class CommitteeHibernate implements CommitteeDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    @Override
    public Committee add(Committee committee) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(committee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return committee;
    }

    @Override
    public Committee getById(Integer id) {
        Session s = hu.getSession();
        Committee committee = s.get(Committee.class, id);
        s.close();
        return committee;
    }

    @Override
    public Set<Committee> getAll() {
        Session s = hu.getSession();
        String query = "FROM Committee";
        Query<Committee> q = s.createQuery(query, Committee.class);
        List<Committee> committeeList = q.getResultList();
        Set<Committee> committeeSet = new HashSet<>();
        committeeSet.addAll(committeeList);
        s.close();
        return committeeSet;
    }

    // TODO: Maybe this needs to be implemented
    @Override
    public Set<Editor> getAllEditor() {
        return null;
    }

    @Override
    public void update(Committee committee) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(committee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(Committee committee) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(committee);
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
                s.createSQLQuery("SELECT setval('online_publisher.committee_id_seq', max(id)) FROM committee").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.committee_id_seq RESTART WITH 1").executeUpdate();
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
