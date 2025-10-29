package com.example.leave_request_workflow.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.leave_request_workflow.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // emailをキーにして検索する
    Optional<User> findByEmail(String email);
}
