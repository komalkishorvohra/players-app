package com.example.players.repository;

import com.example.players.entity.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, String> {
    ApiUser findByUsername(String username);
}
