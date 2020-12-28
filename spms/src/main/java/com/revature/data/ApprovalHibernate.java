package com.revature.data;

import com.revature.beans.Approval;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApprovalHibernate implements ApprovalDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    @Override
    public Approval add(Approval approval){
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(approval);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
          s.close();
        }
        return approval;
    }

    @Override
    public Approval getById(Integer id) {
        Session s = hu.getSession();
        Approval a = s.get(Approval.class, id);
        s.close();
        return a;
    }

    @Override
    public Set<Approval> getAll() {
        Session s = hu.getSession();
        String query = "FROM Approval";
        Query<Approval> q = s.createQuery(query, Approval.class);
        List<Approval> approvalList = q.getResultList();
        Set<Approval> approvalSet = new HashSet<>();
        approvalSet.addAll(approvalList);
        return approvalSet;
    }

    public Set<Approval> getApprovalByEditor(Integer id){
        Session s = hu.getSession();
        String query = "FROM Approval where editor.id = :editor";
        Query<Approval> q = s.createQuery(query, Approval.class);
        q.setParameter("editor", id);
        List<Approval> approvalList = q.getResultList();
        Set<Approval> approvalSet = new HashSet<>();
        approvalSet.addAll(approvalList);
        s.close();
        return approvalSet;
    }

    @Override
    public void update(Approval approval) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(approval);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(Approval approval) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(approval);
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
                s.createSQLQuery("SELECT setval('online_publisher.approval_id_seq', max(id)) FROM approval").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.approval_id_seq RESTART WITH 1").executeUpdate();
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
