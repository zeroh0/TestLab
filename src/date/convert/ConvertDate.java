package date.convert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ConvertDate {

	@DisplayName("년월(yyyyMM) 형식으로 입력받았을 때 해당 년월의 시작일자와 종료일자를 가져온다.")
	@Test
	void getStartDateAndEndDateWhenInputYearMonth() {
		// given
		String input = "202304";
		YearMonth inputYearMonth = YearMonth.parse(input, DateTimeFormatter.ofPattern("yyyyMM"));

		// 년월에서 시작일자 종료일자 구하기
		LocalDate startDate = inputYearMonth.atDay(1);
		LocalDate endDate = inputYearMonth.atEndOfMonth();

		// when then
		assertThat(startDate).isEqualTo(LocalDate.of(2023, 4, 1));
		assertThat(endDate).isEqualTo(LocalDate.of(2023, 4, 30));
	}

	@DisplayName("년월(yyyyMM) 형식으로 입력받았을 때 5개월 전 시작일자와 입력한 년월의 종료일자를 가져온다.")
	@Test
	void getStartDateAndEndDateFromPreviousFiveMonthToInputMonthWhenInputYearMonth() {
		// given
		String input = "202308";
		YearMonth inputYearMonth = YearMonth.parse(input, DateTimeFormatter.ofPattern("yyyyMM"));

		final int range = 5;
		LocalDate endDate = inputYearMonth.atEndOfMonth();
		LocalDate fiveMonthsAgoStartDate = endDate.minusMonths(range - 1).withDayOfMonth(1);

		// when then
		assertThat(fiveMonthsAgoStartDate).isEqualTo(LocalDate.of(2023, 4, 1));
		assertThat(endDate).isEqualTo(LocalDate.of(2023, 8, 31));
	}

	@DisplayName("년월(yyyyMM) 형식으로 입력받았을 때 5개월 전부터 입력한 년월의 리스트를 가져온다.")
	@Test
	void getYearMonthListFromPreviousFiveMonthToInputMonthWhenInputYearMonth() {
		// given
		String input = "202308";
		YearMonth inputYearMonth = YearMonth.parse(input, DateTimeFormatter.ofPattern("yyyyMM"));

		final int range = 5;
		LocalDate endDate = inputYearMonth.atEndOfMonth();
		LocalDate fiveMonthsAgoStartDate = endDate.minusMonths(range - 1).withDayOfMonth(1);

		List<String> yearMonthList = new ArrayList<>();

		while (!fiveMonthsAgoStartDate.isAfter(endDate)) {
			yearMonthList.add(fiveMonthsAgoStartDate.format(DateTimeFormatter.ofPattern("yyyyMM")));
			fiveMonthsAgoStartDate = fiveMonthsAgoStartDate.plusMonths(1);
		}

		// when then
		assertThat(yearMonthList).isEqualTo(List.of("202304", "202305", "202306", "202307", "202308"));
	}

	@DisplayName("날짜(yyyy-MM-dd) 형식으로 입력받았을 때 년월 리스트에 매핑되는 날짜 카운팅")
	@Test
	void countYearMonthWhenInputLocalDate() {
		// given
		// LocalDate 객체 List
		List<LocalDate> localDateList = getLocalDates();

		// yyyy-MM-dd 형식 문자열 List
		List<String> dateStrList = localDateList.stream()
				.map(localdate -> localdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
				.collect(Collectors.toList());

		// yyyyMM 형식 문자열 List (그룹핑 기준)
		List<String> yearMonthStrList = List.of("202304", "202305", "202306", "202307", "202308");

		// yyyyMM 형식 문자열 리스트를 YearMonth 리스트로 변환
//		List<YearMonth> yearMonths = yearMonthStrList.stream()
//				.map(yearMonth -> YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM")))
//				.collect(Collectors.toList());

		Map<String, Long> resultMap = new HashMap<>();

		// 그룹핑 리스트를 기준으로 yyyy-MM-dd 형식 문자열 값을 비교
		for (String yearMonthStr : yearMonthStrList) {
			long yearMonthCount = dateStrList.stream()
					.map(dateStr -> {
						LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
						return date.format(DateTimeFormatter.ofPattern("yyyyMM"));
					})
					.filter(yearMonthStr::equals)
					.count();

			resultMap.put(yearMonthStr, yearMonthCount);
		}

		HashMap<String, Long> expectedMap = new HashMap<>();
		expectedMap.put("202304", 0L);
		expectedMap.put("202305", 0L);
		expectedMap.put("202306", 3L);
		expectedMap.put("202307", 7L);
		expectedMap.put("202308", 2L);

		// when then
		assertThat(resultMap).isEqualTo(expectedMap);
	}

	private List<LocalDate> getLocalDates() {
		LocalDate localDate1 = LocalDate.of(2023, 8, 12);
		LocalDate localDate2 = LocalDate.of(2023, 8, 27);
		LocalDate localDate3 = LocalDate.of(2023, 7, 5);
		LocalDate localDate4 = LocalDate.of(2023, 7, 6);
		LocalDate localDate5 = LocalDate.of(2023, 7, 9);
		LocalDate localDate6 = LocalDate.of(2023, 7, 30);
		LocalDate localDate7 = LocalDate.of(2023, 7, 12);
		LocalDate localDate8 = LocalDate.of(2023, 7, 5);
		LocalDate localDate9 = LocalDate.of(2023, 7, 5);
		LocalDate localDate10 = LocalDate.of(2023, 6, 5);
		LocalDate localDate11 = LocalDate.of(2023, 6, 5);
		LocalDate localDate12 = LocalDate.of(2023, 6, 13);

		List<LocalDate> localDateList = List.of(localDate1, localDate2, localDate3, localDate4, localDate5, localDate6,
				localDate7, localDate8, localDate9, localDate10, localDate11, localDate12);

		return localDateList;
	}

}
