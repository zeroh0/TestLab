package list.order;

import java.time.LocalDate;

public class Picking {

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

	public Picking(String storeId, LocalDate orderDate, LocalDate deliveryDate, String productId, Integer quantity) {
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
	 * 주문현황과 비교하기 위한 key
	 * @param picking
	 * @return
	 */
	public static String getCompareKey(Picking picking) {
		return picking.getStoreId() + picking.getOrderDate() + picking.getDeliveryDate() + picking.getProductId();
	}

}
