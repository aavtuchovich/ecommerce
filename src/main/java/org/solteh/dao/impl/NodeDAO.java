package org.solteh.dao.impl;

import org.solteh.dao.INodeDAO;
import org.solteh.model.Node;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class NodeDAO implements INodeDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Node> getAll() {
        List<Node> result = entityManager.createQuery("SELECT n FROM Node n").getResultList();
        return result;
    }
}
