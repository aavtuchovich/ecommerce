package org.solteh.service.impl;

import org.solteh.dao.IWebUserDAO;
import org.solteh.model.WebUser;
import org.solteh.service.IWebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IWebUserServiceImpl implements IWebUserService {
    @Autowired
    private IWebUserDAO iWebUserDAO;

    @Override
    public WebUser getUserByLogin(String login) {
        return iWebUserDAO.getUserByLogin(login);
    }

    @Override
    public void saveUser(WebUser user) {
        iWebUserDAO.saveUser(user);
    }

    @Override
    public List<WebUser> getAll() {
        return iWebUserDAO.getAll();
    }
}
