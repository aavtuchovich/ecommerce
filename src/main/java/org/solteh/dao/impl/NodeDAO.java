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
        Node ndm = new Node();
        ndm.setDescription("main1");
        Node ndm1 = new Node();
        ndm1.setDescription("sub1");
        Node ndm2 = new Node();
        ndm2.setDescription("sub2");
        Node nds1 = new Node();
        nds1.setDescription("presub1");
        Node nds2 = new Node();
        nds2.setDescription("presub2");
        ndm1.addChild(nds1);
        ndm1.addChild(nds2);
        ndm.addChild(ndm1);
        ndm.addChild(ndm2);
        entityManager.persist(ndm);
        List<Node> result = entityManager.createQuery("SELECT n FROM Node n").getResultList();
        return result;
    }
}
