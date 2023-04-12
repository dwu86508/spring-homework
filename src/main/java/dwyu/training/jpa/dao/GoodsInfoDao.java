package dwyu.training.jpa.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dwyu.training.jpa.entity.Goods;

@Repository
public interface GoodsInfoDao extends JpaRepository<Goods, Long>{
	
	List<Goods> findByGoodsNameContainingIgnoreCase(String searchKeyword);
	
	List<Goods> findByGoodsNameContainingIgnoreCase(Pageable pageable, String searchKeyword);
	
	List<Goods> findByGoodsName(Pageable pageable, String searchKeyword);
	
	List<Goods> findByGoodsIDIn(Collection<Long> goodsID);
}
