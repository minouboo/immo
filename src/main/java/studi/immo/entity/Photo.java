package studi.immo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class Photo extends GenericEntity{

    @Column
    private String path;

    @Column
    private String fileName;

    @Column (columnDefinition = "boolean default false")
    private Boolean mainPhotos = false;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn (name="advertisement_id")
    private Advertisement advertisement;


}
