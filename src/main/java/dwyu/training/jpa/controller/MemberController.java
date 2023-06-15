package dwyu.training.jpa.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dwyu.training.jpa.entity.MemberInfo;
import dwyu.training.jpa.entity.MemberPointsLog;
import dwyu.training.jpa.service.MemberService;
import dwyu.training.jpa.vo.GoodsVo;
import dwyu.training.jpa.vo.MemberInfoVo;
import dwyu.training.jpa.vo.ShoppingCarGoods;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:8086"}, allowCredentials = "true")
@RestController
@RequestMapping("/MemberController")
public class MemberController {
	
	private static Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Resource(name = "sessionMemberInfo")
	private MemberInfoVo sessionMemberInfo;
	
	@Resource(name = "sessionCartGoods")
	private List<GoodsVo> cartGoods;
	
	@Resource(name = "sessionCartGoodsNew")
	private Map<Long,ShoppingCarGoods> cartGoodsNew;
	
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
				.memberID(memberInfoVo.getMemberID())				
				.memberPWD("我不告訴你啊XD")
				.build();
		MemberInfo memberInfo = memberservice.queryGoodsByID(memberInfoVo.getMemberID());
		if(memberInfo==null) {
			member.setLoginMessage("無此帳號，請確認");
		}else if(memberInfoVo.getMemberID().equals(memberInfo.getMemberID()) && memberInfoVo.getMemberPWD().equals(memberInfo.getMemberPWD())) {
			member.setIsLogin(true);
			member.setLoginMessage("登入成功");
			member.setMemberName(memberInfo.getCustomerName());
			member.setMemberPoint(memberInfo.getPoints());
			sessionMemberInfo.setIsLogin(true);
			sessionMemberInfo.setMemberID(memberInfo.getMemberID());
			sessionMemberInfo.setMemberName(memberInfo.getCustomerName());
			sessionMemberInfo.setMemberPWD("我不告訴你啊XD");
			sessionMemberInfo.setMemberPoint(memberInfo.getPoints());;
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
		List<GoodsVo> carGoodsShow = new ArrayList<>();
		carGoodsShow.addAll(cartGoods);
		carGoodsShow.add(goodsVo);
		cartGoods.clear();
		cartGoods.addAll(carGoodsShow);
		return ResponseEntity.ok(carGoodsShow);
	}
	
	@ApiOperation(value = "商品加入購物車")
	@PostMapping(value = "/addCartGoodsNew")
	public ResponseEntity<List<ShoppingCarGoods>> addCartGoodsNew(@RequestBody ShoppingCarGoods shoppingCarGoods) {
		Map<Long,ShoppingCarGoods> carGoodsShow = new LinkedHashMap<>();
		carGoodsShow.putAll(cartGoodsNew);
		if(carGoodsShow.containsKey(shoppingCarGoods.getGoodsID())) {
			carGoodsShow.get(shoppingCarGoods.getGoodsID()).setBuyQuantity(carGoodsShow.get(shoppingCarGoods.getGoodsID()).getBuyQuantity()+1);
		}else {
		shoppingCarGoods.setBuyQuantity(1);
		carGoodsShow.put(shoppingCarGoods.getGoodsID(), shoppingCarGoods);
		}
		cartGoodsNew.clear();
		cartGoodsNew.putAll(carGoodsShow);
		List<ShoppingCarGoods> shoppingCar= new ArrayList<>(carGoodsShow.values());
		return ResponseEntity.ok(shoppingCar);
	}
	
	@ApiOperation(value = "查尋購物車商品")
	@GetMapping(value = "/queryCartGoods")
	public ResponseEntity<List<GoodsVo>> queryCartGoods() {
		
		
		return ResponseEntity.ok(cartGoods);
	}
	
	@ApiOperation(value = "查尋購物車商品")
	@GetMapping(value = "/queryCartGoodsNew")
	public ResponseEntity<List<ShoppingCarGoods>> queryCartGoodsNew() {
		
		List<ShoppingCarGoods> shoppingCar = new ArrayList<>(cartGoodsNew.values());
		return ResponseEntity.ok(shoppingCar);
	}
	
	@ApiOperation(value = "更新購物車商品")
	@PostMapping(value = "/updateCartGoodsNew")
	public ResponseEntity<List<ShoppingCarGoods>> updateCartGoods(@RequestBody ShoppingCarGoods shoppingCarGoods) {
		Map<Long,ShoppingCarGoods> carGoodsShow = new LinkedHashMap<>();
		carGoodsShow.putAll(cartGoodsNew);
		carGoodsShow.get(shoppingCarGoods.getGoodsID()).setBuyQuantity(shoppingCarGoods.getBuyQuantity());;
		cartGoodsNew.clear();
		cartGoodsNew.putAll(carGoodsShow);
		List<ShoppingCarGoods> shoppingCar = new ArrayList<>(carGoodsShow.values());
		return ResponseEntity.ok(shoppingCar);
	}
	
	@ApiOperation(value = "刪除購物車商品")
	@PostMapping(value = "/deleteCartGoodsNew")
	public ResponseEntity<List<ShoppingCarGoods>> deleteCartGoods(@RequestBody ShoppingCarGoods shoppingCarGoods) {
		Map<Long,ShoppingCarGoods> carGoodsShow = new LinkedHashMap<>();
		carGoodsShow.putAll(cartGoodsNew);
		carGoodsShow.remove(shoppingCarGoods.getGoodsID());
		cartGoodsNew.clear();
		cartGoodsNew.putAll(carGoodsShow);
		List<ShoppingCarGoods> shoppingCar = new ArrayList<>(carGoodsShow.values());
		return ResponseEntity.ok(shoppingCar);
	}
	
	@ApiOperation(value = "清空購物車商品")
	@DeleteMapping(value = "/clearCartGoods")
	public ResponseEntity<List<GoodsVo>> clearCartGoods() {
		cartGoods.clear();

		return ResponseEntity.ok(cartGoods);
	}
	
	@ApiOperation(value = "清空購物車商品")
	@DeleteMapping(value = "/clearCartGoodsNew")
	public ResponseEntity<List<ShoppingCarGoods>> clearCartGoodsNew() {
		cartGoodsNew.clear();
		List<ShoppingCarGoods> shoppingCar = new ArrayList<>(cartGoodsNew.values());
		return ResponseEntity.ok(shoppingCar);
	}
	
	@ApiOperation(value = "購物網-查詢會員點數")
	@GetMapping(value = "/memberPoints")
	public ResponseEntity<MemberInfoVo> memberPoints() {		
		logger.info("HttpSession Login:" + httpSession.getId());
		logger.info("Before:" + sessionMemberInfo.toString());
		
		MemberInfo memberInfo = memberservice.queryGoodsByID(sessionMemberInfo.getMemberID());			
		MemberInfoVo member = MemberInfoVo.builder()
				.isLogin(true)				
				.memberID(sessionMemberInfo.getMemberID())				
				.memberPoint(memberInfo.getPoints())
				.build();
		
		logger.info("After:" + sessionMemberInfo.toString());
		
		return ResponseEntity.ok(member);
	}
	
	@ApiOperation(value = "購物網-查詢會員點數紀錄")
	@GetMapping(value = "/memberPointsLog")
	public ResponseEntity<List<MemberPointsLog>> memberPointsLog() {
		
		List<MemberPointsLog> memberPointsLog = memberservice.queryMemberPoints(sessionMemberInfo.getMemberID());
		
		return ResponseEntity.ok(memberPointsLog);
	}
	
}
