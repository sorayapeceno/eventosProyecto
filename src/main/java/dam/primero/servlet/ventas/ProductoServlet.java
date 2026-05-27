package dam.primero.servlet.ventas;

import dam.primero.modelos.ventas.*;
import dam.primero.repositorio.ventas.ProductoRepository;
import dam.primero.exception.MyException;

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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ProductoServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;

    private static final String TEMPLATES = "/WEB-INF/templates/ventas/";
    private static final String SUFFIX    = ".html";

    private final ProductoRepository repo = new ProductoRepository();

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        application = JavaxServletWebApplication.buildApplication(ctx);
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(application);
        resolver.setPrefix(TEMPLATES);
        resolver.setSuffix(SUFFIX);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();

        switch (path) {
            case "/listar":
                listar(req, resp);
                break;
            case "/formulario":
                formulario(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/producto/listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();

        switch (path) {
            case "/guardar":
                guardar(req, resp);
                break;
            case "/eliminar":
                eliminar(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/producto/listar");
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            List<Producto> productos = repo.findAll();

            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());
            context.setVariable("productos", productos);

            templateEngine.process("html/GestionProductos", context, resp.getWriter());

        } catch (MyException e) {
            renderError(req, resp, e.getMessage());
        }
    }

    private void formulario(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());

            context.setVariable("tiposIVA",        TipoIVA.values());
            context.setVariable("estadosEntrada",  EstadoEntrada.values());
            context.setVariable("generos",         Genero.values());

            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isBlank()) {
                Optional<Producto> encontrado = repo.findById(Long.parseLong(idStr));
                if (encontrado.isPresent()) {
                    Producto prod = encontrado.get();
                    context.setVariable("producto",      prod);
                    context.setVariable("tipoProducto",  prod.getTipoProducto().name());

                    // IDs de subtipo para mantenerlos en el formulario de edición
                    if (prod instanceof Entrada) {
                        context.setVariable("idEntrada", ((Entrada) prod).getId_Entrada());
                    }
                    if (prod instanceof VIP) {
                        context.setVariable("idVip",     ((VIP) prod).getId_Vip());
                    } else if (prod instanceof Estandar) {
                        context.setVariable("idEstandar",((Estandar) prod).getId_Estandar());
                    } else if (prod instanceof Beca) {
                        context.setVariable("idBeca",    ((Beca) prod).getId_Beca());
                    }
                    if (prod instanceof Textil) {
                        context.setVariable("idTextil",  ((Textil) prod).getId_Textil());
                    }
                    if (prod instanceof Camisetas) {
                        context.setVariable("idCamisetas",((Camisetas) prod).getId_Camisetas());
                    } else if (prod instanceof Accesorios) {
                        context.setVariable("idAccesorios",((Accesorios) prod).getId_Accesorios());
                    } else if (prod instanceof Otros) {
                        context.setVariable("idOtros",   ((Otros) prod).getId_Otros());
                    }
                }
            }

            templateEngine.process("html/FormularioProducto", context, resp.getWriter());

        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            String idStr         = req.getParameter("id");
            String tipoStr       = req.getParameter("tipoProducto");
            boolean esEdicion    = idStr != null && !idStr.isBlank();

            if (tipoStr == null || tipoStr.isBlank()) {
                renderError(req, resp, "Debes seleccionar un tipo de producto.");
                return;
            }

            TipoProducto tipo;
            try {
                tipo = TipoProducto.valueOf(tipoStr.toUpperCase());
            } catch (IllegalArgumentException ex) {
                renderError(req, resp, "Tipo de producto no válido: " + tipoStr);
                return;
            }

            // ── Campos base ──
            long   idProd      = esEdicion ? Long.parseLong(idStr) : repo.getNextId();
            String nombre      = req.getParameter("nombreProducto");
            double precio      = parseDouble(req.getParameter("precio"), 0.0);
            int    stock       = parseInt(req.getParameter("stockDisponible"), 0);
            String descProd    = req.getParameter("descripcionProducto");
            TipoIVA tipoIVA    = TipoIVA.fromString(req.getParameter("tipoIVA"));
            double  descuento  = parseDouble(req.getParameter("descuento"), 0.0);

            // ── Campos Entrada (compartidos por VIP, Estandar, Beca) ──
            String zona             = req.getParameter("zona");
            String asiento          = req.getParameter("asiento");
            String accesoPermitido  = req.getParameter("accesoPermitido");
            String accesosAdicionales = req.getParameter("accesosAdicionales");
            int    validezHoras     = parseInt(req.getParameter("validezHoras"), 0);
            EstadoEntrada estado    = parseEstado(req.getParameter("estadoEntrada"));

            // ── Campos Textil (compartidos por Camiseta, Accesorio) ──
            String talla      = req.getParameter("talla");
            String color      = req.getParameter("color");
            String material   = req.getParameter("material");
            Genero genero     = parseGenero(req.getParameter("genero"));
            String tipoTextil = req.getParameter("tipoTextil");

            Producto producto;

            switch (tipo) {
                case VIP: {
                    VIP p = new VIP();
                    fillBase(p, idProd, nombre, precio, stock, descProd, tipoIVA, descuento);
                    fillEntradaObj(p, zona, asiento, accesoPermitido, accesosAdicionales, validezHoras, estado);
                    if (esEdicion) p.setId_Entrada(parseLong(req.getParameter("idEntrada"), 0));
                    if (esEdicion) p.setId_Vip(parseLong(req.getParameter("idVip"), 0));
                    p.setBeneficiosIncluidos(req.getParameter("beneficiosIncluidos"));
                    p.setServiciosAdicionales(req.getParameter("serviciosAdicionales"));
                    p.setPrecioExtra(parseDouble(req.getParameter("precioExtra"), 0.0));
                    producto = p;
                    break;
                }
                case ESTANDAR: {
                    Estandar p = new Estandar();
                    fillBase(p, idProd, nombre, precio, stock, descProd, tipoIVA, descuento);
                    fillEntradaObj(p, zona, asiento, accesoPermitido, accesosAdicionales, validezHoras, estado);
                    if (esEdicion) p.setId_Entrada(parseLong(req.getParameter("idEntrada"), 0));
                    if (esEdicion) p.setId_Estandar(parseLong(req.getParameter("idEstandar"), 0));
                    p.setIncluyeRegalo(req.getParameter("incluyeRegalo"));
                    producto = p;
                    break;
                }
                case BECA: {
                    Beca p = new Beca();
                    fillBase(p, idProd, nombre, precio, stock, descProd, tipoIVA, descuento);
                    fillEntradaObj(p, zona, asiento, accesoPermitido, accesosAdicionales, validezHoras, estado);
                    if (esEdicion) p.setId_Entrada(parseLong(req.getParameter("idEntrada"), 0));
                    if (esEdicion) p.setId_Beca(parseLong(req.getParameter("idBeca"), 0));
                    p.setMotivoBeca(req.getParameter("motivoBeca"));
                    p.setPorcentajeDescuento(parseDouble(req.getParameter("porcentajeDescuento"), 0.0));
                    p.setRequisitos(req.getParameter("requisitos"));
                    producto = p;
                    break;
                }
                case CAMISETA: {
                    Camisetas p = new Camisetas();
                    fillBase(p, idProd, nombre, precio, stock, descProd, tipoIVA, descuento);
                    fillTextilObj(p, talla, color, material, genero, tipoTextil);
                    if (esEdicion) p.setId_Textil(parseLong(req.getParameter("idTextil"), 0));
                    if (esEdicion) p.setId_Camisetas(parseLong(req.getParameter("idCamisetas"), 0));
                    p.setModelo(req.getParameter("modelo"));
                    p.setEstampado(req.getParameter("estampado"));
                    producto = p;
                    break;
                }
                case ACCESORIO: {
                    Accesorios p = new Accesorios();
                    fillBase(p, idProd, nombre, precio, stock, descProd, tipoIVA, descuento);
                    fillTextilObj(p, talla, color, material, genero, tipoTextil);
                    if (esEdicion) p.setId_Textil(parseLong(req.getParameter("idTextil"), 0));
                    if (esEdicion) p.setId_Accesorios(parseLong(req.getParameter("idAccesorios"), 0));
                    p.setTipoAccesorio(req.getParameter("tipoAccesorio"));
                    producto = p;
                    break;
                }
                default: { // OTROS
                    Otros p = new Otros();
                    fillBase(p, idProd, nombre, precio, stock, descProd, tipoIVA, descuento);
                    if (esEdicion) p.setId_Otros(parseLong(req.getParameter("idOtros"), 0));
                    p.setDescripcion(req.getParameter("descripcionOtros"));
                    producto = p;
                    break;
                }
            }

            if (esEdicion) {
                repo.update(producto);
            } else {
                repo.save(producto);
            }

            resp.sendRedirect(req.getContextPath() + "/producto/listar?msg=guardado");

        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, "Error al guardar producto: " + e.getMessage());
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            boolean eliminado = repo.deleteById(id);
            if (eliminado) {
                resp.sendRedirect(req.getContextPath() + "/producto/listar?msg=eliminado");
            } else {
                resp.sendRedirect(req.getContextPath() + "/producto/listar?error=tieneVentas");
            }
        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, "Error al eliminar producto: " + e.getMessage());
        }
    }

    private void fillBase(Producto p, long id, String nombre, double precio,
                          int stock, String desc, TipoIVA iva, double dto) {
        p.setId_Producto(id);
        p.setNombreProducto(nombre);
        p.setPrecio(precio);
        p.setStockDisponible(stock);
        p.setDescripcionProducto(desc);
        p.setTipoIVA(iva);
        p.setDescuento(dto);
    }

    private void fillEntradaObj(Entrada e, String zona, String asiento,
                                 String accesoPermitido, String accesosAdicionales,
                                 int validezHoras, EstadoEntrada estado) {
        e.setZona(zona);
        e.setAsiento(asiento);
        e.setAccesoPermitido(accesoPermitido);
        e.setAccesosAdicionales(accesosAdicionales);
        e.setValidezHoras(validezHoras);
        e.setEstadoEntrada(estado);
    }

    private void fillTextilObj(Textil t, String talla, String color,
                                String material, Genero genero, String tipoTextil) {
        t.setTalla(talla);
        t.setColor(color);
        t.setMaterial(material);
        t.setGenero(genero);
        t.setTipoTextil(tipoTextil);
    }

    private double parseDouble(String val, double def) {
        if (val == null || val.isBlank()) return def;
        try { return Double.parseDouble(val); } catch (NumberFormatException e) { return def; }
    }

    private int parseInt(String val, int def) {
        if (val == null || val.isBlank()) return def;
        try { return Integer.parseInt(val); } catch (NumberFormatException e) { return def; }
    }

    private long parseLong(String val, long def) {
        if (val == null || val.isBlank()) return def;
        try { return Long.parseLong(val); } catch (NumberFormatException e) { return def; }
    }

    private EstadoEntrada parseEstado(String val) {
        if (val == null || val.isBlank()) return EstadoEntrada.ACTIVA;
        try { return EstadoEntrada.valueOf(val.toUpperCase()); }
        catch (IllegalArgumentException e) { return EstadoEntrada.ACTIVA; }
    }

    private Genero parseGenero(String val) {
        if (val == null || val.isBlank()) return null;
        try { return Genero.valueOf(val.toUpperCase()); }
        catch (IllegalArgumentException e) { return null; }
    }

    private void renderError(HttpServletRequest req, HttpServletResponse resp, String mensaje)
            throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        IServletWebExchange webExchange = application.buildExchange(req, resp);
        WebContext context = new WebContext(webExchange, req.getLocale());
        context.setVariable("error", mensaje);
        templateEngine.process("html/error", context, resp.getWriter());
    }
}
