package com.revature.data;

import com.revature.beans.User;
import com.revature.exception.NonUniqueUsernameException;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserHibernate implements UserDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();
    @Override
    public User add(User user) throws NonUniqueUsernameException {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(user);
            tx.commit();
        } catch (Exception e) {
            if (e.getMessage().contains("violates unique constraint")){
                throw new NonUniqueUsernameException();
            }
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return user;
    }

    @Override
    public User getById(Integer id) {
        Session s = hu.getSession();
        User user = s.get(User.class, id);
        s.close();
        return user;
    }

    @Override
    public Set<User> getAll() {
        Session s = hu.getSession();
        String query = "FROM User";
        Query<User> q = s.createQuery(query, User.class);
        List<User> userList = q.getResultList();
        Set<User> userSet = new HashSet<>();
        userSet.addAll(userList);
        s.close();
        return userSet;
    }

    @Override
    public void update(User user) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(User user) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
            resetSequence();
        }
    }

    @Override
    public User getByUsername(String username) {
        Session s = hu.getSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> root = criteria.from(User.class);

        Predicate predicateForUsername = cb.equal(root.get("username"), username);
        criteria.select(root).where(predicateForUsername);
        User user = s.createQuery(criteria).getSingleResult();
        return user;
    }

    public void resetSequence(){
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            if (getAll().size() > 0){
                s.createSQLQuery("SELECT setval('online_publisher.user_login_id_seq', max(id)) FROM user_login").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.user_login_id_seq RESTART WITH 1").executeUpdate();
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
