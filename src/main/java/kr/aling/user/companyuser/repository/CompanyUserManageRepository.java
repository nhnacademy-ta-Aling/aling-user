package kr.aling.user.companyuser.repository;

import kr.aling.user.companyuser.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CompanyUserManageRepository.
 *
 * @author : 여운석
 * @since : 1.0
 **/
public interface CompanyUserManageRepository extends JpaRepository<CompanyUser, Long> {

}
