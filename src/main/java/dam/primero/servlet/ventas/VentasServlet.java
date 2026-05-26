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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VentasServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;

    private static final String TEMPLATES = "/WEB-INF/templates/ventas/";
    private static final String SUFFIX    = ".html";

    private final TicketRepository    ticketRepo    = new TicketRepository();
    private final AsistenteRepository asistenteRepo = new AsistenteRepository();
    private final ProductoRepository  productoRepo  = new ProductoRepository();

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
            case "/":
            case "/index":
                mostrarIndex(req, resp);
                break;
            case "/verListadoVentas":
                mostrarListadoVentas(req, resp);
                break;
            case "/detalleTicket":
                mostrarDetalleTicket(req, resp);
                break;
            case "/verAsistentes":
                mostrarListadoAsistentes(req, resp);
                break;
            case "/verProductos":
                mostrarListadoProductos(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ventas/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();

        switch (path) {
            case "/cancelarTicket":
                cancelarTicket(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/ventas/verListadoVentas");
        }
    }

    private void mostrarIndex(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            List<Ticket>   tickets   = ticketRepo.findAll();
            List<Producto> productos = productoRepo.findAll();
            long         totalAsis = asistenteRepo.findAll().size();

            double totalFacturado = tickets.stream()
                    .mapToDouble(Ticket::getPrecioFinal).sum();

            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());
            context.setVariable("totalTickets",    tickets.size());
            context.setVariable("totalAsistentes", totalAsis);
            context.setVariable("totalProductos",  productos.size());
            context.setVariable("totalFacturado",  String.format("%.2f", totalFacturado));

            templateEngine.process("indexVentas", context, resp.getWriter());

        } catch (MyException e) {
            renderError(req, resp, e.getMessage());
        }
    }

    private void mostrarListadoVentas(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            List<Ticket> tickets = ticketRepo.findAll();

            double totalFacturado = tickets.stream()
                    .mapToDouble(Ticket::getPrecioFinal).sum();

            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());
            context.setVariable("tickets",        tickets);
            context.setVariable("totalFacturado", String.format("%.2f", totalFacturado));
            context.setVariable("totalTickets",   tickets.size());

            templateEngine.process("html/ListadoVentas", context, resp.getWriter());

        } catch (MyException e) {
            renderError(req, resp, e.getMessage());
        }
    }

    private void mostrarDetalleTicket(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isBlank()) {
                resp.sendRedirect(req.getContextPath() + "/ventas/verListadoVentas");
                return;
            }

            long id = Long.parseLong(idStr);
            Optional<Ticket> resultado = ticketRepo.findByIdConLineas(id);

            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());

            if (resultado.isPresent()) {
                context.setVariable("ticket", resultado.get());
                templateEngine.process("html/DetalleTicket", context, resp.getWriter());
            } else {
                context.setVariable("error", "Ticket no encontrado");
                templateEngine.process("html/error", context, resp.getWriter());
            }

        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, "Error al cargar el ticket: " + e.getMessage());
        }
    }

    private void mostrarListadoAsistentes(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            List<Asistente> asistentes = asistenteRepo.findAll();

            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());
            context.setVariable("asistentes", asistentes);

            templateEngine.process("html/ListadoAsistentes", context, resp.getWriter());

        } catch (MyException e) {
            renderError(req, resp, e.getMessage());
        }
    }

    private void mostrarListadoProductos(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            List<Producto> productos = productoRepo.findAll();

            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());
            context.setVariable("productos", productos);

            templateEngine.process("html/GestionProductos", context, resp.getWriter());

        } catch (MyException e) {
            renderError(req, resp, e.getMessage());
        }
    }

    private void cancelarTicket(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            ticketRepo.cancelar(id);
            resp.sendRedirect(req.getContextPath() + "/ventas/verListadoVentas?msg=cancelado");
        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, "Error al cancelar ticket: " + e.getMessage());
        }
    }

    private void renderError(HttpServletRequest req, HttpServletResponse resp, String mensaje)
            throws IOException {
        IServletWebExchange webExchange = application.buildExchange(req, resp);
        WebContext context = new WebContext(webExchange, req.getLocale());
        context.setVariable("error", mensaje);
        templateEngine.process("html/error", context, resp.getWriter());
    }
}
