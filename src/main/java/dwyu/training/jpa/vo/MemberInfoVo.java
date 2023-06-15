package dwyu.training.jpa.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Builder
@Data
@ToString

public class MemberInfoVo {	
	
	private Boolean isLogin;
	
	private String loginMessage;

	private String memberID;
	
	private String memberPWD;
	
	private String memberName;
	
	private long memberPoint;
	
}
