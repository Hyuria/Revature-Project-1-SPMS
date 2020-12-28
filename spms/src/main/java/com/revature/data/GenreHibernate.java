package com.revature.data;

import com.revature.beans.Committee;
import com.revature.beans.Genre;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenreHibernate implements GenreDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    @Override
    public Genre add(Genre genre){
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(genre);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return genre;
    }

    @Override
    public Genre getById(Integer id) {
        Session s = hu.getSession();
        Genre genre = s.get(Genre.class, id);
        s.close();
        return genre;
    }

    @Override
    public Set<Genre> getAll() {
        Session s = hu.getSession();
        String query = "FROM Genre";
        Query<Genre> q = s.createQuery(query, Genre.class);
        List<Genre> genreList = q.getResultList();
        Set<Genre> genreSet = new HashSet<>();
        genreSet.addAll(genreList);
        s.close();
        return genreSet;
    }

    @Override
    public void update(Genre genre) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(genre);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(Genre genre) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(genre);
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
                s.createSQLQuery("SELECT setval('online_publisher.genre_id_seq', max(id)) FROM genre").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.genre_id_seq RESTART WITH 1").executeUpdate();
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
