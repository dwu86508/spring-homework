package dwyu.training.jpa.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

import dwyu.training.jpa.vo.GoodsVo;
import dwyu.training.jpa.vo.MemberInfoVo;


@Configuration
public class MemberSessionConfig {
	
	@Bean
	@SessionScope
	public MemberInfoVo sessionMemberInfo() {
		
		MemberInfoVo member = MemberInfoVo.builder()
				.isLogin(false)
				.loginMessage(null)
				.memberID(null)
				.memberName(null)
				.memberPWD(null)
				.build();
		
		return member;
	}
	
	@Bean
	@SessionScope
	public List<GoodsVo> sessionCartGoods() {
		
		List<GoodsVo> carGoods = new ArrayList<>(); 
		
		return carGoods;
	}
}
