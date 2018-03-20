package org.solteh.service;

import org.solteh.dao.IProductDAO;
import org.solteh.model.Node;
import org.solteh.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductService {
    public List<Product> getAll();
}
