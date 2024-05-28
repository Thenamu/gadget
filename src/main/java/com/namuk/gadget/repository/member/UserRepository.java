package com.namuk.gadget.repository.member;

import com.namuk.gadget.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // id로 사용자 정보를 가져온다.
    Optional<User> findByUserId(String id);

    // Optional<User> findByRefreshToken(String refreshToke);
}
