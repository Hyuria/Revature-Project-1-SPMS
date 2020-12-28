package com.revature.data;

import com.revature.beans.Genre;
import com.revature.beans.Request;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestHibernate implements RequestDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    @Override
    public Request add(Request request){
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(request);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return request;
    }

    @Override
    public Request getById(Integer id) {
        Session s = hu.getSession();
        Request request = s.get(Request.class, id);
        s.close();
        return request;
    }

    @Override
    public Set<Request> getAll() {
        Session s = hu.getSession();
        String query = "FROM Request";
        Query<Request> q = s.createQuery(query, Request.class);
        List<Request> requestList = q.getResultList();
        Set<Request> requestSet = new HashSet<>();
        requestSet.addAll(requestList);
        s.close();
        return requestSet;
    }

    @Override
    public void update(Request request) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(request);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(Request request) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(request);
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
                s.createSQLQuery("SELECT setval('online_publisher.request_id_seq', max(id)) FROM request").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.request_id_seq RESTART WITH 1").executeUpdate();
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
