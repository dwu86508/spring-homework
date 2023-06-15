package dwyu.training.jpa.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;


import dwyu.training.jpa.vo.GoodsVo;
import dwyu.training.jpa.vo.MemberInfoVo;
import dwyu.training.jpa.vo.ShoppingCarGoods;


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
	
	@Bean
	@SessionScope
	public Map<Long,ShoppingCarGoods> sessionCartGoodsNew() {
		
		Map<Long,ShoppingCarGoods> carGoods = new LinkedHashMap<>(); 
		
		return carGoods;
	}
	
	
}
