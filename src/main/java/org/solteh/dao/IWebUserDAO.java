package org.solteh.dao;

import org.solteh.model.WebUser;

public interface IWebUserDAO extends ISuperDAO<WebUser> {
    WebUser getUserByLogin(String login);
}
