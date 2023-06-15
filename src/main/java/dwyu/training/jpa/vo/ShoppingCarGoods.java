package dwyu.training.jpa.vo;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dwyu.training.jpa.vo.OrderVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Data

public class ShoppingCarGoods {

	private long goodsID;
	
	private String goodsName;

	private String goodsDescription;	

	private int goodsPrice;

	private int buyQuantity;

	private String goodsImageName;

	private String goodsStatus;
	
}
