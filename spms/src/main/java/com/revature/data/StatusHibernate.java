package com.revature.data;

import com.revature.beans.Status;
import com.revature.beans.Story;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatusHibernate implements StatusDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    @Override
    public Status add(Status status) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(status);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return status;
    }

    @Override
    public Status getById(Integer id) {
        Session s = hu.getSession();
        Status status = s.get(Status.class, id);
        s.close();
        return status;
    }

    @Override
    public Set<Status> getAll() {
        Session s = hu.getSession();
        String query = "FROM Status";
        Query<Status> q = s.createQuery(query, Status.class);
        List<Status> statusList = q.getResultList();
        Set<Status> statusSet = new HashSet<>();
        statusSet.addAll(statusList);
        s.close();
        return statusSet;
    }

    @Override
    public void update(Status status) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(status);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(Status status) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(status);
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
                s.createSQLQuery("SELECT setval('online_publisher.status_id_seq', max(id)) FROM status").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.story_id_seq RESTART WITH 1").executeUpdate();
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
