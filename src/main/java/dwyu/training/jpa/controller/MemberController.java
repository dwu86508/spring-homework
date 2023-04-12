package dwyu.training.jpa.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dwyu.training.jpa.entity.MemberInfo;
import dwyu.training.jpa.service.MemberService;
import dwyu.training.jpa.vo.GoodsVo;
import dwyu.training.jpa.vo.MemberInfoVo;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/MemberController")
public class MemberController {
	
	private static Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Resource(name = "sessionMemberInfo")
	private MemberInfoVo sessionMemberInfo;
	
	@Resource(name = "sessionCartGoods")
	private List<GoodsVo> cartGoods;
	
	@Autowired
	private HttpSession httpSession; 
	
	@Autowired
	private MemberService memberservice;

	@ApiOperation(value = "購物網-會員-檢查登入")
	@GetMapping(value = "/checkLogin")
	public ResponseEntity<MemberInfoVo> checkLogin(Model model) {
		
		logger.info("HttpSession checkLogin:" + httpSession.getId());
		logger.info("CheckLogin:" + sessionMemberInfo.toString());		
		MemberInfoVo member =
				MemberInfoVo.builder()
				.isLogin(sessionMemberInfo.getIsLogin())
//				.loginMessage(sessionMemberInfo.getLoginMessage())
				.memberID(sessionMemberInfo.getMemberID())
				.memberName(sessionMemberInfo.getMemberName())
				.memberPWD(sessionMemberInfo.getMemberPWD())
				.build();		
		member.setLoginMessage((sessionMemberInfo.getIsLogin()) ? "已登入" : "尚未登入");
		
		
		return ResponseEntity.ok(member);
	}
	
	@ApiOperation(value = "購物網-會員-登入")
	@PostMapping(value = "/login")
	public ResponseEntity<MemberInfoVo> login(@RequestBody MemberInfoVo memberInfoVo) {		
		logger.info("HttpSession Login:" + httpSession.getId());
		logger.info("Before:" + sessionMemberInfo.toString());
		MemberInfoVo member = MemberInfoVo.builder()
				.isLogin(false)
				.loginMessage(null)
				.memberID(memberInfoVo.getMemberID())
				.memberName(null)
				.memberPWD("我不告訴你啊XD")
				.build();
		MemberInfo memberInfo = memberservice.queryGoodsByID(memberInfoVo.getMemberID());
		if(memberInfo==null) {
			member.setLoginMessage("無此帳號，請確認");
		}else if(memberInfoVo.getMemberID().equals(memberInfo.getMemberID()) && memberInfoVo.getMemberPWD().equals(memberInfo.getMemberPWD())) {
			member.setIsLogin(true);
			member.setLoginMessage("登入成功");
			member.setMemberName(memberInfo.getCustomerName());
			sessionMemberInfo.setIsLogin(true);
			sessionMemberInfo.setMemberID(memberInfo.getMemberID());
			sessionMemberInfo.setMemberName(memberInfo.getCustomerName());
			sessionMemberInfo.setMemberPWD("我不告訴你啊XD");
			httpSession.setAttribute("memberInfo", member);
		}else {
			member.setLoginMessage("帳號或密碼錯誤，請確認");
		}
		logger.info("After:" + sessionMemberInfo.toString());
		
		return ResponseEntity.ok(member);
	}
	
	@ApiOperation(value = "購物網-會員-登出")
	@GetMapping(value = "/logout")
	public ResponseEntity<MemberInfoVo> logout() {
		
		logger.info("HttpSession logout:" + httpSession.getId());		
		MemberInfoVo member = MemberInfoVo.builder().build();
		member.setIsLogin(false);
		member.setLoginMessage("已登出");
		sessionMemberInfo.setIsLogin(false);
		sessionMemberInfo.setMemberID(null);
		sessionMemberInfo.setMemberName(null);
		sessionMemberInfo.setMemberPWD(null);
		
		return ResponseEntity.ok(member);
		
		
	}
	
	@ApiOperation(value = "商品加入購物車")
	@PostMapping(value = "/addCartGoods")
	public ResponseEntity<List<GoodsVo>> addCartGoods(@RequestBody GoodsVo goodsVo) {
		/*
			{
			  "goodsID": 28,
			  "goodsName": "Java Chip",
			  "description": "暢銷口味之一，以摩卡醬、乳品及可可碎片調製，加上細緻鮮奶油及摩卡醬，濃厚的巧克力風味。",
			  "imageName": "20130813154445805.jpg",
			  "price": 145,
			  "quantity": 17
			}

			{
			  "goodsID": 3,
			  "goodsName": "柳橙檸檬蜂蜜水",
			  "description": "廣受喜愛的蜂蜜水，搭配柳橙與檸檬汁，酸甜的好滋味，尾韻更帶有柑橘清香。",
			  "imageName": "2021110210202761.jpg",
			  "price": 20,
			  "quantity": 16
			}
		 */
		List<GoodsVo> carGoodsShow = new ArrayList<>();
		carGoodsShow.addAll(cartGoods);
		carGoodsShow.add(goodsVo);
		cartGoods.clear();
		cartGoods.addAll(carGoodsShow);
		return ResponseEntity.ok(carGoodsShow);
	}
	
	@ApiOperation(value = "查尋購物車商品")
	@GetMapping(value = "/queryCartGoods")
	public ResponseEntity<List<GoodsVo>> queryCartGoods() {
		
		
		return ResponseEntity.ok(cartGoods);
	}
	
	@ApiOperation(value = "清空購物車商品")
	@DeleteMapping(value = "/clearCartGoods")
	public ResponseEntity<List<GoodsVo>> clearCartGoods() {
		cartGoods.clear();

		return ResponseEntity.ok(cartGoods);
	}
	
}
