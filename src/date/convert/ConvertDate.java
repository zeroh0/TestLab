package date.convert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
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

		Map<String, Long> resultMap = new LinkedHashMap<>();

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

		HashMap<String, Long> expectedMap = new LinkedHashMap<>();
		expectedMap.put("202304", 0L);
		expectedMap.put("202305", 0L);
		expectedMap.put("202306", 3L);
		expectedMap.put("202307", 7L);
		expectedMap.put("202308", 2L);

		System.out.println("expectedMap = " + expectedMap);

		// when then
		assertThat(resultMap).isEqualTo(expectedMap);
	}

	@DisplayName("년월(yyyyMM) 형식으로 입력받았을 때 해당 년월의 전체 주(week)를 구한다.")
	@Test
	void getWeekAndMonthOfYear() {
		// given
		String inputYearMonth = "202304";
		YearMonth yearMonth = YearMonth.parse(inputYearMonth, DateTimeFormatter.ofPattern("yyyyMM"));

		TemporalField weekOfYearField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		int firstWeek = yearMonth.atDay(1).get(weekOfYearField);
		int lastWeek = yearMonth.atEndOfMonth().get(weekOfYearField);

		int totalWeekOfMonth = lastWeek - firstWeek + 1;

		// when then
		assertThat(totalWeekOfMonth).isEqualTo(6);
	}

	@DisplayName("날짜(yyyy-MM-dd) 형식으로 입력받았을 때 월 기준으로 몇 주차인지 구한다.")
	@Test
	void getWeekInMonthFromDate() throws ParseException {
		// Ref: ISO-8601
		String inputDate = "2023-04-30";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(inputDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int weekNumber = cal.get(Calendar.WEEK_OF_MONTH);

		assertThat(weekNumber).isEqualTo(6);
	}

	@DisplayName("년월(yyyyMM) 형식으로 입력받았을 때 주차(week)별로 해당하는 일자를 그룹핑")
	@Test
	void groupDatesByWeek() {
		// Ref: ISO-8601
		String inputDate = "202304";
		YearMonth yearMonth = YearMonth.parse(inputDate, DateTimeFormatter.ofPattern("yyyyMM"));

		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

		Map<Integer, List<LocalDate>> datesByWeek = new HashMap<>();
		int weekNumber = 1;
		LocalDate currentDay = firstDayOfMonth;

		while (!currentDay.isAfter(lastDayOfMonth)) {
			datesByWeek.computeIfAbsent(weekNumber, k -> new ArrayList<>()).add(currentDay);
			if (currentDay.getDayOfWeek() == DayOfWeek.SATURDAY) { // 일~토
				weekNumber++;
			}
			currentDay = currentDay.plusDays(1);
		}

		Map<Integer, List<LocalDate>> expectedMap = getGroupDatesByWeek();
		assertThat(datesByWeek).isEqualTo(expectedMap);
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

	private static Map<Integer, List<LocalDate>> getGroupDatesByWeek() {
		Map<Integer, List<LocalDate>> datesByWeek = new HashMap<>();
		datesByWeek.put(1, List.of(LocalDate.of(2023, 4, 1)));
		datesByWeek.put(2, List.of(LocalDate.of(2023, 4, 2), LocalDate.of(2023, 4, 3), LocalDate.of(2023, 4, 4), LocalDate.of(2023, 4, 5),
				LocalDate.of(2023, 4, 6), LocalDate.of(2023, 4, 7), LocalDate.of(2023, 4, 8)));
		datesByWeek.put(3, List.of(LocalDate.of(2023, 4, 9), LocalDate.of(2023, 4, 10), LocalDate.of(2023, 4, 11), LocalDate.of(2023, 4, 12),
				LocalDate.of(2023, 4, 13), LocalDate.of(2023, 4, 14), LocalDate.of(2023, 4, 15)));
		datesByWeek.put(4, List.of(LocalDate.of(2023, 4, 16), LocalDate.of(2023, 4, 17), LocalDate.of(2023, 4, 18), LocalDate.of(2023, 4, 19),
				LocalDate.of(2023, 4, 20), LocalDate.of(2023, 4, 21), LocalDate.of(2023, 4, 22)));
		datesByWeek.put(5, List.of(LocalDate.of(2023, 4, 23), LocalDate.of(2023, 4, 24), LocalDate.of(2023, 4, 25), LocalDate.of(2023, 4, 26),
				LocalDate.of(2023, 4, 27), LocalDate.of(2023, 4, 28), LocalDate.of(2023, 4, 29)));
		datesByWeek.put(6, List.of(LocalDate.of(2023, 4, 30)));

		return datesByWeek;
	}

}
