package dam.primero.servlet.ventas;

import dam.primero.modelos.ventas.*;
import dam.primero.repositorio.ventas.AsistenteRepository;
import dam.primero.repositorio.ventas.ProductoRepository;
import dam.primero.repositorio.ventas.TicketRepository;
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
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class TicketServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;

    private static final String TEMPLATES       = "/WEB-INF/templates/ventas/";
    private static final String SUFFIX          = ".html";
    private static final String CARRITO_SESSION = "carrito";

    private final AsistenteRepository asistenteRepo = new AsistenteRepository();
    private final ProductoRepository  productoRepo  = new ProductoRepository();
    private final TicketRepository    ticketRepo    = new TicketRepository();

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
            case "/nuevo":
                mostrarFormularioNuevoTicket(req, resp);
                break;
            case "/carritoAgregar":
                agregarAlCarrito(req, resp);
                break;
            case "/carritoEliminar":
                eliminarDelCarrito(req, resp);
                break;
            case "/carritoClear":
                limpiarCarrito(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ventas/verListadoVentas");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();

        if ("/confirmar".equals(path)) {
            confirmarTicket(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/ventas/verListadoVentas");
        }
    }

    private void mostrarFormularioNuevoTicket(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            List<Asistente> asistentes = asistenteRepo.findAll();
            List<Producto>  productos  = productoRepo.findAll();

            HttpSession        session = req.getSession();
            List<LineaCarrito> carrito = getCarrito(session);

            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());
            context.setVariable("asistentes",   asistentes);
            context.setVariable("productos",    productos);
            context.setVariable("metodoPagos",  MetodoPago.values());
            context.setVariable("carrito",      carrito);
            context.setVariable("totalCarrito", calcularTotalCarrito(carrito));

            templateEngine.process("html/NuevoTicket", context, resp.getWriter());

        } catch (MyException e) {
            renderError(req, resp, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void agregarAlCarrito(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            long idProducto = Long.parseLong(req.getParameter("idProducto"));
            int  cantidad   = Integer.parseInt(req.getParameter("cantidad"));

            if (cantidad <= 0) {
                resp.sendRedirect(req.getContextPath() + "/ticket/nuevo?error=cantidadInvalida");
                return;
            }

            Producto producto = productoRepo.findById(idProducto)
                .orElseThrow(() -> new MyException("Producto no encontrado"));

            if (producto.getStockDisponible() < cantidad) {
                resp.sendRedirect(req.getContextPath() + "/ticket/nuevo?error=sinStock");
                return;
            }

            HttpSession        session  = req.getSession();
            List<LineaCarrito> carrito  = getCarrito(session);

            Optional<LineaCarrito> existente = carrito.stream()
                .filter(lc -> lc.getIdProducto() == idProducto)
                .findFirst();

            if (existente.isPresent()) {
                existente.get().setCantidad(existente.get().getCantidad() + cantidad);
            } else {
                carrito.add(new LineaCarrito(idProducto, producto.getNombreProducto(),
                                             producto.getPrecioConDescuento(), cantidad));
            }

            session.setAttribute(CARRITO_SESSION, carrito);
            resp.sendRedirect(req.getContextPath() + "/ticket/nuevo");

        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, "Error al añadir al carrito: " + e.getMessage());
        }
    }

    private void eliminarDelCarrito(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            long idProducto = Long.parseLong(req.getParameter("idProducto"));
            HttpSession        session = req.getSession();
            List<LineaCarrito> carrito = getCarrito(session);
            carrito.removeIf(lc -> lc.getIdProducto() == idProducto);
            session.setAttribute(CARRITO_SESSION, carrito);
        } catch (NumberFormatException ignored) {}
        resp.sendRedirect(req.getContextPath() + "/ticket/nuevo");
    }

    private void limpiarCarrito(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.getSession().removeAttribute(CARRITO_SESSION);
        resp.sendRedirect(req.getContextPath() + "/ticket/nuevo");
    }

    private void confirmarTicket(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            HttpSession        session = req.getSession();
            List<LineaCarrito> carrito = getCarrito(session);

            if (carrito.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/ticket/nuevo?error=carritoVacio");
                return;
            }

            long   idAsistente    = Long.parseLong(req.getParameter("idAsistente"));
            String metodoPagoStr  = req.getParameter("metodoPago");
            String codPromocional = req.getParameter("codPromocional");
            double descuento      = 0.0;

            if (codPromocional != null && !codPromocional.isBlank()) {
                if ("BIENVENIDA".equalsIgnoreCase(codPromocional))       descuento = 5.0;
                else if ("PROMO_DEPOR".equalsIgnoreCase(codPromocional)) descuento = 10.0;
            }

            Asistente asistente = asistenteRepo.findById(idAsistente)
                .orElseThrow(() -> new MyException("Asistente no encontrado"));

            MetodoPago metodoPago;
            try {
                metodoPago = MetodoPago.valueOf(metodoPagoStr.toUpperCase());
            } catch (Exception e) {
                metodoPago = MetodoPago.TARJETA;
            }

            long ticketId = ticketRepo.getNextTicketId();
            Ticket ticket = new Ticket(
                ticketId, asistente,
                "QR-" + String.format("%04d", (int)(Math.random() * 9999)),
                LocalDate.now(), metodoPago, descuento, codPromocional
            );

            long lineaId = ticketRepo.getNextLineaId();
            for (LineaCarrito lc : carrito) {
                Producto prod = productoRepo.findById(lc.getIdProducto())
                    .orElseThrow(() -> new MyException(
                        "Producto no encontrado: " + lc.getNombreProducto()));

                LineaTicket linea = new LineaTicket();
                linea.setId_LineaTicket(lineaId++);
                linea.setProducto(prod);
                linea.setCantidad(lc.getCantidad());
                linea.calcularTotales();
                ticket.addLinea(linea);
            }

            ticketRepo.saveConLineas(ticket);

            double nuevoTotal = asistente.getTotalGastado() + ticket.getPrecioFinal();
            asistenteRepo.actualizarTotalGastado(idAsistente, nuevoTotal);

            session.removeAttribute(CARRITO_SESSION);

            resp.sendRedirect(req.getContextPath() +
                              "/ventas/detalleTicket?id=" + ticketId + "&msg=creado");

        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, "Error al confirmar ticket: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private List<LineaCarrito> getCarrito(HttpSession session) {
        List<LineaCarrito> carrito =
            (List<LineaCarrito>) session.getAttribute(CARRITO_SESSION);
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(CARRITO_SESSION, carrito);
        }
        return carrito;
    }

    private double calcularTotalCarrito(List<LineaCarrito> carrito) {
        return carrito.stream()
                      .mapToDouble(lc -> lc.getPrecioUnitario() * lc.getCantidad())
                      .sum();
    }

    private void renderError(HttpServletRequest req, HttpServletResponse resp, String mensaje)
            throws IOException {
        IServletWebExchange webExchange = application.buildExchange(req, resp);
        WebContext context = new WebContext(webExchange, req.getLocale());
        context.setVariable("error", mensaje);
        templateEngine.process("html/error", context, resp.getWriter());
    }

    public static class LineaCarrito implements java.io.Serializable {
        private long   idProducto;
        private String nombreProducto;
        private double precioUnitario;
        private int    cantidad;

        public LineaCarrito() {}

        public LineaCarrito(long idProducto, String nombreProducto,
                            double precioUnitario, int cantidad) {
            this.idProducto     = idProducto;
            this.nombreProducto = nombreProducto;
            this.precioUnitario = precioUnitario;
            this.cantidad       = cantidad;
        }

        public long   getId_Producto()        { return idProducto; }
        public long   getIdProducto()         { return idProducto; }
        public void   setIdProducto(long v)   { this.idProducto = v; }
        public String getNombreProducto()     { return nombreProducto; }
        public void   setNombreProducto(String v) { this.nombreProducto = v; }
        public double getPrecioUnitario()     { return precioUnitario; }
        public void   setPrecioUnitario(double v) { this.precioUnitario = v; }
        public int    getCantidad()           { return cantidad; }
        public void   setCantidad(int v)      { this.cantidad = v; }
        public double getSubtotal()           { return precioUnitario * cantidad; }
    }
}
