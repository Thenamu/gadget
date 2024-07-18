package com.namuk.gadget.repository.token;

import com.namuk.gadget.domain.RefreshTokens;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface RefreshTokenRepository extends JpaRepository<RefreshTokens, Long> {

    void deleteByRefreshToken(String refreshToken);

    RefreshTokens findByRefreshToken(String refreshToken);
}
