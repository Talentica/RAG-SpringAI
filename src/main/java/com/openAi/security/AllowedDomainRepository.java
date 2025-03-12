package com.openAi.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllowedDomainRepository extends JpaRepository<AllowedDomain, Integer> {

    List<AllowedDomain> findAll();
}
