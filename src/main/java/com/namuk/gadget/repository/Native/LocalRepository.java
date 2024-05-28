package com.namuk.gadget.repository.Native;

import com.namuk.gadget.domain.Native;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalRepository extends JpaRepository<Native, Long> {

    Optional<Native> findByNativeId(String id);
}
