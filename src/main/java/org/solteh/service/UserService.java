package org.solteh.service;

import org.solteh.entity.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
