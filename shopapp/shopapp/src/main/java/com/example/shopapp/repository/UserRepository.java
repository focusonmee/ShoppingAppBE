package com.example.shopapp.repository;

import com.example.shopapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber); // Sửa thành existsByPhoneNumber

    Optional<User> findByPhoneNumber(String phoneNumber);

}
