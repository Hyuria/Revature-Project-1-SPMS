package com.revature.data;

import com.revature.beans.Author;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthorHibernate implements AuthorDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    @Override
    public Author add(Author author) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(author);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return author;
    }

    @Override
    public Author getById(Integer id) {
        Session s = hu.getSession();
        Author author = s.get(Author.class, id);
        s.close();
        return author;
    }

    @Override
    public Set<Author> getAll() {
        Session s = hu.getSession();
        String query = "FROM Author";
        Query<Author> q = s.createQuery(query, Author.class);
        List<Author> authorList = q.getResultList();
        Set<Author> authorSet = new HashSet<>();
        authorSet.addAll(authorList);
        s.close();
        return authorSet;
    }

    @Override
    public void update(Author author) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(author);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(Author author) {
            Session s = hu.getSession();
            Transaction tx = null;
            try {
                tx = s.beginTransaction();
                s.delete(author);
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
                s.createSQLQuery("SELECT setval('online_publisher.author_id_seq', max(id)) FROM author").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.author_id_seq RESTART WITH 1").executeUpdate();
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
