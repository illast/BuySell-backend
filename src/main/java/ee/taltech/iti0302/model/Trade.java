package ee.taltech.iti0302.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@ToString
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="trade")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "buyer_id")
    private Integer buyerId;

    @Column(name = "seller_id")
    private Integer sellerId;

    private LocalDate date;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "buyer_id", insertable=false, updatable=false)
    private User buyer;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "seller_id", insertable=false, updatable=false)
    private User seller;

    @ToString.Exclude
    @OneToOne(mappedBy = "trade")
    private Product product;
}
