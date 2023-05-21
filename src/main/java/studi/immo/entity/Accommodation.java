package studi.immo.entity;



import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
public class Accommodation extends GenericEntity {

    @Column
    private String title;

    @Column
    private Double squareMeter;

    @Column
    private Integer rooms;

    @ManyToOne
    @JoinColumn (name="user_id")
    private User user;

    @ManyToOne (cascade = CascadeType.ALL)
    private Address address;

    @OneToMany (mappedBy = "accommodation", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Advertisement> advertisements;

}
