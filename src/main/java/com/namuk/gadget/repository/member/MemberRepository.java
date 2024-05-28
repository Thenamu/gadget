package com.namuk.gadget.repository.member;

import com.namuk.gadget.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<User, Long> {

    User findByUserId(String id);

    boolean existsByUserId(String id);

    boolean existsByUserEmail(String email);

    boolean existsByUserPhoneNumber(String phoneNumbers);
}