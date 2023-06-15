package dwyu.training.jpa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dwyu.training.jpa.dao.MemberDao;
import dwyu.training.jpa.dao.MemberPointsDao;
import dwyu.training.jpa.entity.MemberInfo;
import dwyu.training.jpa.entity.MemberPointsLog;

@Service
public class MemberService {
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private MemberPointsDao memberPointsDao; 

	public MemberInfo queryGoodsByID(String memberID){
		Optional<MemberInfo> memberInfos = memberDao.findById(memberID);
		MemberInfo memberInfo = (memberInfos.isPresent()) ? memberInfos.get() : null;
		return memberInfo;
	}
	
	public List<MemberPointsLog> queryMemberPoints(String memberID){
		List<MemberPointsLog> memeberPointsLog = memberPointsDao.findByCustomerID(memberID);		
		return memeberPointsLog;
	}
	
	
	
}
