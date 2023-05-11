package studi.immo.service.implement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import studi.immo.entity.Photo;
import studi.immo.repository.PhotoRepository;
import studi.immo.service.PhotoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class PhotoServiceImplement implements PhotoService {

    private PhotoRepository photoRepository;

    public PhotoServiceImplement (PhotoRepository photoRepository){
        this.photoRepository=photoRepository;
    }


    @Value("${picture.upload.path}")
    private String pictureUploadPath;


    @Override
    public Photo savePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    @Override
    public String saveImage(MultipartFile imageFile, Photo photo) throws IOException {
        String originalFilename = imageFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String randomFilename = imageFile.getOriginalFilename() + UUID.randomUUID().toString() + extension;
        String folder = pictureUploadPath;

        photo.setPath(folder);
        byte [] bytes = imageFile.getBytes();
        Path path = Paths.get(folder + randomFilename);
        Files.write(path, bytes);
        return randomFilename;
    }

    @Override
    public Photo getImageById(Long id) {
        return photoRepository.findById(id).get();
    }

    @Override
    public List<Photo> getPhotosByAdvertisementId(Long advertisementId) {
        return photoRepository.getPhotosByAdvertisementId(advertisementId);
    }

    @Override
    public Photo getMainPhotoByAdvertisementId(Long advertsiementId) {
        return photoRepository.getMainPhotoByAdvertisementId(advertsiementId);
    }

    @Override
    public void deletePhotosById(Long id) {
        photoRepository.deleteById(id);
    }


}
