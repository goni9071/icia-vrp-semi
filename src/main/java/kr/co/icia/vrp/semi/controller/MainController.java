package kr.co.icia.vrp.semi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.icia.vrp.semi.entity.Node;
import kr.co.icia.vrp.semi.entity.NodeCost;
import kr.co.icia.vrp.semi.parameter.NodeCostParam;
import kr.co.icia.vrp.semi.service.NodeCostService;
import kr.co.icia.vrp.semi.service.NodeService;
import kr.co.icia.vrp.semi.util.JsonResult;
import kr.co.icia.vrp.semi.util.KakaoApiUtil;
import kr.co.icia.vrp.semi.util.KakaoApiUtil.Point;

@Controller
public class MainController {
  @Autowired
  private NodeService nodeService;
  @Autowired
  private NodeCostService nodeCostService;

  @GetMapping("/main")
  public String getMain() {
    return "main";
  }

  @GetMapping("/poi")
  @ResponseBody
  public JsonResult getPoi(@RequestParam double x, @RequestParam double y) throws IOException, InterruptedException {
    Point center = new Point(x, y);// 중심좌표
    List<Point> pointList = KakaoApiUtil.getPointByKeyword("약국", center);
    List<Node> nodeList = new ArrayList<>();
    for (Point point : pointList) {
      Node node = nodeService.getOne(Long.valueOf(point.getId()));
      if (node == null) {
        node = new Node();
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
      nodeList.add(node);
    }
    for (int i = 1; i < nodeList.size(); i++) {
      Node prev = nodeList.get(i - 1);
      Node next = nodeList.get(i);

      NodeCostParam nodeCostParam = new NodeCostParam();
      nodeCostParam.setStartNodeId(prev.getId());
      nodeCostParam.setEndNodeId(next.getId());
      NodeCost nodeCost = nodeCostService.getOneByParam(nodeCostParam);
      if (nodeCost == null) {
        List<Point> vehiclePaths = KakaoApiUtil.getVehiclePaths(new Point(prev.getX(), next.getY()),
            new Point(next.getX(), next.getY()));
        nodeCost = new NodeCost();
        nodeCost.setStartNodeId(prev.getId());//시작노드id
        nodeCost.setEndNodeId(next.getId());//종료노드id
        nodeCost.setDistanceMeter();//이동거리(미터)
        nodeCost.setDurationSecond();//이동시간(초)
        nodeCost.setTollFare();//통행 요금(톨게이트)
        nodeCost.setTaxiFare();//택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
        nodeCost.setFuelPrice();//해당 시점의 전국 평균 유류비와 연비를 감안한 유류비
        nodeCost.setPathJson();//이동경로json [[x,y],[x,y]]
        nodeCost.setRegDt(new Date());//등록일시
        nodeCost.setModDt(new Date());//수정일시
      }
    }
    return new JsonResult();
  }
}
