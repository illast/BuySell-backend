package ee.taltech.iti0302.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@ToString
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    private String name;

    @Column(name = "original_file_name")
    private String originalFileName;
    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @ToString.Exclude
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] bytes;

    @ToString.Exclude
    @OneToOne(mappedBy = "image")
    private Product product;
}
