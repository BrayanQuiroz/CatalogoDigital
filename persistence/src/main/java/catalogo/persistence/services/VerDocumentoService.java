package catalogo.persistence.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class VerDocumentoService {

    public Path findDocument(String ruc, int type) throws DocumentNotFoundException, IOException {
        String directoryPath = switch (type) {
            case 1 -> "/opt/data/autorizacion/";
            case 2 -> "/opt/data/cul/";
            case 3 -> "/opt/data/cv/";
            case 4 -> "/opt/data/imagenes/";
            case 5 -> "/opt/data/fichaRuc/";
            default -> throw new IllegalArgumentException("Invalid document type");
        };

        Path directory = Paths.get(directoryPath);

        System.out.println("Checking directory: " + directory);

        if (!Files.exists(directory)) {
            throw new IOException("Directory does not exist: " + directory);
        }

        if (!Files.isDirectory(directory)) {
            throw new IOException("Path is not a directory: " + directory);
        }

        try (var paths = Files.walk(directory)) {
            Optional<Path> documentPath = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith(ruc))
                    .findFirst();

            if (documentPath.isPresent()) {
                System.out.println("Document found: " + documentPath.get());
                return documentPath.get();
            } else {
                throw new DocumentNotFoundException("Document not found");
            }
        } catch (IOException e) {
            throw new IOException("Error while accessing the file system", e);
        }
    }


    public static class DocumentNotFoundException extends Exception {
        public DocumentNotFoundException(String message) {
            super(message);
        }
    }
}
