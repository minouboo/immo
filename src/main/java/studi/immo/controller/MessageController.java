package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studi.immo.entity.*;
import studi.immo.form.ChatForm;
import studi.immo.service.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "chat")
public class MessageController {

    private AccommodationService accommodationService;
    private MessageService messageService;
    private UserService userService;
    private ChatRoomService chatRoomService;
    private AdvertisementService advertisementService;
    private TenantService tenantService;

    public MessageController(AccommodationService accommodationService, MessageService messageService, UserService userService, ChatRoomService chatRoomService, AdvertisementService advertisementService, TenantService tenantService) {
        this.accommodationService = accommodationService;
        this.messageService = messageService;
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.advertisementService = advertisementService;
        this.tenantService = tenantService;
    }


    @GetMapping(value = "/nouveau-message/{id}")
    public String myNewMessage (@PathVariable Long id, Model model){
        Accommodation accommodation = accommodationService.getAccommodationAndUserById(id);
        User currentUser = userService.getCurrentUser();
        Tenant currentTenant = tenantService.getTenantByUserId(currentUser.getId());
        if (currentUser == null){
            return "redirect:/login";
        }
        if (currentUser.getId().equals(accommodation.getUser().getId())){
            ChatForm chatForm = new ChatForm();
            model.addAttribute("SendMessage", chatForm);
            model.addAttribute("Accommodation", accommodation);
            return "NewMessage";
        }
        if (currentTenant == null){
            return "redirect:/user/creation-locataire";
        }
        ChatForm chatForm = new ChatForm();
        model.addAttribute("SendMessage", chatForm);
        model.addAttribute("Accommodation", accommodation);
        return "NewMessage";

    }

    @PostMapping(value = "/envoyer-nouveau-message/{id}")
    public String sendNewMessage (@PathVariable Long id,
                               @ModelAttribute("SendMessage") ChatForm chatForm){
        User userTenant = userService.getCurrentUser();
        if (userTenant == null){
            return "redirect:/login";
        }
        Accommodation a = accommodationService.getAccommodationById(id);
        User userLandlord = a.getUser();
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime currentDate = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
        Set groupUser = new HashSet<>();
        groupUser.add(userTenant);
        groupUser.add(userLandlord);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setAccommodation(a);
        chatRoom.getUsers().addAll(groupUser);
        chatRoomService.saveChatRoom(chatRoom);
        Message message = new Message();
        message.setMessage(chatForm.getMessage());
        message.setChatRoom(chatRoom);
        message.setUserFrom(userTenant);
        /*message.setUserTo(userLandlord);*/
        message.setSendingDate(currentDate);
        messageService.saveMessage(message);
        return "redirect:/chat/mes-conversations";
    }

    @GetMapping (value = "/mes-conversations")
    public String myChatRooms (Model model){
        User user = userService.getCurrentUser();
        if (user == null){
            return "redirect:/login";
        }
        model.addAttribute("Title","Mes messages");
        boolean isActive = true;
        model.addAttribute("ButtonMessage", isActive);
        model.addAttribute("ButtonMessageArchived", !isActive);
        List<ChatRoom> myChatRooms = chatRoomService.getAllChatRoomByUserTenantId(user.getId());
        model.addAttribute("MyChatRoom", myChatRooms);
        return "MyChatRooms";
    }

    @GetMapping (value = "/mes-conversations-archivees")
    public String myChatRoomsArchived (Model model){
        User user = userService.getCurrentUser();
        if (user == null){
            return "redirect:/login";
        }
        model.addAttribute("Title","Mes messages archivés");
        boolean isActive = true;
        model.addAttribute("ButtonMessage", !isActive);
        model.addAttribute("ButtonMessageArchived", isActive);
        List<ChatRoom> myChatRooms = chatRoomService.getAllChatRoomArchivedByUserTenantId(user.getId());
        model.addAttribute("MyChatRoom", myChatRooms);
        return "MyChatRooms";
    }

    @GetMapping (value = "/conversation-message/{id}")
    public String messageChatRoom (@PathVariable Long id, Model model){
        List<Message> messagesChatRoom = messageService.getMessageByChatRoomId(id);
        model.addAttribute("MessagesChatRoom", messagesChatRoom);
        Message message = new Message();
        model.addAttribute("SendMessage", message);
        ChatRoom chatRoom = chatRoomService.getChatRoomById(id);
        model.addAttribute("ChatRoom", chatRoom);
        User landLordUser = userService.getCurrentUser();
        if (landLordUser == null){
            return "redirect:/login";
        }
        boolean IsLandLord=false;
        if (landLordUser != null){
            IsLandLord =  landLordUser.getId().equals(chatRoom.getAccommodation().getUser().getId());
        }
        model.addAttribute("IsLandLord",IsLandLord);
        return "MessageChatRoom";
    }

    @PostMapping (value = "/envoyer-message-conversation/{id}")
    public String sendMessageChatRoom (@PathVariable Long id, @ModelAttribute("SendMessage")ChatForm chatForm){
        User userFrom = userService.getCurrentUser();
        if (userFrom == null){
            return "redirect:/login";
        }
        ChatRoom chatRoom = chatRoomService.getChatRoomById(id);
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime currentDate = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
        Message message = new Message();
        message.setUserFrom(userFrom);
        message.setMessage(chatForm.getMessage());
        message.setChatRoom(chatRoom);
        message.setSendingDate(currentDate);
        messageService.saveMessage(message);
        return "redirect:/chat/conversation-message/"+chatRoom.getId();
    }

    @GetMapping(value="/conversation/suppression/{id}")
    public String archiveChatRoom (@PathVariable Long id){
        ChatRoom currentChatRoom = chatRoomService.getChatRoomById(id);
        currentChatRoom.setArchived(Boolean.TRUE);
        chatRoomService.saveChatRoom(currentChatRoom);
        return "redirect:/chat/mes-conversations";
    }

    @GetMapping(value="/conversation/remettre/{id}")
    public String unarchiveChatRoom (@PathVariable Long id){
        ChatRoom currentChatRoom = chatRoomService.getChatRoomById(id);
        currentChatRoom.setArchived(Boolean.FALSE);
        chatRoomService.saveChatRoom(currentChatRoom);
        return "redirect:/chat/mes-conversations";
    }
}
