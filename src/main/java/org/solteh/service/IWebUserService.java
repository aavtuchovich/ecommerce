package org.solteh.service;

import org.solteh.model.WebUser;

import java.util.List;

public interface IWebUserService {
    WebUser getUserByLogin(String login);
    void saveUser(WebUser user);
    List<WebUser> getAll();
}
