package date.convert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

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

}
