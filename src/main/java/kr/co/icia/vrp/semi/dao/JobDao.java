package kr.co.icia.vrp.semi.dao;
import org.apache.ibatis.annotations.Mapper;
import kr.co.icia.vrp.semi.dao.base.BaseDao;
import kr.co.icia.vrp.semi.entity.Job;
@Mapper
public interface JobDao extends BaseDao<Job, Long> {
}
