package studi.immo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Getter
public class ApartmentInventory extends GenericEntity{

    @Column
    private Date entryDate;

    @Column
    private Date exitDate;

    @Column
    private String commentEntry;

    @Column
    private String commentExit;

    @ManyToOne
    private Agreement agreement;

}
