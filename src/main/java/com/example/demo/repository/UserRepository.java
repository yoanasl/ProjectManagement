package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

//    User findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByProjectsId(Long projectId);
    Optional<User> findByEmail(String email);

}

