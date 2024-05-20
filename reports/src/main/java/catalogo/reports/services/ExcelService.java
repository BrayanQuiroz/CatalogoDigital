package catalogo.reports.services;

import catalogo.reports.client.UsersClient;
import catalogo.reports.dto.UsuariosDTO;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {


    private final UsersClient usersClient;


    @Autowired
    public ExcelService(UsersClient usersClient) {
        this.usersClient = usersClient;
    }

    public ByteArrayInputStream reportExcel() throws IOException{

        List<UsuariosDTO> usuarios = usersClient.getUsuarios();

        try(Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();){
            Sheet sheet = workbook.createSheet("Datos");

            String[] columnas = {
                                "RUC","Correo","Razón social","DNI","Nombre", "Apellido Paterno",  "Apellido Materno", "Genero",
                                "Departamento","Provincia", "Distrito", "Dirección","Experiencia laboral", "Carrera profesional",
                                "Web","Nivel academico", "Cadena productiva", "Sector", "Especialidad","TipoProveedor","Región"
                                 };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                headerRow.createCell(i).setCellValue(columnas[i]);
            }

            int rowNum = 1;
            for (UsuariosDTO dato : usuarios) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dato.getRuc());
                row.createCell(1).setCellValue(dato.getCorreo());
                row.createCell(2).setCellValue(dato.getDni());
                row.createCell(3).setCellValue(dato.getNombres());
                row.createCell(4).setCellValue(dato.getApellidoPaterno());
                row.createCell(5).setCellValue(dato.getApellidoMaterno());
                row.createCell(6).setCellValue(dato.getExperienciaLaboral());
                row.createCell(7).setCellValue(dato.getDepartamentos());
                row.createCell(8).setCellValue(dato.getProvincia());
                row.createCell(9).setCellValue(dato.getDistrito());
                row.createCell(10).setCellValue(dato.getCarreraProfesional());
                row.createCell(11).setCellValue(dato.getRazonSocial());
                row.createCell(12).setCellValue(dato.getWeb());
                row.createCell(13).setCellValue(dato.getGenero());
                row.createCell(14).setCellValue(dato.getNivelAcademico());
                row.createCell(15).setCellValue(dato.getCadenaProductiva());
                row.createCell(16).setCellValue(dato.getSector());
                row.createCell(17).setCellValue(dato.getEspecialidad());
                row.createCell(18).setCellValue(dato.getDireccion());
                row.createCell(19).setCellValue(dato.getTipoProveedor());
                row.createCell(20).setCellValue(dato.getDepartamento());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }catch (IOException e) {

            System.err.println("Error al crear el documento Excel: " + e.getMessage());
            throw e;
        }
    }
}
