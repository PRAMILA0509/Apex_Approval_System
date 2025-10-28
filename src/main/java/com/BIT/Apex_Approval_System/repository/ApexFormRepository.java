
package com.BIT.Apex_Approval_System.repository;

import com.BIT.Apex_Approval_System.model.ApexForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApexFormRepository extends JpaRepository<ApexForm, Long> {

}

