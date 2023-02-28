package studi.immo.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
public class Message extends GenericEntity {

    @ManyToOne
    private User userFrom;

    @ManyToOne
    private User userTo;

    @Column
    private String message;

    @ManyToOne
    @JoinColumn (name = "chat_room_id")
    private ChatRoom chatRoom;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime sendingDate;


}
