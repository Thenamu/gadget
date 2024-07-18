package com.namuk.gadget.repository.member;

import com.namuk.gadget.domain.User;
import com.namuk.gadget.dto.member.UserProfileResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<User, Long> {

    User findByUserId(String id);

    boolean existsByUserId(String id);

    boolean existsByUserEmail(String email);

    boolean existsByUserPhoneNumber(String phoneNumbers);

    @Query(value = "select new com.namuk.gadget.dto.member.UserProfileResponseDTO(u.userId, u.userEmail, u.userPhoneNumber, u.userPassword) " +
            "from User u " +
            "where u.userId = :id")
    UserProfileResponseDTO findUserById(String id);
}