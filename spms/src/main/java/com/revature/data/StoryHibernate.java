package com.revature.data;

import com.revature.beans.Request;
import com.revature.beans.Story;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoryHibernate implements StoryDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    @Override
    public Story add(Story story) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(story);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return story;
    }

    @Override
    public Story getById(Integer id) {
        Session s = hu.getSession();
        Story story = s.get(Story.class, id);
        s.close();
        return story;
    }

    @Override
    public Set<Story> getAll() {
        Session s = hu.getSession();
        String query = "FROM Story";
        Query<Story> q = s.createQuery(query, Story.class);
        List<Story> storyList = q.getResultList();
        Set<Story> storySet = new HashSet<>();
        storySet.addAll(storyList);
        s.close();
        return storySet;
    }

    @Override
    public void update(Story story) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(story);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(Story story) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(story);
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
                s.createSQLQuery("SELECT setval('online_publisher.story_id_seq', max(id)) FROM story_type").executeUpdate();
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
