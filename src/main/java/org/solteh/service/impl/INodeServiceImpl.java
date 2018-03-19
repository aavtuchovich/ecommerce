package org.solteh.service.impl;

import org.solteh.dao.INodeDAO;
import org.solteh.model.Node;
import org.solteh.service.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
public class INodeServiceImpl implements INodeService {
    private final INodeDAO nodeDAO;

    @Autowired
    public INodeServiceImpl(INodeDAO nodeDAO) {
        this.nodeDAO = nodeDAO;
    }

    @Override
    public List<Node> getAll() {
        return nodeDAO.getAll();
    }
}
