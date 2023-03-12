package studi.immo.service;

import org.springframework.web.multipart.MultipartFile;
import studi.immo.entity.Photo;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    Photo savePhoto (Photo photo);

    void saveImage (MultipartFile imageFile, Photo photo) throws IOException;

    Photo getImageById (Long id);

    List<Photo> getPhotosByAdvertisementId (Long advertisementId);

    Photo getMainPhotoByAdvertisementId (Long advertsiementId);

    void deletePhotosById(Long id);


}
