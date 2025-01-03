package com.openAi.security.repository;

import com.openAi.security.entity.CustomerCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCredentialRepository extends JpaRepository<CustomerCredentials, Integer>, JpaSpecificationExecutor<CustomerCredentials> {
     List<CustomerCredentials> findAllByUserNameOrUserEmail(String userName,String userEmail);
     List<CustomerCredentials> findAllByUserEmail(String userEmail);

}
