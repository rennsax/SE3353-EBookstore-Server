package com.sjtu.rbj.bookstore.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Bojun Ren
 * @date 2023/04/20
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "`order`")
@Check(constraints = "`state` IN ('PENDING', 'COMPLETE', 'TRANSPORTING')")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(
        name = "`user_id`",
        nullable = false,
        referencedColumnName = "`id`",
        foreignKey = @ForeignKey(name = "order_user_fk")
    )
    private User user;

    @Generated(GenerationTime.INSERT)
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT(NOW())")
    private Timestamp time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "CHAR(20)")
    private OrderState state = OrderState.COMPLETE;

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderItem> orderItemList = new ArrayList<>();

    public Boolean addOrderItem(OrderItem orderItem) {
        Boolean isAdded = this.orderItemList.add(orderItem);
        orderItem.setOrder(this);
        return isAdded;
    }

    public Boolean removeOrderItem(OrderItem orderItem) {
        Boolean isRemoved = this.orderItemList.remove(orderItem);
        if (isRemoved) {
            orderItem.setOrder(null);
        }
        return isRemoved;
    }

    public OrderItem removeOrderItem(int index) {
        return this.orderItemList.remove(index);
    }

    public void clearOrderItem() {
        this.orderItemList.clear();
    }

    @Getter
    @Setter
    @Entity
    @NoArgsConstructor
    @Table(name = "`order_item`")
    public static class OrderItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
        @JoinColumn(
            name = "`order_id`",
            nullable = false,
            referencedColumnName = "`id`",
            foreignKey = @ForeignKey(name = "order_item_fk")
        )
        private Order order;

        /** unidirectional association */
        @OneToOne
        @JoinColumn(
            name = "`item_id`",
            referencedColumnName = "`uuid`",
            foreignKey = @ForeignKey(name = "item_uuid_fk")
        )
        private Book book;

        @Column(nullable = false)
        @ColumnDefault("1")
        private Integer quantity;

        @PrePersist
        void prePersistInitialize() {
            if (quantity == null) {
                if (quantity <= 0) {
                    throw new IllegalArgumentException("Can set quantity to " + quantity.toString());
                }
                quantity = 1;
            }
        }
    }
}
