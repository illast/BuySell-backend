package ee.taltech.iti0302.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@ToString
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate birthdate;
    private String gender;
    private Double balance;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Product> products;

    @ToString.Exclude
    @OneToMany(mappedBy = "buyer")
    private List<Trade> buyerTrades;

    @ToString.Exclude
    @OneToMany(mappedBy = "seller")
    private List<Trade> sellerTrades;
}
