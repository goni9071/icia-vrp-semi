package kr.co.icia.vrp.semi.controller;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.icia.vrp.semi.util.JsonResult;
import kr.co.icia.vrp.semi.util.JsonResult.Code;

@SpringBootTest
public class MainControllerTest {
  @Autowired
  private MainController mainController;

  @Test
  public void getPoiTest() throws IOException, InterruptedException {
    JsonResult jsonResult = mainController.getPoi(126.675113024566, 37.4388938204128);// 인천일보아카데미

    Code code = jsonResult.getCode();
    Map<String, Object> data = jsonResult.getData();
    System.out.println("code : " + code);

    System.out.println("전체이동거리 : " + data.get("totalDistance"));// 전체이동거리
    System.out.println("전체이동시간 : " + data.get("totalDuration"));// 전체이동시간
    System.out.println("전체이동경로 : " + new ObjectMapper().writeValueAsString(data.get("totalPathPointList")));// 전체이동경로
    System.out.println("방문지목록 : " + new ObjectMapper().writeValueAsString(data.get("nodeList")));// 방문지목록
  }
}
