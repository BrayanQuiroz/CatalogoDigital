package catalogo.reports.controller;
import catalogo.reports.dto.RegistroDTO;
import catalogo.reports.util.LeerExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import catalogo.reports.services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reporte")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    private final ExcelService excelService;

    @Autowired
    public ReportController(ExcelService excelService, RestTemplate restTemplate) {
        this.excelService = excelService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/descargar")
    public ResponseEntity<InputStreamResource> descargarExcel() throws IOException {
        try {
            ByteArrayInputStream stream = excelService.reportExcel();
            InputStreamResource resource = new InputStreamResource(stream);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporteProveedores.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException  e) {
            log.error("Error al generar el archivo Excel", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private final RestTemplate restTemplate;

    @PostMapping("/upload")
    public ResponseEntity<List<RegistroDTO>> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String filename = file.getOriginalFilename();
            List<RegistroDTO> registros = LeerExcel.leerExcel(file.getInputStream());
            String url = "http://192.168.1.47:9000/save/data";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<RegistroDTO>> request = new HttpEntity<>(registros, headers);


            System.out.println("Enviando datos a " + url + ": " + request.getBody());

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Datos enviados correctamente al servicio: " + response.getBody());
            } else {
                System.out.println("Error al enviar datos al servicio: " + response.getStatusCode());
            }

            return new ResponseEntity<>(registros, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error al enviar datos al servicio: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
