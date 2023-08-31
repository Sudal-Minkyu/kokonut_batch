package com.app.batch.utils;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;

@Slf4j
@Component
public class CommonUtil {

	// 현재 원화가치 가져오기 -> USD 기준
	public static int wonPriceGet() {
		int wonPrice = 0;

		try {
			URL url = new URL("https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD");

			// 응답 데이터 얻기
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			br.close();

			// JSON 파싱
			JSONArray jsonArray = new JSONArray(sb.toString());
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			wonPrice = jsonObject.getInt("basePrice");
		} catch (IOException e) {
			log.error("원화 환율 가져오기 실패");
		}

		return wonPrice;
	}

	// 코코넛 서비스 월결제 금액 반환
	public static int kokonutMonthPrice(Integer countMonthAverage) {
		int price = 0;

		if(countMonthAverage < 10000) {
			price = 99000;
		} else if(countMonthAverage < 100000) {
			price = 390000;
		} else if(countMonthAverage < 300000) {
			price = 790000;
		} else if(countMonthAverage < 500000) {
			price = 1490000;
		} else if(countMonthAverage < 800000) {
			price = 2290000;
		} else if(countMonthAverage < 1000000) {
			price = 2990000;
		}

		return price;
	}

	// 구독해지 대상 사용한만큼 금액결제
	public static int calculateUsedAmount(int totalAmount, LocalDate currentDate, int daysUsed) {
		// 현재 월의 총 일수를 가져옵니다.
		YearMonth yearMonth = YearMonth.from(currentDate);
		int daysInCurrentMonth = yearMonth.lengthOfMonth();

		// 일일 서비스 비용 계산
		int dailyServiceAmount = totalAmount / daysInCurrentMonth;

		// 사용한 서비스에 대한 비용 계산
		return dailyServiceAmount * daysUsed;
	}

}
