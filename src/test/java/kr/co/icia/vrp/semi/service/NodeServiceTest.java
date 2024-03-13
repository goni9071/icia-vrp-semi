/**
 * 
 */
package kr.co.icia.vrp.semi.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.co.icia.vrp.semi.entity.Node;
import kr.co.icia.vrp.semi.util.KakaoApiUtil;
import kr.co.icia.vrp.semi.util.KakaoApiUtil.Point;

/**
 * 
 */
@SpringBootTest
public class NodeServiceTest {
  @Autowired
  private NodeService nodeService;

  @Test
  public void addTest() throws IOException, InterruptedException {
    Point center = new Point(126.675113024566, 37.4388938204128);// 인천일보아카데미
    List<Point> pointByKeyword = KakaoApiUtil.getPointByKeyword("약국", center);
    for (Point point : pointByKeyword) {
      Node node = new Node();
      node.setId(Long.valueOf(point.getId()));// 노드id
      node.setName(point.getName());
      node.setPhone(point.getPhone());// 전화번호
      node.setAddress(point.getAdddress());// 주소
      node.setX(point.getX());// 경도
      node.setY(point.getY());// 위도
      node.setRegDt(new Date());// 등록일시
      node.setModDt(new Date());// 수정일시
      nodeService.add(node);
    }

  }
}
