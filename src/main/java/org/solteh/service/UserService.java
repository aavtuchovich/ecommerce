package org.solteh.service;

import org.solteh.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
