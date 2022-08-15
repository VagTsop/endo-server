package org.endofusion.endoserver.repository;

import org.endofusion.endoserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isActive = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);
}

