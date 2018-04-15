package org.solteh.repository;

import org.solteh.model.SubscribeEmails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailsRepository extends JpaRepository<SubscribeEmails, Long> {
}
