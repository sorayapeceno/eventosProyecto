package dam.primero.servlet.ventas;

import dam.primero.modelos.ventas.Asistente;
import dam.primero.repositorio.ventas.AsistenteRepository;
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

public class AsistenteServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;

    private static final String TEMPLATES = "/WEB-INF/templates/ventas/";
    private static final String SUFFIX    = ".html";

    private final AsistenteRepository repo = new AsistenteRepository();

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
                resp.sendRedirect(req.getContextPath() + "/asistente/listar");
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
                resp.sendRedirect(req.getContextPath() + "/asistente/listar");
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            List<Asistente> asistentes = repo.findAll();

            resp.setContentType("text/html;charset=UTF-8");
            IServletWebExchange webExchange = application.buildExchange(req, resp);
            WebContext context = new WebContext(webExchange, req.getLocale());
            context.setVariable("asistentes", asistentes);

            templateEngine.process("html/ListadoAsistentes", context, resp.getWriter());

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

            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isBlank()) {
                Optional<Asistente> encontrado = repo.findById(Long.parseLong(idStr));
                encontrado.ifPresent(a -> context.setVariable("asistente", a));
            }

            templateEngine.process("html/FormularioAsistente", context, resp.getWriter());

        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, e.getMessage());
        }
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            String idStr = req.getParameter("id");
            Asistente a = new Asistente();

            if (idStr != null && !idStr.isBlank()) {
                a.setId_Asistente(Long.parseLong(idStr));
            } else {
                a.setId_Asistente(repo.getNextId());
            }

            a.setTematica(req.getParameter("tematica"));
            a.setDireccion(req.getParameter("direccion"));
            a.setObservaciones(req.getParameter("observaciones"));
            a.setBio(req.getParameter("bio"));
            a.setNivelImparticion(req.getParameter("nivelImparticion"));

            String totalStr = req.getParameter("totalGastado");
            a.setTotalGastado(totalStr != null && !totalStr.isBlank()
                              ? Double.parseDouble(totalStr) : 0.0);

            if (idStr != null && !idStr.isBlank()) {
                repo.update(a);
            } else {
                repo.save(a);
            }

            resp.sendRedirect(req.getContextPath() + "/asistente/listar?msg=guardado");

        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, "Error al guardar asistente: " + e.getMessage());
        }
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            repo.deleteById(id);
            resp.sendRedirect(req.getContextPath() + "/asistente/listar?msg=eliminado");
        } catch (MyException | NumberFormatException e) {
            renderError(req, resp, "Error al eliminar asistente: " + e.getMessage());
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
