package com.uav.uavapp.repository;

import com.uav.uavapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    User findByAccount(String account);

}
