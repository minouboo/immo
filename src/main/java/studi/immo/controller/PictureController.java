package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studi.immo.entity.Advertisement;
import studi.immo.entity.Photo;
import studi.immo.entity.Role;
import studi.immo.entity.User;
import studi.immo.service.AdvertisementService;
import studi.immo.service.PhotoService;
import studi.immo.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping (value = "/photo")
public class PictureController {

        private AdvertisementService advertisementService;
        private PhotoService photoService;
        private UserService userService;

        public PictureController (AdvertisementService advertisementService, PhotoService photoService, UserService userService){
            this.advertisementService =advertisementService;
            this.photoService = photoService;
            this.userService = userService;
        }


        @GetMapping(value = "/selection-images/{id}")
        public String displayUploadForm(@PathVariable Long id, Model model) {
            User currentUser = userService.getCurrentUser();
            Advertisement currentAdvertisement = advertisementService.getAdvertisementById(id);
            model.addAttribute("MyAdvertisement", currentAdvertisement);
            Photo mainPhoto = photoService.getMainPhotoByAdvertisementId(id);
            model.addAttribute("MainPhoto", mainPhoto);
            List<Photo> photosAdvertisement = photoService.getPhotosByAdvertisementId(id);
            model.addAttribute("Photo",photosAdvertisement);
            String folder = "/images/";
            model.addAttribute("Path",folder);
            if(currentUser.getRoles().contains(Role.ADMIN) || currentAdvertisement.getAccommodation().getUser().equals(currentUser)){
                return "AddPhotos";
            }


            return "redirect:/login";
        }



        @PostMapping (value = "/telecharger-image-principal/{id}")
        public String uploadMainPic(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) {
            Advertisement currentAdvertisement = advertisementService.getAdvertisementById(id);
            Photo photo = new Photo();
            try {
                String fileName = photoService.saveImage(imageFile, photo);
                photo.setFileName(fileName);
                photo.setAdvertisement(currentAdvertisement);
                photo.setMainPhotos(Boolean.TRUE);
                photoService.savePhoto(photo);
                return "redirect:/photo/selection-images/"+currentAdvertisement.getId();
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/photo/selection-images/"+currentAdvertisement.getId();
            }
        }

        @PostMapping (value = "/telecharger-images/{id}")
        public String uploadPics(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) {
            Advertisement currentAdvertisement = advertisementService.getAdvertisementById(id);
            Photo photo = new Photo();
            try {
                String fileName = photoService.saveImage(imageFile, photo);
                photo.setFileName(fileName);
                photo.setFileName(imageFile.getOriginalFilename());
                photo.setAdvertisement(currentAdvertisement);
                photoService.savePhoto(photo);
                return "redirect:/photo/selection-images/"+currentAdvertisement.getId();
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/photo/selection-images/"+currentAdvertisement.getId();
            }
        }

        @GetMapping (value = "/supprimer-image/{id}")
        public String deletePic (@PathVariable Long id) throws IOException {
            Photo currentPhoto = photoService.getImageById(id);
            Advertisement currentAdvertisement = photoService.getImageById(id).getAdvertisement();
            Path pathPhoto = Paths.get(currentPhoto.getPath() + currentPhoto.getFileName());
            Files.delete(pathPhoto);
            photoService.deletePhotosById(id);
            return "redirect:/photo/selection-images/"+currentAdvertisement.getId();
        }

}
