package studi.immo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class ChatRoom extends GenericEntity{

    @ManyToOne
    private Accommodation accommodation;

    @ManyToMany
    @JoinTable(
            name = "chat_room_user",
            joinColumns = @JoinColumn (name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn (name = "user_id"))
    private Set<User> users = new HashSet<>();

    @OneToMany (mappedBy = "message")
    private Set<Message> messages;

}
