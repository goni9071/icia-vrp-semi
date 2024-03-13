package kr.co.icia.vrp.semi.vrp;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class VrpServiceTest {

  private List<String> 위치목록 = Arrays.asList("제약회사", "약국01", "약국02", "약국03", "약국04");

  /**
   * 제약회사에서 1대의 차량으로 3개의 감기약을 받아서 3개의 약국에 배달하기. 모든 위치 간의 이동시간은 10분.
   */
  @Test
  public void 기본_예제() {
    VrpService vrpService = new VrpService();
    vrpService.addVehicle("차량01", "제약회사");

    vrpService.addShipement("감기약01", "제약회사", "약국01");
    vrpService.addShipement("감기약02", "제약회사", "약국02");
    vrpService.addShipement("감기약03", "제약회사", "약국03");

    for (int i = 0; i < 위치목록.size(); i++) {
      for (int j = 0; j < 위치목록.size(); j++) {
        // 모든 위치끼리는 10분 걸림.
        // 같은 위치는 0분.
        vrpService.addCost(위치목록.get(i), 위치목록.get(j), i == j ? 0L : 10L);
      }
    }

    VrpResult vrpResult = vrpService.getVrpResult();

    System.out.println("총 시간:" + vrpResult.getTotalTime());
    System.out.println("전체 배송 건수:" + vrpResult.getTotalJobCount());
    System.out.println("전체 차량 수:" + vrpResult.getTotalVehicleCount());
    System.out.println("사용 차량 수:" + vrpResult.getVehicleCount());

    for (VrpVehicleRoute vrpVehicleRoute : vrpResult.getVrpVehicleRouteList()) {
      System.out.println(vrpVehicleRoute);
    }
  }

  /**
   * <pre>
   * 제약회사에서 1대의 차량으로 3개의 감기약을 받아서 3개의 약국에 배달하기.
   * 제약회사 <-> 약국01 : 이동시간 10분
   * 제약회사 <-> 약국02 : 이동시간 30분
   * 제약회사 <-> 약국03 : 이동시간 30분
   * 약국01 <-> 약국02 : 이동시간 20분
   * 약국01 <-> 약국03 : 이동시간 30분
   * 약국02 <-> 약국03 : 이동시간 10분
   * 
   * 기대결과 : 제약회사 -> 약국01 -> 약국02 -> 약국03 혹은 그 반대(3,2,1)
   * </pre>
   */
  @Test
  public void 예제01() {
    VrpService vrpService = new VrpService();
    vrpService.addVehicle("차량01", "제약회사");

    vrpService.addShipement("감기약01", "제약회사", "약국01");
    vrpService.addShipement("감기약02", "제약회사", "약국02");
    vrpService.addShipement("감기약03", "제약회사", "약국03");

    vrpService.addCost("제약회사", "약국01", 10L);
    vrpService.addCost("제약회사", "약국02", 30L);
    vrpService.addCost("제약회사", "약국03", 30L);
    vrpService.addCost("약국01", "약국02", 20L);
    vrpService.addCost("약국01", "약국03", 30L);
    vrpService.addCost("약국02", "약국03", 10L);

    VrpResult vrpResult = vrpService.getVrpResult();

    System.out.println("총 시간:" + vrpResult.getTotalTime());
    System.out.println("전체 배송 건수:" + vrpResult.getTotalJobCount());
    System.out.println("전체 차량 수:" + vrpResult.getTotalVehicleCount());
    System.out.println("사용 차량 수:" + vrpResult.getVehicleCount());

    for (VrpVehicleRoute vrpVehicleRoute : vrpResult.getVrpVehicleRouteList()) {
      System.out.println(vrpVehicleRoute);
    }
  }
}
