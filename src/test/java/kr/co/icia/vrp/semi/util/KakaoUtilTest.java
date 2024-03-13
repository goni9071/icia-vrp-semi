package kr.co.icia.vrp.semi.util;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import kr.co.icia.vrp.semi.util.KakaoApiUtil.Point;

public class KakaoUtilTest {

	@Test
	public void getPointByKeywordTest() throws IOException, InterruptedException {
		Point center = new Point(126.675113024566, 37.4388938204128);// 인천일보아카데미
		List<Point> pointByKeyword = KakaoApiUtil.getPointByKeyword("약국", center);
		for (Point point : pointByKeyword) {
			System.out.println(//
					"x:" + point.getX() //
							+ ",y:" + point.getY()//
							+ ",name:" + point.getName()//
							+ ",phone:" + point.getPhone()//
			);
		}

	}

	@Test
	public void getPointByAddressTest() throws IOException, InterruptedException {
		Point point = KakaoApiUtil.getPointByAddress("인천광역시 미추홀구 매소홀로488번길 6-32 태승빌딩 5층");
		System.out.println("x:" + point.getX() + ",y:" + point.getY());
	}

	@Test
	public void getVehiclePathsTest() throws IOException, InterruptedException {
		Point from = KakaoApiUtil.getPointByAddress("인천광역시 미추홀구 매소홀로488번길 6-32 태승빌딩 5층");
		System.out.println("인천일보아카데미) x:" + from.getX() + ",y:" + from.getY());
		Point to = KakaoApiUtil.getPointByAddress("와우산로 23길 20 패스트파이브 5층");
		System.out.println("브이유에스) x:" + to.getX() + ",y:" + to.getY());

		System.out.println("출발!!");
		List<Point> vehiclePaths = KakaoApiUtil.getVehiclePaths(from, to);
		for (Point point : vehiclePaths) {
			System.out.println("x:" + point.getX() + ",y:" + point.getY());
		}
	}
}
