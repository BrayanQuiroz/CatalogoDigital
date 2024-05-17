package catalogo.persistence.controller;

import catalogo.persistence.services.CurriculumService;
import catalogo.persistence.services.VerDocumentoService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/documents")
public class DocumentoController {

    private final CurriculumService curriculumService;

    private final VerDocumentoService verDocumentoService;

    @Autowired
    public DocumentoController(CurriculumService curriculumService, VerDocumentoService verDocumentoService) {
        this.curriculumService = curriculumService;
        this.verDocumentoService = verDocumentoService;
    }

    @PostMapping("/uploadcv")
    public ResponseEntity<String> uploadDocumento(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("ruc") String ruc,
                                                  @RequestParam("tipo") Integer tipo) {
        try {
            curriculumService.saveDocument(file, ruc, tipo);
            return ResponseEntity.ok("Documento guardado correctamente");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al guardar el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/ver/{ruc}/{tipo}")
    public ResponseEntity<?> getDocumentByRucAndType(@PathVariable String ruc, @PathVariable int tipo) {
        try {
            Path file = verDocumentoService.findDocument(ruc, tipo);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                byte[] fileBytes = Files.readAllBytes(file);
                String encodedString = Base64.encodeBase64String(fileBytes);

                String contentType = tipo == 4 ? "image/jpeg" : "application/pdf";
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(encodedString);
            } else {
                return ResponseEntity.status(404).body("Document not readable or does not exist.");
            }
        } catch (Exception e) {
            if (e instanceof VerDocumentoService.DocumentNotFoundException) {
                return ResponseEntity.status(404).body("Document not found: " + e.getMessage());
            } else {
                return ResponseEntity.status(500).body("Error accessing file system: " + e.getMessage());
            }
        }
    }

}
