package studi.immo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class ChatRoom extends GenericEntity{

    @ManyToOne
    private User userTenant;

    @ManyToOne
    private User userLandlord;

    @ManyToOne
    private Accommodation accommodation;

    @OneToMany (mappedBy = "message", cascade = CascadeType.ALL)
    private Set<Message> messages;

}
