package org.endofusion.endoserver.repository;

import org.endofusion.endoserver.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {

    Page<UserDto> getUsersList(Pageable pageable, UserDto dto);

    List<UserDto> fetchFirstNames();

    List<UserDto> fetchLastNames();

    List<UserDto> fetchUsernames();

    List<UserDto> fetchEmails();
}
