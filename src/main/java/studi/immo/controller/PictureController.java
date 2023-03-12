package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studi.immo.entity.Advertisement;
import studi.immo.entity.Photo;
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
/*@PreAuthorize("isAuthenticated()")*/
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

            Advertisement currentAdvertisement = advertisementService.getAdvertisementById(id);
            model.addAttribute("MyAdvertisement", currentAdvertisement);
            Photo mainPhoto = photoService.getMainPhotoByAdvertisementId(id);
            model.addAttribute("MainPhoto", mainPhoto);
            List<Photo> photosAdvertisement = photoService.getPhotosByAdvertisementId(id);
            model.addAttribute("Photo",photosAdvertisement);


            return "AddPhotos";
        }



        @PostMapping (value = "/telecharger-image-principal/{id}")
        public String uploadMainPic(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) {
            Advertisement currentAdvertisement = advertisementService.getAdvertisementById(id);
            Photo photo = new Photo();
            try {
                photo.setFileName(imageFile.getOriginalFilename());
                photo.setAdvertisement(currentAdvertisement);
                photo.setMainPhotos(Boolean.TRUE);
                photoService.saveImage(imageFile, photo);
                photoService.savePhoto(photo);
                return "redirect:/photo/selection-images/"+currentAdvertisement.getId();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @PostMapping (value = "/telecharger-images/{id}")
        public String uploadPics(@PathVariable Long id, @RequestParam("imageFile") MultipartFile imageFile) {
            Advertisement currentAdvertisement = advertisementService.getAdvertisementById(id);
            Photo photo = new Photo();
            try {
                photo.setFileName(imageFile.getOriginalFilename());
                photo.setAdvertisement(currentAdvertisement);
                photoService.saveImage(imageFile, photo);
                photoService.savePhoto(photo);
                return "redirect:/photo/selection-images/"+currentAdvertisement.getId();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @GetMapping (value = "/supprimer-image/{id}")
        public String deletePic (@PathVariable Long id) throws IOException {
            Photo currentPhoto = photoService.getImageById(id);
            Path pathPhoto = Paths.get(currentPhoto.getPath() + currentPhoto.getFileName());
            Files.delete(pathPhoto);
            photoService.deletePhotosById(id);
            return "redirect:/user/mes-annonces/";
        }


        /*@PostMapping ("upload-image")
        public ModelAndView uploadPic(Photo photo, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
            String returnValue = "Index";
            ModelAndView modelAndView= new ModelAndView();
            try {
                photoService.saveImage(imageFile,photo);

            } catch (IOException e) {
                e.printStackTrace();
                modelAndView.setViewName("error");
                return modelAndView;
            }

            try {
                Photo photo = new Photo();
                photo.setFileName(imageFile.getOriginalFilename());
                photo.setPath("/photo");
                photoService.saveImage(imageFile,);
            }

        }*/

        /*

        @PostMapping("/pic/upload")
        public RedirectView savePhoto (Advertisement advertisement,@RequestParam ("image")MultipartFile multipartFile) throws IOException{
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            advertisement.setPhotos(fileName);
            Advertisement saveAdvertisement = advertisementRepository.save(advertisement);
            String uploadDir = "advertisement-photos/"+saveAdvertisement.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            return new RedirectView("/index", true);
        }*/

        /*public static String UPLOAD_DIRECTORY = System.getProperty("/Users/minhbuu/") + "/uploads";



        @PostMapping("/upload")
        public String uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException {
            StringBuilder fileNames = new StringBuilder();
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            Files.write(fileNameAndPath, file.getBytes());
            model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
            return "Upload-pic";
        }*/



    /*public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    @GetMapping("/uploadimage") public String displayUploadForm() {
        return "Upload-pic";
    }

    @PostMapping("/upload") public String uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        *//*Files.write(fileNameAndPath, file.getBytes());*//*
        String test = encodeFileToBase64Binary(fileNameAndPath.toFile());

        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
        return "Upload-pic";
    }

    private static String encodeFileToBase64Binary(File file) throws IOException {
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }*/
}
