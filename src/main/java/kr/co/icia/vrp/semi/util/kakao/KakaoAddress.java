package kr.co.icia.vrp.semi.util.kakao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAddress {
	private List<Document> documents;

	public List<Document> getDocuments() {
		return documents;
	}


}