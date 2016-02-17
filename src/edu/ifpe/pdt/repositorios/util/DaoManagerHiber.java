/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ifpe.pdt.repositorios.util;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

/**
 *
 * @author Eduardo
 */
public class DaoManagerHiber {
    private static DaoManagerHiber myself = null;
    
    private SessionFactory sessionFactory;
    private Session s;

    private DaoManagerHiber(){

    try{

        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        s = sessionFactory.openSession();
        

    }catch(Throwable th){

        System.err.println("Enitial SessionFactory creation failed"+th);

        throw new ExceptionInInitializerError(th);

    }

  }
    
    public static DaoManagerHiber getInstance(){
        if(myself==null)
            myself = new DaoManagerHiber();
        
        return myself;
    }
  
    public void persist(Object o){
        
        Transaction tr = null;
        
        s.close();
        
        s = sessionFactory.openSession();
        
        tr = s.beginTransaction();
        
        s.save(o);
        
        tr.commit();
        
    }
    
    public List recover(String hql){
        Transaction tr = null;
        
        s.close();
        
         s = sessionFactory.openSession();
         
         tr = s.beginTransaction();
        
        Query query = (Query) s.createQuery(hql);
        
        return query.list();
    }
    
    public List recoverSQL(String sql){
        Transaction tr = null;
        
        s.close();
        
        s = sessionFactory.openSession();
        
        tr = s.beginTransaction();  
        
        Query query = (Query) s.createSQLQuery(sql);
        
        return query.list();
    }
    
    public List recover(Object o){
        
        Criteria c = s.createCriteria(o.getClass());
        
        c.add(Example.create(o).enableLike(MatchMode.ANYWHERE).ignoreCase().excludeProperty("codigo"));
        
        List l = c.list();
        s.close();
        
        return l;
    }
    
    public void update(Object o) {
        Transaction tr = null;
        
            s.close();
            s = sessionFactory.openSession();
            tr = s.beginTransaction();  
        
        
        s.update(o);
        
        tr.commit();
        
        
    }
    
    public void delete(Object o){
        Transaction tr = null;
        
            s.close();
            s=sessionFactory.openSession();
            tr = s.beginTransaction();
        s.delete(o);
        
        tr.commit();
    }
    
}

