package com.fatihbozik.aviationroutefinder.repository;

import com.fatihbozik.aviationroutefinder.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
}
