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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.icia.vrp.semi.entity.Node;
import kr.co.icia.vrp.semi.entity.NodeCost;
import kr.co.icia.vrp.semi.parameter.NodeCostParam;
import kr.co.icia.vrp.semi.service.NodeCostService;
import kr.co.icia.vrp.semi.service.NodeService;
import kr.co.icia.vrp.semi.util.JsonResult;
import kr.co.icia.vrp.semi.util.KakaoApiUtil;
import kr.co.icia.vrp.semi.util.KakaoApiUtil.Point;
import kr.co.icia.vrp.semi.util.kakao.KakaoDirections;
import kr.co.icia.vrp.semi.util.kakao.KakaoDirections.Route;
import kr.co.icia.vrp.semi.util.kakao.KakaoDirections.Route.Section;
import kr.co.icia.vrp.semi.util.kakao.KakaoDirections.Route.Section.Road;
import kr.co.icia.vrp.semi.util.kakao.KakaoDirections.Route.Summary;
import kr.co.icia.vrp.semi.util.kakao.KakaoDirections.Route.Summary.Fare;

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

    int totalDistance = 0;
    int totalDuration = 0;
    List<Point> totalPathPointList = new ArrayList<>();
    for (int i = 1; i < nodeList.size(); i++) {
      Node prev = nodeList.get(i - 1);
      Node next = nodeList.get(i);

      NodeCostParam nodeCostParam = new NodeCostParam();
      nodeCostParam.setStartNodeId(prev.getId());
      nodeCostParam.setEndNodeId(next.getId());
      NodeCost nodeCost = nodeCostService.getOneByParam(nodeCostParam);

      if (nodeCost == null) {
        KakaoDirections kakaoDirections = KakaoApiUtil.getKakaoDirections(new Point(prev.getX(), next.getY()),
            new Point(next.getX(), next.getY()));
        List<Route> routes = kakaoDirections.getRoutes();
        Route route = routes.get(0);
        List<Point> pathPointList = new ArrayList<Point>();
        List<Section> sections = route.getSections();

        if (sections == null) {
          // {"trans_id":"018e3d7f7526771d9332cb717909be8f","routes":[{"result_code":104,"result_msg":"출발지와
          // 도착지가 5 m 이내로 설정된 경우 경로를 탐색할 수 없음"}]}
          continue;
        }
        List<Road> roads = sections.get(0).getRoads();
        for (Road road : roads) {
          List<Double> vertexes = road.getVertexes();
          for (int q = 0; q < vertexes.size(); q++) {
            pathPointList.add(new Point(vertexes.get(q), vertexes.get(++q)));
          }
        }
        Summary summary = route.getSummary();
        Integer distance = summary.getDistance();
        Integer duration = summary.getDuration();
        Fare fare = summary.getFare();
        Integer taxi = fare.getTaxi();
        Integer toll = fare.getToll();

        nodeCost = new NodeCost();
        nodeCost.setStartNodeId(prev.getId());// 시작노드id
        nodeCost.setEndNodeId(next.getId());// 종료노드id
        nodeCost.setDistanceMeter(distance.longValue());// 이동거리(미터)
        nodeCost.setDurationSecond(duration.longValue());// 이동시간(초)
        nodeCost.setTollFare(toll);// 통행 요금(톨게이트)
        nodeCost.setTaxiFare(taxi);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
        nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
        nodeCost.setRegDt(new Date());// 등록일시
        nodeCost.setModDt(new Date());// 수정일시
        nodeCostService.add(nodeCost);
      }

      totalDistance += nodeCost.getDistanceMeter();
      totalDuration += nodeCost.getDurationSecond();
      totalPathPointList.addAll(new ObjectMapper().readValue(nodeCost.getPathJson(), new TypeReference<List<Point>>() {
      }));
    }
    JsonResult jsonResult = new JsonResult();
    jsonResult.addData("totalDistance", totalDistance);// 전체이동거리
    jsonResult.addData("totalDuration", totalDuration);// 전체이동시간
    jsonResult.addData("totalPathPointList", totalPathPointList);// 전체이동경로
    jsonResult.addData("nodeList", nodeList);// 방문지목록
    return jsonResult;
  }
}
