package studi.immo.service;

import studi.immo.entity.CommentInventory;

import java.util.List;

public interface CommentInventoryService {

    CommentInventory saveCommentInventory (CommentInventory commentInventory);

    void deleteCommentInventoryById (Long id);
    List<CommentInventory> getCommentInventoryByApartmentId (Long ApartmentInventoryId);
    CommentInventory getCommentInventoryById (Long id);
}
