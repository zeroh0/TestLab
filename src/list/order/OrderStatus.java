package list.order;

import java.time.LocalDate;

public class OrderStatus {

	/** 주문매장 */
	private String storeId;

	/** 주문일 */
	private LocalDate orderDate;

	/** 납품일 */
	private LocalDate deliveryDate;

	/** 상품코드 */
	private String productId;

	/** 수량 */
	private Integer quantity;

	public OrderStatus() {
	}

	public OrderStatus(String storeId, LocalDate orderDate, LocalDate deliveryDate, String productId, Integer quantity) {
		this.storeId = storeId;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.productId = productId;
		this.quantity = quantity;
	}

	public String getStoreId() {
		return storeId;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public String getProductId() {
		return productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * 피킹이력과 비교를 위한 key
	 * @param status
	 * @return
	 */
	public static String getCompareKey(OrderStatus status) {
		return status.getStoreId() + status.getOrderDate() + status.getDeliveryDate() + status.getProductId();
	}

}
