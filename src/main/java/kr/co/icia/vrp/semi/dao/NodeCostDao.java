package kr.co.icia.vrp.semi.dao;
import org.apache.ibatis.annotations.Mapper;
import kr.co.icia.vrp.semi.dao.base.BaseDao;
import kr.co.icia.vrp.semi.entity.NodeCost;
@Mapper
public interface NodeCostDao extends BaseDao<NodeCost, Long> {
}
