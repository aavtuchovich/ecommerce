package org.solteh.service.impl;

import org.solteh.dao.INodeDAO;
import org.solteh.dao.IProductDAO;
import org.solteh.model.Node;
import org.solteh.model.Product;
import org.solteh.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IProductServiceImpl implements IProductService {
    private final IProductDAO productDAO;

    @Autowired
    public IProductServiceImpl(IProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> getAll() {
        return productDAO.getAll();
    }
}
