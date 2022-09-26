package com.example.authentication.repository;

import com.example.authentication.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    Optional <User> findByEmail(String email);

    Optional <User> findByIdAndTokenRefreshTokenAndTokenExpiredAtGreaterThan(Long id, String refreshToken, LocalDateTime expiredAt);
}
