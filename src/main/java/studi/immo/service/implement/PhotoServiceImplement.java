package studi.immo.service.implement;

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

@Service
public class PhotoServiceImplement implements PhotoService {

    private PhotoRepository photoRepository;

    public PhotoServiceImplement (PhotoRepository photoRepository){
        this.photoRepository=photoRepository;
    }


    @Override
    public Photo savePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    @Override
    public void saveImage(MultipartFile imageFile, Photo photo) throws IOException {
        Path currentPath = Paths.get(".");
        Path absolutePath = currentPath.toAbsolutePath();
        photo.setPath(absolutePath + "/src/main/resources/static/images/");
        byte [] bytes = imageFile.getBytes();
        Path path = Paths.get(photo.getPath() + imageFile.getOriginalFilename());
        Files.write(path, bytes);

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
