package com.revature.data;

import com.revature.beans.StoryType;
import com.revature.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoryTypeHibernate implements StoryTypeDAO{
    private final HibernateUtil hu = HibernateUtil.getHibernateUtil();

    @Override
    public StoryType add(StoryType storyType){
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.save(storyType);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
        return storyType;
    }

    @Override
    public StoryType getById(Integer id) {
        Session s = hu.getSession();
        StoryType storyType = s.get(StoryType.class, id);
        s.close();
        return storyType;
    }

    @Override
    public Set<StoryType> getAll() {
        Session s = hu.getSession();
        String query = "FROM StoryType";
        Query<StoryType> q = s.createQuery(query, StoryType.class);
        List<StoryType> storyTypeList = q.getResultList();
        Set<StoryType> storyTypeSet = new HashSet<>();
        storyTypeSet.addAll(storyTypeList);
        s.close();
        return storyTypeSet;
    }

    @Override
    public void update(StoryType storyType) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.update(storyType);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public void delete(StoryType storyType) {
        Session s = hu.getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            s.delete(storyType);
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
                s.createSQLQuery("SELECT setval('online_publisher.story_type_id_seq', max(id)) FROM story_type").executeUpdate();
            }else{
                s.createSQLQuery("ALTER SEQUENCE online_publisher.story_type_id_seq RESTART WITH 1").executeUpdate();
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
