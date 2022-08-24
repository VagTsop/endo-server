package org.endofusion.endoserver.repository;

import org.endofusion.endoserver.domain.User;
import org.endofusion.endoserver.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.isActive = TRUE WHERE a.email = ?1")
    void enableAppUser(String email);

    Page<UserDto> getUsersList(Pageable pageable, UserDto dto);

    List<UserDto> fetchFirstNames();

    List<UserDto> fetchLastNames();

    List<UserDto> fetchUsernames();

    List<UserDto> fetchEmails();
}

