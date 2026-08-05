package mx.edu.utez.poo.cargaarchivos.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

@WebServlet("/CargarArchivosServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB (memoria RAM intermedia)
        maxFileSize = 1024 * 1024 * 2,      // 2 MB (límite por archivo)
        maxRequestSize = 1024 * 1024 * 5    // 5 MB (límite total del formulario)
)

public class CargarArchivosServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Extraemos el archivo utilizando el nombre asignado en el input del HTML
        Part filePart = req.getPart("fileDocumento");

        // Validar que se haya seleccionado un archivo válido
        if (filePart == null || filePart.getSubmittedFileName() == null || filePart.getSubmittedFileName().isEmpty()) {
            notificarError("Debes seleccionar un archivo obligatorio.", req, resp);
            return;
        }

        String nombreOriginal = filePart.getSubmittedFileName();

        // 2. Extraer la extensión del archivo (convertida a minúsculas)
        String extension = "";
        int i = nombreOriginal.lastIndexOf('.');
        if (i > 0) {
            extension = nombreOriginal.substring(i + 1).toLowerCase();
        }

        // 3. Validar lista blanca de extensiones permitidas
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            notificarError("Error: Solo se permiten archivos de imagen (.jpg, .jpeg, .png)", req, resp);
            return;
        }

        // 4. Validar que el archivo no supere los 2 MB (2,097,152 bytes)
        long maxBytes = 2 * 1024 * 1024;
        if (filePart.getSize() > maxBytes) {
            notificarError("Error: El archivo supera el tamaño máximo de 2 MB.", req, resp);
            return;
        }
        // 5. Obtener la ruta absoluta de la carpeta 'uploads' dentro del servidor
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);

        // Crear la carpeta si no existe físicamente en el servidor
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // 6. Escribir el archivo en el disco
        String filePath = uploadPath + File.separator + nombreOriginal;
        filePart.write(filePath);

        // 7. Notificar éxito a la vista
        req.setAttribute("exito", true);
        req.setAttribute("mensaje", "¡Archivo '" + nombreOriginal + "' subido exitosamente!"); // Enviamos el nombre del archivo guardado a la vista
        // Enviamos el nombre del archivo guardado a la vista
        req.setAttribute("nombreArchivo", nombreOriginal);

        req.getRequestDispatcher("subida.jsp").forward(req, resp);
    }

    private void notificarError(String mensaje, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        request.setAttribute("exito", false);
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("subida.jsp").forward(request, response);
    }

}

