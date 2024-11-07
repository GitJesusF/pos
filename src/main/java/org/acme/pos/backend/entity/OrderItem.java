package org.acme.pos.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request_order_item")
public class OrderItem {

  @Id
  @Column(updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  // Use ManyToOne relationship for proper foreign key mapping
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "request_order_id", nullable = false)
  private Order order;  // Represents the Order associated with this item

  // Use ManyToOne relationship for proper foreign key mapping
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;  // Represents the Item associated with this order

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private BigDecimal subtotal;
}
