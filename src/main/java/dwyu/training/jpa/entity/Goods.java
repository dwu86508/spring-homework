package dwyu.training.jpa.entity;


import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@NoArgsConstructor
@Data
@ToString(exclude = {"orders"})
@Entity
@Table(name = "BEVERAGE_GOODS")
public class Goods {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BEVERAGE_GOODS_SEQ_GEN")
    @SequenceGenerator(name = "BEVERAGE_GOODS_SEQ_GEN", sequenceName = "BEVERAGE_GOODS_SEQ", allocationSize = 1)
	@Column(name = "GOODS_ID")
	private Long goodsID;	
	
	@Column(name = "GOODS_NAME")
	private String goodsName;
	
	@Column(name = "DESCRIPTION")
	private String goodsDescription;
	
	@Column(name = "PRICE")
	private Integer goodsPrice;
	
	@Column(name = "QUANTITY")
	private Integer goodsQuantity;

	@Column(name = "IMAGE_NAME")
	private String goodsImageName;
	
	@Column(name = "STATUS")
	private String goodsStatus;
	
	@JsonIgnore
	@OneToMany(
		// 透過mappedBy來連結雙向之間的關係，所對映的物件欄位名稱(除非關係是單向的，否則此參數必須)
		mappedBy = "goods",
		cascade = {CascadeType.ALL}, orphanRemoval = true,
		fetch = FetchType.LAZY // LAZY(只在用到時才載入關聯的物件)		
	)
	@OrderBy(value = "orderID")
	private List<Orders> orders;
	
}
