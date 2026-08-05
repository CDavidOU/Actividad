<%--
  Created by IntelliJ IDEA.
  User: CA2-MAYO
  Date: 05/08/2026
  Time: 01:28 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Bootstrap demo</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<body>
<div class="container justify-content-center mt-5">
  <c:if test="${not empty requestScope.mensaje}">
    <div class="alert alert-${requestScope.exito ? 'success' : 'danger'} mb-3">
        ${requestScope.mensaje}
    </div>
  </c:if>
  <form action="CargarArchivosServlet" method="POST" enctype="multipart/form-data" class="p-4 border">
    <div class="mb-3">
      <label for="fileDocumento" class="form-label">Selecciona una imagen (PNG o JPG, máx 2 MB):</label>
      <input type="file" name="fileDocumento" id="fileDocumento" class="form-control" required>
    </div>
    <button type="submit" class="btn btn-success">Cargar Archivo</button>
    <a href="index.jsp" class="btn btn-primary">Regresar</a>
  </form>
  <%-- Mostrar la imagen subida si la variable 'nombreArchivo' está presente --%>
  <c:if test="${not empty requestScope.nombreArchivo}">
    <div class="container alignt-item-center justify-content-center mt-4 text-center card p-3" style=" max-width: 400px;" >
      <h5>Vista previa de la imagen guardada:</h5>

      <!-- Apuntamos la fuente de la imagen (src) hacia la URL de nuestro Servlet descargador -->
      <img src="DescargarArchivosServlet?nombre=${requestScope.nombreArchivo}"
           alt="Imagen cargada"
           class="img-fluid rounded border my-5 mx-3 mx-auto">

      <p class="text-muted small">
        URL servida: <code>DescargarArchivosServlet?nombre=${requestScope.nombreArchivo}</code>
      </p>
    </div>
  </c:if>
</div>




<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
</body>
</html>