package com.dxc.production.mimi.dao;

import com.dxc.production.mimi.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findById(long id);
    UserEntity findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.role = 1")
    Page<UserEntity> findAllUserAccount(Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE u.username " +
            "LIKE %:searchText% " +
            "OR u.email LIKE %:searchText% " +
            "OR u.name LIKE %:searchText% ")
    Page<UserEntity> searchAccountByKeyword(Pageable pageable, String searchText);
}
