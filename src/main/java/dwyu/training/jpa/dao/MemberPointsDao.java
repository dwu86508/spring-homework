package dwyu.training.jpa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dwyu.training.jpa.entity.MemberPointsLog;

@Repository
public interface MemberPointsDao extends JpaRepository<MemberPointsLog, String>{
	
	List<MemberPointsLog> findByCustomerID (String customerID);

}
