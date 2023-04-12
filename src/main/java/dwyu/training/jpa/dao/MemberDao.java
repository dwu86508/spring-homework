package dwyu.training.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dwyu.training.jpa.entity.MemberInfo;

@Repository
public interface MemberDao extends JpaRepository<MemberInfo, String>{
	
	
}

