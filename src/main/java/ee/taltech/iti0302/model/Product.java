package ee.taltech.iti0302.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    private String name;
    private String description;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "trade_id")
    private Integer tradeId;

    private Double price;

    @Column(name = "image_id")
    private Integer imageId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", insertable=false, updatable=false)
    private User user;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "category_id", insertable=false, updatable=false)
    private ProductCategory productCategory;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "trade_id", insertable=false, updatable=false)
    private Trade trade;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "image_id", insertable=false, updatable=false)
    private Image image;
}
