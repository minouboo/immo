package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.CommentInventory;
import studi.immo.repository.CommentInventoryRepository;
import studi.immo.service.CommentInventoryService;

import java.util.List;

@Service
public class CommentInventoryServiceImplement implements CommentInventoryService {

    private CommentInventoryRepository commentInventoryRepository;

    public CommentInventoryServiceImplement (CommentInventoryRepository commentInventoryRepository){
        super ();
        this.commentInventoryRepository = commentInventoryRepository;
    }

    @Override
    public CommentInventory saveCommentInventory(CommentInventory commentInventory) {
        return commentInventoryRepository.save(commentInventory);
    }

    @Override
    public void deleteCommentInventoryById(Long id) {
        commentInventoryRepository.deleteById(id);
    }

    @Override
    public List<CommentInventory> getCommentInventoryByApartmentId(Long ApartmentInventoryId) {
        return commentInventoryRepository.getCommentInventoryByApartmentId(ApartmentInventoryId);
    }

    @Override
    public CommentInventory getCommentInventoryById(Long id) {
        return commentInventoryRepository.findById(id).get();
    }


}
