package catalogo.reports.util;

import catalogo.reports.dto.PersonaJuridica;
import catalogo.reports.dto.RegistroDTO;
import catalogo.reports.dto.UsuarioDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LeerExcel {

    public static List<RegistroDTO> leerExcel(InputStream excelInput) {
        List<RegistroDTO> registros = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        try (Workbook workbook = new XSSFWorkbook(excelInput)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                UsuarioDTO usuario = new UsuarioDTO();
                usuario.setRuc(getLongValueFromCell(row.getCell(0)));
                usuario.setCorreo(formatter.formatCellValue(row.getCell(1)));

                PersonaJuridica pj = new PersonaJuridica();
                pj.setRuc(getLongValueFromCell(row.getCell(2)));
                pj.setDni(getLongValueFromCell(row.getCell(3)));
                pj.setNombres(formatter.formatCellValue(row.getCell(4)));
                pj.setApellidoPaterno(formatter.formatCellValue(row.getCell(5)));
                pj.setApellidoMaterno(formatter.formatCellValue(row.getCell(6)));
                pj.setExperienciaLaboral(getNumberValue(row.getCell(7), Integer.class, 0));
                pj.setDepartamentos(formatter.formatCellValue(row.getCell(8)));
                pj.setProvincia(formatter.formatCellValue(row.getCell(9)));
                pj.setDistrito(formatter.formatCellValue(row.getCell(10)));
                pj.setCarreraProfesional(formatter.formatCellValue(row.getCell(11)));
                pj.setRazonSocial(formatter.formatCellValue(row.getCell(12)));
                pj.setWeb(formatter.formatCellValue(row.getCell(13)));
                pj.setGenero(Integer.valueOf(formatter.formatCellValue(row.getCell(14))));
                pj.setNivelAcademico(Integer.valueOf(formatter.formatCellValue(row.getCell(15))));
                pj.setCadenaProductiva(getNumberValue(row.getCell(16), Integer.class, 1));
                pj.setSector(getNumberValue(row.getCell(17), Integer.class,1));
                pj.setEspecialidad(getNumberValue(row.getCell(18), Integer.class,1));
                pj.setDireccion(formatter.formatCellValue(row.getCell(19)));
                pj.setTipoProveedor(Integer.valueOf(formatter.formatCellValue(row.getCell(20))));
                pj.setDepartamento(getNumberValue(row.getCell(21), Integer.class,1 ));

                RegistroDTO registro = new RegistroDTO();
                registro.setUsuario(usuario);
                registro.setPersonaJuridica(pj);
                registros.add(registro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registros;
    }

    private static long getLongValueFromCell(Cell cell) {
        if (cell == null) {
            System.err.println("Celda no encontrada o es nula.");
            return 0L; // Retorna long por defecto
        }

        // Agregando la impresión del tipo de celda para diagnosticar el problema
        System.out.println("Tipo de celda: " + cell.getCellType());

        DataFormatter formatter = new DataFormatter();
        String cellValue = formatter.formatCellValue(cell);

        // Verifica si la celda está vacía después de formatear
        if (cellValue == null || cellValue.isEmpty()) {
            System.err.println("Celda vacía o formateada a cadena vacía.");
            return 0L; // Retorna long por defecto
        }

        try {
            return Long.parseLong(cellValue);
        } catch (NumberFormatException e) {
            System.err.println("Número no válido en la celda: " + cell.getAddress() + " Valor leído: '" + cellValue + "'");
            return 0L; // Retorna long por defecto
        }
    }


    private static <T extends Number> T getNumberValue(Cell cell, Class<T> type, T defaultValue) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return defaultValue; // Retorna un valor por defecto si la celda es nula o vacía
        }
        try {
            DataFormatter formatter = new DataFormatter();
            String cellValue = formatter.formatCellValue(cell);
            if (cellValue == null || cellValue.isEmpty()) {
                return defaultValue; // Retorna un valor por defecto si el valor de la celda está vacío
            }

            if (type == Integer.class) {
                return type.cast(Integer.parseInt(cellValue));
            } else if (type == Long.class) {
                return type.cast(Long.parseLong(cellValue));
            }
        } catch (NumberFormatException e) {
            System.err.println("Número no válido en la celda: " + cell.getAddress());
            return defaultValue; // Retorna un valor por defecto si ocurre un error de formato
        }
        return defaultValue; // En caso de cualquier otra eventualidad, retorna el valor por defecto
    }


}