package com.namuk.gadget.repository.Native;

import com.namuk.gadget.domain.Native;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NativeRepository extends JpaRepository<Native, Long> {

    boolean existsByNativeId(String id);

    boolean existsByNativeEmail(String email);

    boolean existsByNativePhoneNumber(String phoneNumber);

    Native findByNativeId(String id);
}
