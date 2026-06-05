package dam.primero.servlet.crm;

import dam.primero.modelos.crm.*;
import dam.primero.repositorio.crm.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CRMServlet extends HttpServlet {
    private static final long serialVersionUID = 2051990309999713971L;
    public static final String TEXT_HTML_CHARSET_UTF_8 = "text/html;charset=UTF-8";
    public static final String TEMPLATES = "/WEB-INF/templates/crm/";
    public static final String SUFFIX = ".html";

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;
    private RepoCRM repoCRM;
    private LectorXPathCRM lectorXPathCRM;
    private GeneradorJsonCRM generadorJsonCRM;

    @Override
    public void init() throws ServletException {
        System.out.println("En el init CRMServlet");
        ServletContext servletContext = getServletContext();
        application = JavaxServletWebApplication.buildApplication(servletContext);

        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
        templateResolver.setPrefix(TEMPLATES);
        templateResolver.setSuffix(SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        repoCRM = new RepoCRM();
        lectorXPathCRM = new LectorXPathCRM();
        generadorJsonCRM = new GeneradorJsonCRM();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("En el doGet CRMServlet");
        response.setContentType(TEXT_HTML_CHARSET_UTF_8);

        WebContext context = crearContexto(request, response);
        String servletPath = (request.getServletPath() != null) ? request.getServletPath().trim() : "";
        String pathInfo = request.getPathInfo();
        String path = (pathInfo != null) ? pathInfo.trim() : "";

        System.out.println("doGet servletPath: " + servletPath);
        System.out.println("doGet pathInfo:    " + pathInfo);

        if ("/".equals(servletPath) && path.isEmpty()) {
            templateEngine.process("index", context, response.getWriter());
        } else if ("/crm".equals(servletPath)) {
            procesarGetCRM(path, context, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType(TEXT_HTML_CHARSET_UTF_8);

        WebContext context = crearContexto(request, response);
        String servletPath = (request.getServletPath() != null) ? request.getServletPath().trim() : "";
        String pathInfo = request.getPathInfo();
        String path = (pathInfo != null) ? pathInfo.trim() : "";

        System.out.println("doPost servletPath: " + servletPath);
        System.out.println("doPost pathInfo:    " + pathInfo);

        if ("/crm".equals(servletPath)) {
            procesarPostCRM(path, request, context, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private WebContext crearContexto(HttpServletRequest request, HttpServletResponse response) {
        IServletWebExchange webExchange = application.buildExchange(request, response);
        return new WebContext(webExchange, request.getLocale());
    }

    private void procesarGetCRM(String path, WebContext context, HttpServletResponse response) throws IOException {
        if (path.isEmpty() || path.equals("/")) {
            templateEngine.process("indexCRM", context, response.getWriter());
            return;
        }

        String accion = path.substring(1).split("/")[0].toLowerCase();
        System.out.println("doGet accion CRM: " + accion);

        switch (accion) {
            case "organizacion":
                templateEngine.process("html/formularioOrganizacion", context, response.getWriter());
                break;
            case "oportunidad":
                templateEngine.process("html/formularioOportunidad", context, response.getWriter());
                break;
            case "producto":
                templateEngine.process("html/formularioProducto", context, response.getWriter());
                break;
            case "xpath":
                mostrarConsultasXPath(context, response);
                break;
            case "generar-json":
                generarJsonProductos(context, response);
                break;
            case "crearpagina":
                templateEngine.process("html/Crear_Pagina", context, response.getWriter());
                break;
            case "listadopaginas":
                RepoPaginaWeb repo = new RepoPaginaWeb();
                List<PaginaWeb> paginas = repo.listarPaginaWeb();

                context.setVariable("paginas", paginas);

                templateEngine.process("html/Listado_Paginas", context, response.getWriter());
                break;
            case "listadotipospagina":
                RepoTipoPagina repoTipoPagina = new RepoTipoPagina();
                List<TipoPagina> paginasTipoPagina = repoTipoPagina.listarTipoPagina();

                context.setVariable("paginas", paginasTipoPagina);

                templateEngine.process("html/Listado_TiposPagina", context, response.getWriter());
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no reconocida: " + accion);
        }
    }

    private void procesarPostCRM(String path, HttpServletRequest request, WebContext context, HttpServletResponse response)
            throws IOException {
        if (path.isEmpty() || path.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String accion = path.substring(1).split("/")[0].toLowerCase();
        System.out.println("doPost accion CRM: " + accion);

        switch (accion) {
            case "organizacion":
                guardarOrganizacion(request, context, response);
                break;
            case "oportunidad":
                guardarOportunidad(request, context, response);
                break;
            case "producto":
                guardarProducto(request, context, response);
                break;
            case "guardarpagina":
                guardarPagina(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no reconocida: " + accion);
        }
    }

    private void guardarPagina(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String titulo = request.getParameter("titulo");
        String url = request.getParameter("url");
        String contenidoHTML = request.getParameter("contenidoHTML");
        int idTipoPagina = Integer.parseInt(request.getParameter("idTipoPagina"));

        PaginaWeb nueva = new PaginaWeb();
        nueva.setTitulo(titulo);
        nueva.setUrl(url);
        nueva.setContenidoHTML(contenidoHTML);
        nueva.setIdTipoPagina(idTipoPagina);

        RepoPaginaWeb repo = new RepoPaginaWeb();
        repo.crearPaginaWeb(nueva);

        response.sendRedirect(request.getContextPath() + "/crm/listadopaginas");
    }


    private void mostrarConsultasXPath(WebContext context, HttpServletResponse response) throws IOException {
        context.setVariable("obligatoriosOrganizacion",
                lectorXPathCRM.consultar("/formularios/formulario[@id='organizacion']/campo[@obligatorio='true']/@nombre"));
        context.setVariable("numericosProducto",
                lectorXPathCRM.consultar("/formularios/formulario[@id='producto']/campo[@tipo='number']/@nombre"));
        context.setVariable("camposOportunidad",
                lectorXPathCRM.consultar("/formularios/formulario[@id='oportunidad']/campo/@nombre"));
        context.setVariable("rutasFormularios",
                lectorXPathCRM.consultar("/formularios/formulario/@ruta"));

        templateEngine.process("xpathCRM", context, response.getWriter());
    }


    private void generarJsonProductos(WebContext context, HttpServletResponse response) throws IOException {
        try {
            List<FormularioProducto> productos = repoCRM.obtenerProductosConStock();
            String rutaArchivo = obtenerRutaJson();

            generadorJsonCRM.generarProductos(productos, rutaArchivo);

            context.setVariable("exito", "JSON generado correctamente.");
            context.setVariable("rutaArchivo", rutaArchivo);
            context.setVariable("totalProductos", productos.size());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            context.setVariable("error", "No se pudo generar el fichero JSON.");
        }

        templateEngine.process("jsonCRM", context, response.getWriter());
    }

    private String obtenerRutaJson() {
        String rutaWeb = getServletContext().getRealPath("/");
        File carpetaWeb = new File(rutaWeb);
        File carpetaMain = carpetaWeb.getParentFile();
        File archivoJson = new File(carpetaMain, "resources/crm/productos_filtrados.json");

        return archivoJson.getAbsolutePath();
    }

    private void guardarOrganizacion(HttpServletRequest request, WebContext context, HttpServletResponse response)
            throws IOException {
        String nombre = limpiar(request.getParameter("nombre"));
        String direccion = limpiar(request.getParameter("direccion"));
        String telefono = limpiar(request.getParameter("telefono"));
        String email = limpiar(request.getParameter("email"));
        String tipoOrganizacion = limpiar(request.getParameter("tipoOrganizacion"));

        String error = validarOrganizacion(nombre, telefono, email, tipoOrganizacion);

        if (error != null) {
            context.setVariable("organizacion", new FormularioOrganizacion(nombre, direccion, telefono, email, tipoOrganizacion));
            mostrarMensaje("formularioOrganizacion", "error", error, context, response);
        } else {
            try {
                repoCRM.insertarOrganizacion(new FormularioOrganizacion(nombre, direccion, telefono, email, tipoOrganizacion));
                mostrarMensaje("formularioOrganizacion", "exito", "Organización guardada correctamente.", context, response);
            } catch (SQLException e) {
                mostrarMensaje("formularioOrganizacion", "error", "No se pudo guardar la organización.", context, response);
            }
        }
    }

    private void guardarOportunidad(HttpServletRequest request, WebContext context, HttpServletResponse response)
            throws IOException {
        String titulo = limpiar(request.getParameter("titulo"));
        String descripcion = limpiar(request.getParameter("descripcion"));
        String fechaInicio = limpiar(request.getParameter("fechaInicio"));
        String tiposOportunidad = unirValores(request.getParameterValues("tiposOportunidad"));

        String error = validarOportunidad(titulo, fechaInicio, tiposOportunidad);

        if (error != null) {
            context.setVariable("oportunidad", new FormularioOportunidad(titulo, descripcion, fechaInicio, tiposOportunidad));
            mostrarMensaje("formularioOportunidad", "error", error, context, response);
        } else {
            try {
                repoCRM.insertarOportunidad(new FormularioOportunidad(titulo, descripcion, fechaInicio, tiposOportunidad));
                mostrarMensaje("formularioOportunidad", "exito", "Oportunidad guardada correctamente.", context, response);
            } catch (SQLException e) {
                mostrarMensaje("formularioOportunidad", "error", "No se pudo guardar la oportunidad.", context, response);
            }
        }
    }

    private void guardarProducto(HttpServletRequest request, WebContext context, HttpServletResponse response)
            throws IOException {
        String nombre = limpiar(request.getParameter("nombre"));
        String descripcion = limpiar(request.getParameter("descripcion"));
        String precioTexto = limpiar(request.getParameter("precio"));
        String stockTexto = limpiar(request.getParameter("stock"));
        String categoria = limpiar(request.getParameter("categoria"));

        String error = validarProducto(nombre, precioTexto, stockTexto, categoria);

        if (error != null) {
            context.setVariable("producto", new FormularioProducto(nombre, descripcion, precioTexto, stockTexto, categoria));
            mostrarMensaje("formularioProducto", "error", error, context, response);
        } else {
            try {
                repoCRM.insertarProducto(new FormularioProducto(nombre, descripcion, precioTexto, stockTexto, categoria));
                mostrarMensaje("formularioProducto", "exito", "Producto guardado correctamente.", context, response);
            } catch (SQLException e) {
                mostrarMensaje("formularioProducto", "error", "No se pudo guardar el producto.", context, response);
            }
        }
    }

    private String validarOrganizacion(String nombre, String telefono, String email, String tipoOrganizacion) {
        String mensaje = null;

        if (estaVacio(nombre)) {
            mensaje = "El nombre es obligatorio.";
        } else if (estaVacio(email) || !emailValido(email)) {
            mensaje = "El email no es válido.";
        } else if (!telefonoValido(telefono)) {
            mensaje = "El teléfono debe tener entre 9 y 15 números.";
        } else if (estaVacio(tipoOrganizacion)) {
            mensaje = "Debes seleccionar un tipo de organización.";
        }

        return mensaje;
    }

    private String validarOportunidad(String titulo, String fechaInicio, String tiposOportunidad) {
        String mensaje = null;

        if (estaVacio(titulo)) {
            mensaje = "El título es obligatorio.";
        } else if (estaVacio(fechaInicio)) {
            mensaje = "La fecha de inicio es obligatoria.";
        } else if (estaVacio(tiposOportunidad)) {
            mensaje = "Selecciona al menos un tipo de oportunidad.";
        }

        return mensaje;
    }

    private String validarProducto(String nombre, String precioTexto, String stockTexto, String categoria) {
        String mensaje = null;

        if (estaVacio(nombre)) {
            mensaje = "El nombre del producto es obligatorio.";
        } else if (estaVacio(categoria)) {
            mensaje = "La categoría es obligatoria.";
        } else {
            try {
                if (Double.parseDouble(precioTexto) <= 0) {
                    mensaje = "El precio debe ser mayor que 0.";
                }
            } catch (NumberFormatException e) {
                mensaje = "El precio debe ser numérico.";
            }

            if (mensaje == null) {
                try {
                    if (Integer.parseInt(stockTexto) < 0) {
                        mensaje = "El stock no puede ser negativo.";
                    }
                } catch (NumberFormatException e) {
                    mensaje = "El stock debe ser un número entero.";
                }
            }
        }

        return mensaje;
    }

    private void mostrarMensaje(String plantilla, String tipo, String mensaje, WebContext context, HttpServletResponse response)
            throws IOException {
        context.setVariable(tipo, mensaje);
        templateEngine.process("html/" + plantilla, context, response.getWriter());
    }

    private String limpiar(String valor) {
        String texto = "";

        if (valor != null) {
            texto = valor.trim();
        }

        return texto;
    }

    private boolean estaVacio(String valor) {
        boolean vacio = true;

        if (valor != null && !valor.trim().isEmpty()) {
            vacio = false;
        }

        return vacio;
    }

    private boolean emailValido(String email) {
        boolean valido = false;

        if (email != null && email.contains("@") && email.contains(".")) {
            valido = true;
        }

        return valido;
    }

    private boolean telefonoValido(String telefono) {
        boolean valido = true;

        if (!estaVacio(telefono) && !telefono.matches("[0-9]{9,15}")) {
            valido = false;
        }

        return valido;
    }

    private String unirValores(String[] valores) {
        String resultado = "";

        if (valores != null) {
            for (int i = 0; i < valores.length; i++) {
                resultado += valores[i];

                if (i < valores.length - 1) {
                    resultado += ",";
                }
            }
        }

        return resultado;
    }

}
