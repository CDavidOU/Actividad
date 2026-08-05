package mx.edu.utez.poo.cargaarchivos.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet("/DescargarArchivosServlet")
public class DescargarArchivosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Obtener el nombre del archivo enviado por la URL (ej. ?nombre=foto.png)
        String nombreArchivo = request.getParameter("nombre");
        // 2. Validar que el parámetro no venga vacío
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error: Nombre de archivo requerido.");
            return;
        }

        // 3. Ubicar la carpeta 'uploads' y construir la ruta del archivo
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File archivo = new File(uploadPath, nombreArchivo);

        // 4. Validar si el archivo existe físicamente en el disco
        if (!archivo.exists() || archivo.isDirectory()) {
            // Responder con un estado HTTP 404 Not Found
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error 404: El archivo solicitado no existe.");
            return;
        }

        // 5. Detectar automáticamente el tipo de contenido (MIME Type)
        String mimeType = getServletContext().getMimeType(archivo.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Tipo por defecto para datos binarios
        }

        // 6. Configurar las cabeceras HTTP de respuesta
        response.setContentType(mimeType);
        response.setContentLength((int) archivo.length());
        response.setHeader("Content-Disposition", "inline; filename=\"" + archivo.getName() + "\"");
        // 7. Transferir los bytes del archivo desde el disco hacia la respuesta HTTP
        try (InputStream in = new FileInputStream(archivo);
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[4096]; // Bloque de lectura de 4 KB
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}