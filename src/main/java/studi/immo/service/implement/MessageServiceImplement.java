package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.Message;
import studi.immo.repository.MessageRepository;
import studi.immo.service.MessageService;

import java.util.List;

@Service
public class MessageServiceImplement implements MessageService {

    private MessageRepository messageRepository;

    public MessageServiceImplement (MessageRepository messageRepository){
        super();
        this.messageRepository = messageRepository;
    }

    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessageByAccommodationAndUser(Long userId, Long accommodationId) {
        return messageRepository.getMessageByAccommodationAndUser(userId,accommodationId);
    }

    @Override
    public List<Message> getMessageByChatRoomId(Long chatRoomId) {
        return messageRepository.getMessageByChatRoomId(chatRoomId);
    }


}

