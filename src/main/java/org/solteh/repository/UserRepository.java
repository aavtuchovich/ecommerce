package org.solteh.repository;

import org.solteh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

    @Override
    User getOne(Long aLong);
}
