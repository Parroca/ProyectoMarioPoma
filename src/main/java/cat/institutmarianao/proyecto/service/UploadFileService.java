package cat.institutmarianao.proyecto.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {
	 private final String folder = "images/";
	
	 public String saveImg(MultipartFile file) throws IOException {
	        if (!file.isEmpty()) {
	            byte[] bytes = file.getBytes();
	            Path path = Paths.get(folder + file.getOriginalFilename());
	            Files.write(path, bytes);
	            return file.getOriginalFilename();
	        }
	        return "default.jpg";
	    }
	 
	 public void deleteImg(String filename) throws IOException {
	        if (filename != null && !filename.equals("default.jpg")) {
	            Path path = Paths.get(folder + filename);
	            Files.deleteIfExists(path);
	        }
	    }
}
