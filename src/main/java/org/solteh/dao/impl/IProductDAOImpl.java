package org.solteh.dao.impl;

import org.solteh.dao.IProductDAO;
import org.solteh.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class IProductDAOImpl implements IProductDAO {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Product> getAll() {
        return (List<Product>) entityManager.createQuery("SELECT p FROM Product p").getResultList();
    }
}
