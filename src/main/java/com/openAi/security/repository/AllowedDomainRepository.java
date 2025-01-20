package com.openAi.security.repository;

import com.drew.lang.annotations.NotNull;
import com.openAi.security.entity.AllowedDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllowedDomainRepository extends JpaRepository<AllowedDomain, Integer> {

    @NotNull
    List<AllowedDomain> findAll();
    @NotNull List<AllowedDomain> findAllByDomainName(String domainName);
    List<AllowedDomain> findByTeamIdsContains(String teamId);
}
