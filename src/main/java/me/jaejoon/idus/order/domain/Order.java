package me.jaejoon.idus.order.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/03
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "seq")
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDERS_ID")
    private Long seq;

    @Column(name = "ORDERS_NUMBER", nullable = false, length = 12, unique = true)
    private String orderNumber;

    @Column(name = "ITEM_NAME", nullable = false, length = 100)
    private String itemName;

    @Column(name = "MEMBER_EMAIL", length = 100, nullable = false, updatable = false)
    private String memberEmail;

    @Column(name = "PAYMENT_DATE", nullable = false)
    @CreationTimestamp
    private LocalDateTime paymentDate;

    @Builder
    private Order(String orderNumber, String itemName, String memberEmail) {
        this.orderNumber = orderNumber;
        this.itemName = itemName;
        this.memberEmail = memberEmail;
    }
}
