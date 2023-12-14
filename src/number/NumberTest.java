package number;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class NumberTest {

	@DisplayName("Double 실수(RealNumber) subtract")
	@Test
	void subtractDoubleTypeRealNumber() {
		Double number1 = 1.345;
		Double number2 = 1.34;
		System.out.println("number1 = " + number1);
		System.out.println("number2 = " + number2);

		Double result = number1 - number2;
		Double expected = 0.005;

		// *기댓값과 다름*
		assertThat(result).isNotEqualTo(expected);
	}

	@DisplayName("BigDecimal 실수(RealNumber) subtract")
	@Test
	void subtractBigDecimalRealNumber() {
		BigDecimal number1 = BigDecimal.valueOf(1.345);
		BigDecimal number2 = BigDecimal.valueOf(1.34);

		BigDecimal result = number1.subtract(number2);
		BigDecimal expected = BigDecimal.valueOf(0.005);

		assertThat(result).isEqualTo(expected);
	}

}
