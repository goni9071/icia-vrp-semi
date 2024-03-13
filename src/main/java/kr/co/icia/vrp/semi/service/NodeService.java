package kr.co.icia.vrp.semi.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.icia.vrp.semi.dao.NodeDao;
import kr.co.icia.vrp.semi.entity.Node;
import kr.co.icia.vrp.semi.service.base.BaseService;
@Transactional(readOnly = true)
@Service
public class NodeService extends BaseService<Node, Long, NodeDao> {
}
