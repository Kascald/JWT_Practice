package com.onboarding.preonboarding.repository;

import com.onboarding.preonboarding.entity.User;
import jakarta.transaction.TransactionRolledbackException;
import jakarta.transaction.TransactionalException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
