package dam.primero.servlet.crm;

import dam.primero.modelos.crm.FormularioOportunidad;
import dam.primero.modelos.crm.FormularioOrganizacion;
import dam.primero.modelos.crm.FormularioProducto;
import dam.primero.modelos.crm.PaginaWeb;
import dam.primero.modelos.crm.TipoPagina;
import dam.primero.repositorio.crm.GeneradorJsonCRM;
import dam.primero.repositorio.crm.LectorXPathCRM;
import dam.primero.repositorio.crm.RepoCRM;
import dam.primero.repositorio.crm.RepoPaginaWeb;
import dam.primero.repositorio.crm.RepoTipoPagina;
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

    private static final long serialVersionUID = 1L;
    private static final String TEXT_HTML = "text/html;charset=UTF-8";
    private static final String TEMPLATES = "/WEB-INF/templates/crm/";
    private static final String SUFFIX = ".html";

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;
    private RepoCRM repoCRM;
    private LectorXPathCRM lectorXPathCRM;
    private GeneradorJsonCRM generadorJsonCRM;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        application = JavaxServletWebApplication.buildApplication(servletContext);

        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
        templateResolver.setPrefix(TEMPLATES);
        templateResolver.setSuffix(SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        repoCRM = new RepoCRM();
        lectorXPathCRM = new LectorXPathCRM();
        generadorJsonCRM = new GeneradorJsonCRM();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType(TEXT_HTML);

        IServletWebExchange webExchange = application.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, request.getLocale());

        String servletPath = request.getServletPath();
        String path = request.getPathInfo();

        if (path == null || path.isEmpty()) {
            path = "/";
        }

        if ("/".equals(servletPath)) {
            templateEngine.process("index", context, response.getWriter());
        } else if ("/crm".equals(servletPath)) {
            switch (path) {
                case "/":
                    templateEngine.process("indexCRM", context, response.getWriter());
                    break;
                case "/organizacion":
                    templateEngine.process("html/formularioOrganizacion", context, response.getWriter());
                    break;
                case "/oportunidad":
                    templateEngine.process("html/formularioOportunidad", context, response.getWriter());
                    break;
                case "/producto":
                    templateEngine.process("html/formularioProducto", context, response.getWriter());
                    break;
                case "/xpath":
                    mostrarXPath(context, response);
                    break;
                case "/generar-json":
                    generarJson(context, response);
                    break;
                case "/crearpagina":
                    templateEngine.process("html/Crear_Pagina", context, response.getWriter());
                    break;
                case "/listadopaginas":
                    listarPaginas(context, response);
                    break;
                case "/listadotipospagina":
                    listarTiposPagina(context, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType(TEXT_HTML);

        IServletWebExchange webExchange = application.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, request.getLocale());

        String path = request.getPathInfo();

        if (path == null || path.isEmpty()) {
            path = "/";
        }

        switch (path) {
            case "/organizacion":
                guardarOrganizacion(request, context, response);
                break;
            case "/oportunidad":
                guardarOportunidad(request, context, response);
                break;
            case "/producto":
                guardarProducto(request, context, response);
                break;
            case "/guardarpagina":
                guardarPagina(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listarPaginas(WebContext context, HttpServletResponse response) throws IOException {
        RepoPaginaWeb repo = new RepoPaginaWeb();
        List<PaginaWeb> paginas = repo.listarPaginaWeb();
        context.setVariable("paginas", paginas);
        templateEngine.process("html/Listado_Paginas", context, response.getWriter());
    }

    private void listarTiposPagina(WebContext context, HttpServletResponse response) throws IOException {
        RepoTipoPagina repo = new RepoTipoPagina();
        List<TipoPagina> tiposPagina = repo.listarTipoPagina();
        context.setVariable("paginas", tiposPagina);
        templateEngine.process("html/Listado_TiposPagina", context, response.getWriter());
    }

    private void guardarPagina(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String titulo = request.getParameter("titulo");
        String url = request.getParameter("url");
        String contenidoHTML = request.getParameter("contenidoHTML");
        int idTipoPagina = Integer.parseInt(request.getParameter("idTipoPagina"));

        PaginaWeb pagina = new PaginaWeb();
        pagina.setTitulo(titulo);
        pagina.setUrl(url);
        pagina.setContenidoHTML(contenidoHTML);
        pagina.setIdTipoPagina(idTipoPagina);

        RepoPaginaWeb repo = new RepoPaginaWeb();
        repo.crearPaginaWeb(pagina);

        response.sendRedirect(request.getContextPath() + "/crm/listadopaginas");
    }

    private void mostrarXPath(WebContext context, HttpServletResponse response) throws IOException {
        List<String> obligatorios = lectorXPathCRM.consultar("/formularios/formulario[@id='organizacion']/campo[@obligatorio='true']/@nombre");
        List<String> numericos = lectorXPathCRM.consultar("/formularios/formulario[@id='producto']/campo[@tipo='number']/@nombre");
        List<String> oportunidad = lectorXPathCRM.consultar("/formularios/formulario[@id='oportunidad']/campo/@nombre");
        List<String> rutas = lectorXPathCRM.consultar("/formularios/formulario/@ruta");

        context.setVariable("obligatoriosOrganizacion", obligatorios);
        context.setVariable("numericosProducto", numericos);
        context.setVariable("camposOportunidad", oportunidad);
        context.setVariable("rutasFormularios", rutas);

        templateEngine.process("xpathCRM", context, response.getWriter());
    }

    private void generarJson(WebContext context, HttpServletResponse response) throws IOException {
        try {
            List<FormularioProducto> productos = repoCRM.obtenerProductosConStock();
            String rutaArchivo = obtenerRutaJson();

            generadorJsonCRM.generarProductos(productos, rutaArchivo);

            context.setVariable("exito", "JSON generado correctamente.");
            context.setVariable("rutaArchivo", rutaArchivo);
            context.setVariable("totalProductos", productos.size());
        } catch (SQLException | IOException e) {
            context.setVariable("error", "No se pudo generar el fichero JSON.");
        }

        templateEngine.process("jsonCRM", context, response.getWriter());
    }

    private String obtenerRutaJson() {
        String rutaWeb = getServletContext().getRealPath("/");
        File carpetaWeb = new File(rutaWeb);
        File carpetaMain = carpetaWeb.getParentFile();
        File archivoJson = new File(carpetaMain, "resources/crm/productos_filtrados.json");
        String ruta = archivoJson.getAbsolutePath();
        return ruta;
    }

    private void guardarOrganizacion(HttpServletRequest request, WebContext context, HttpServletResponse response)
            throws IOException {
        String nombre = limpiar(request.getParameter("nombre"));
        String direccion = limpiar(request.getParameter("direccion"));
        String telefono = limpiar(request.getParameter("telefono"));
        String email = limpiar(request.getParameter("email"));
        String tipoOrganizacion = limpiar(request.getParameter("tipoOrganizacion"));

        FormularioOrganizacion organizacion = new FormularioOrganizacion(nombre, direccion, telefono, email, tipoOrganizacion);
        String mensaje = validarOrganizacion(organizacion);

        context.setVariable("organizacion", organizacion);

        if (mensaje == null) {
            try {
                repoCRM.insertarOrganizacion(organizacion);
                context.setVariable("exito", "Organización guardada correctamente.");
            } catch (SQLException e) {
                context.setVariable("error", "No se pudo guardar la organización.");
            }
        } else {
            context.setVariable("error", mensaje);
        }

        templateEngine.process("html/formularioOrganizacion", context, response.getWriter());
    }

    private void guardarOportunidad(HttpServletRequest request, WebContext context, HttpServletResponse response)
            throws IOException {
        String titulo = limpiar(request.getParameter("titulo"));
        String descripcion = limpiar(request.getParameter("descripcion"));
        String fechaInicio = limpiar(request.getParameter("fechaInicio"));
        String tiposOportunidad = unirValores(request.getParameterValues("tiposOportunidad"));

        FormularioOportunidad oportunidad = new FormularioOportunidad(titulo, descripcion, fechaInicio, tiposOportunidad);
        String mensaje = validarOportunidad(oportunidad);

        context.setVariable("oportunidad", oportunidad);

        if (mensaje == null) {
            try {
                repoCRM.insertarOportunidad(oportunidad);
                context.setVariable("exito", "Oportunidad guardada correctamente.");
            } catch (SQLException e) {
                context.setVariable("error", "No se pudo guardar la oportunidad.");
            }
        } else {
            context.setVariable("error", mensaje);
        }

        templateEngine.process("html/formularioOportunidad", context, response.getWriter());
    }

    private void guardarProducto(HttpServletRequest request, WebContext context, HttpServletResponse response)
            throws IOException {
        String nombre = limpiar(request.getParameter("nombre"));
        String descripcion = limpiar(request.getParameter("descripcion"));
        String precio = limpiar(request.getParameter("precio"));
        String stock = limpiar(request.getParameter("stock"));
        String categoria = limpiar(request.getParameter("categoria"));

        FormularioProducto producto = new FormularioProducto(nombre, descripcion, precio, stock, categoria);
        String mensaje = validarProducto(producto);

        context.setVariable("producto", producto);

        if (mensaje == null) {
            try {
                repoCRM.insertarProducto(producto);
                context.setVariable("exito", "Producto guardado correctamente.");
            } catch (SQLException e) {
                context.setVariable("error", "No se pudo guardar el producto.");
            }
        } else {
            context.setVariable("error", mensaje);
        }

        templateEngine.process("html/formularioProducto", context, response.getWriter());
    }

    private String validarOrganizacion(FormularioOrganizacion organizacion) {
        String mensaje = null;

        if (estaVacio(organizacion.getNombre())) {
            mensaje = "El nombre es obligatorio.";
        } else if (estaVacio(organizacion.getEmail()) || !emailValido(organizacion.getEmail())) {
            mensaje = "El email no es válido.";
        } else if (!telefonoValido(organizacion.getTelefono())) {
            mensaje = "El teléfono debe tener entre 9 y 15 números.";
        } else if (estaVacio(organizacion.getTipoOrganizacion())) {
            mensaje = "Debes seleccionar un tipo de organización.";
        }

        return mensaje;
    }

    private String validarOportunidad(FormularioOportunidad oportunidad) {
        String mensaje = null;

        if (estaVacio(oportunidad.getTitulo())) {
            mensaje = "El título es obligatorio.";
        } else if (estaVacio(oportunidad.getFechaInicio())) {
            mensaje = "La fecha de inicio es obligatoria.";
        } else if (estaVacio(oportunidad.getTiposOportunidad())) {
            mensaje = "Selecciona al menos un tipo de oportunidad.";
        }

        return mensaje;
    }

    private String validarProducto(FormularioProducto producto) {
        String mensaje = null;

        if (estaVacio(producto.getNombre())) {
            mensaje = "El nombre del producto es obligatorio.";
        } else if (estaVacio(producto.getCategoria())) {
            mensaje = "La categoría es obligatoria.";
        } else {
            mensaje = validarPrecio(producto.getPrecio());

            if (mensaje == null) {
                mensaje = validarStock(producto.getStock());
            }
        }

        return mensaje;
    }

    private String validarPrecio(String precio) {
        String mensaje = null;

        try {
            if (Double.parseDouble(precio) <= 0) {
                mensaje = "El precio debe ser mayor que 0.";
            }
        } catch (NumberFormatException e) {
            mensaje = "El precio debe ser numérico.";
        }

        return mensaje;
    }

    private String validarStock(String stock) {
        String mensaje = null;

        try {
            if (Integer.parseInt(stock) < 0) {
                mensaje = "El stock no puede ser negativo.";
            }
        } catch (NumberFormatException e) {
            mensaje = "El stock debe ser un número entero.";
        }

        return mensaje;
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
