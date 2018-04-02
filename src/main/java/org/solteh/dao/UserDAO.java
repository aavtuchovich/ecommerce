package org.solteh.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.solteh.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class UserDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User findAccount(String userName) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.find(User.class, userName);
    }
}
