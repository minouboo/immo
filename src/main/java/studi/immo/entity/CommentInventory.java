package studi.immo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CommentInventory extends GenericEntity{

    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name="apartment_inventory_id")
    private ApartmentInventory apartmentInventory;

}
