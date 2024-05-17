package catalogo.persistence.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class CurriculumService {

    private final String directoryPath = "curriculums/";
    private final String directoryPathTwo = "certificado/";

    public void saveDocument(MultipartFile file, String ruc, Integer tipo) throws IOException {
        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            if(tipo == 1){
                Path path = Paths.get(directoryPath + ruc + "-cv.pdf");
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } else if (tipo == 2) {
                Path path = Paths.get(directoryPathTwo + ruc + "-certificado.pdf");
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new IOException("No se pudo guardar el archivo: " + e.getMessage(), e);
        }
    }
}
