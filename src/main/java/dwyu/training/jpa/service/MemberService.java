package dwyu.training.jpa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dwyu.training.jpa.dao.MemberDao;
import dwyu.training.jpa.entity.MemberInfo;

@Service
public class MemberService {
	
	@Autowired
	private MemberDao memberDao; 

	public MemberInfo queryGoodsByID(String memberID){
		Optional<MemberInfo> memberInfos = memberDao.findById(memberID);
		MemberInfo memberInfo = (memberInfos.isPresent()) ? memberInfos.get() : null;
		return memberInfo;
	}
	
	
	
}
