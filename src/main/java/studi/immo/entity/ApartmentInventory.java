package studi.immo.entity;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class ApartmentInventory extends GenericEntity{

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date dateInventory;

    @Column
    private String comment;

    @ManyToOne
    private Agreement agreement;

    @ElementCollection (targetClass = InventoryType.class, fetch = FetchType.EAGER)
    @CollectionTable (name="inventory_type")
    @Column (name="type")
    @Enumerated (value = EnumType.STRING)
    private Set<InventoryType> type = new HashSet<>();

    @Column (columnDefinition = "boolean default false")
    private Boolean tenantValidate = false;

    @Column (columnDefinition = "boolean default false")
    private Boolean LandlordValidate = false;

}
