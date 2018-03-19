package org.solteh.dao.impl;

import org.hibernate.search.query.dsl.EntityContext;
import org.solteh.dao.IWebUserDAO;
import org.solteh.model.WebUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class WebUserDAO implements IWebUserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public WebUser getUserByLogin(String login) {
        return (WebUser) entityManager.createQuery("SELECT u FROM users u where u.login= :login")
                .setParameter("login", login).getSingleResult();
    }

    @Override
    public List<WebUser> getAll() {
        return entityManager.createQuery("SELECT u FROM users u",WebUser.class).getResultList();
    }
}
