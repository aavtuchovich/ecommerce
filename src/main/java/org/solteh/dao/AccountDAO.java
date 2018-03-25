package org.solteh.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.solteh.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public class AccountDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public AccountDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Account findAccount(String userName) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.find(Account.class, userName);
    }
}
