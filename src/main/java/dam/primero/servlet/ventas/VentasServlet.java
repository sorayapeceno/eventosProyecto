package dam.primero.servlet.ventas;

import dam.primero.modelos.ventas.*;
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

    private final TicketRepository ticketRepo = new TicketRepository();

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
            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());

            templateEngine.process("indexVentas", context, resp.getWriter());

        } catch (Exception e) {
            renderError(req, resp, e.getMessage());
        }
    }

    private void mostrarListadoVentas(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            List<Ticket> tickets = ticketRepo.findAll();

            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());
            context.setVariable("tickets", tickets);

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
