package list;

import list.order.OrderStatus;
import list.order.Picking;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static list.order.OrderStatus.getCompareKey;

public class CompareTest {

	@DisplayName("피킹이력에 주문현황을 비교하여 새로운 주문상품이나 변경된 주문상품을 가져오기")
	@Test
	void pickingCompareWithOrderStatus() {
		// 피킹은 물류창고 내부에서 주문한 상품과 수량을 가져오기 위한 작업
		// 주문된 내역을 기준으로 피킹 작업
		// -> 피킹 작업을 한 이후에 주문을 했던 사용자가 상품을 추가하거나 수량을 변경할 수 있음. (주문 마감 전)
		// 새로운 주문상품은 상품과 수량을 새롭게 얻어오고 변경된 주문상품은 기존 주문에서 변경된 수량을 얻어온다.

		// 피킹내역
		Picking picking1 = new Picking("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p001", 5);
		Picking picking2 = new Picking("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p002", 9);
		Picking picking3 = new Picking("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p003", 8);
		List<Picking> pickingList = List.of(picking1, picking2, picking3);

		// 주문현황
		OrderStatus orderStatus1 = new OrderStatus("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p001", 10);
		OrderStatus orderStatus2 = new OrderStatus("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p002", 5);
		OrderStatus orderStatus3 = new OrderStatus("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p003", 8);
		OrderStatus orderStatus4 = new OrderStatus("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p005", 3);
		List<OrderStatus> orderStatusList = List.of(orderStatus1, orderStatus2, orderStatus3, orderStatus4);

		// 주문이력과 비교하기 위한 피킹이력의 key 값을 가진 Map 객체 생성
		Map<String, Picking> pickingMap = pickingList.stream().collect(Collectors.toMap(Picking::getCompareKey, picking -> picking));

		// 피킹목록 갱신
		List<Picking> newPickingList = orderStatusList.stream()
				.filter(status -> isAlreadyRegisteredPicking(status, pickingMap))
				.map(status -> new Picking(status.getStoreId(), status.getOrderDate(), status.getDeliveryDate(), status.getProductId(), status.getQuantity()))
				.collect(Collectors.toList());

		Assertions.assertThat(newPickingList).hasSize(3)
				.extracting("storeId", "orderDate", "deliveryDate", "productId", "quantity")
				.containsExactlyInAnyOrder(
						Tuple.tuple("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p001", 10),
						Tuple.tuple("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p002", 5),
						Tuple.tuple("c001", LocalDate.of(2023, 9, 3), LocalDate.of(2023, 9, 10), "p005", 3)
				);
	}

	/**
	 * 기존에 피킹된 내역인지 체크
	 * @param status
	 * @param pickingMap
	 * @return
	 */
	private boolean isAlreadyRegisteredPicking(OrderStatus status, Map<String, Picking> pickingMap) {
		String compareKey = getCompareKey(status);

		if (pickingMap.containsKey(compareKey)) {
			Picking picking = pickingMap.get(compareKey);
			return picking.getQuantity() != status.getQuantity();
		}

		return true;
	}

}
