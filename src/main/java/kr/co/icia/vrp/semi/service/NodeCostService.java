package kr.co.icia.vrp.semi.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.icia.vrp.semi.dao.NodeCostDao;
import kr.co.icia.vrp.semi.entity.NodeCost;
import kr.co.icia.vrp.semi.service.base.BaseService;
@Transactional(readOnly = true)
@Service
public class NodeCostService extends BaseService<NodeCost, Long, NodeCostDao> {
}
