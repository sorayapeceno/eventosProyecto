package dam.primero.servlet.dashboards;

import dam.primero.repositorio.dashboards.RepoDashboards;
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

public class DashBoardsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String TEXT_HTML_CHARSET_UTF_8 = "text/html;charset=UTF-8";
	public static final String TEMPLATES = "/WEB-INF/templates/dashboards/";
	public static final String SUFFIX = ".html";

	private TemplateEngine templateEngine;
	private JavaxServletWebApplication application;

	@Override
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		application = JavaxServletWebApplication.buildApplication(servletContext);
		WebApplicationTemplateResolver templateResolver =
				new WebApplicationTemplateResolver(application);
		templateResolver.setPrefix(TEMPLATES);
		templateResolver.setSuffix(SUFFIX);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType(TEXT_HTML_CHARSET_UTF_8);
		IServletWebExchange webExchange = application.buildExchange(request, response);
		WebContext context = new WebContext(webExchange, request.getLocale());

		String pathInfo = request.getPathInfo();
		String path = (pathInfo != null) ? pathInfo.trim() : "";

		RepoDashboards repo = new RepoDashboards();

		if (path.isEmpty() || path.equals("/")) {
			templateEngine.process("indexDashboards", context, response.getWriter());
			return;
		}

		String[] partes = path.substring(1).split("/");
		String accion  = partes[0];
		String detalle = partes.length > 1 ? partes[1] : null;

		switch (accion) {

			// ── Eventos ──────────────────────────────────────────────────────
			case "eventos":
				if ("detalle".equals(detalle)) {
					context.setVariable("eventos", repo.detalleEventos());
					context.setVariable("eventosPorEstado",    repo.eventosPorEstado());
					context.setVariable("eventosPorModalidad", repo.eventosPorModalidad());
					templateEngine.process("detalleEventos", context, response.getWriter());
				} else {
					context.setVariable("totalEventos",        repo.contarEventos());
					context.setVariable("eventosPorEstado",    repo.eventosPorEstado());
					context.setVariable("eventosPorModalidad", repo.eventosPorModalidad());
					context.setVariable("eventosPorCiudad",    repo.eventosPorCiudad());
					templateEngine.process("dashboardEventos", context, response.getWriter());
				}
				break;

			// ── Ponencias ─────────────────────────────────────────────────────
			case "ponencias":
				if ("detalle".equals(detalle)) {
					context.setVariable("ponencias",            repo.detallePonencias());
					context.setVariable("ponenciasPorTipo",     repo.ponenciasPorTipo());
					context.setVariable("ponenciasPorNivel",    repo.ponenciasPorNivel());
					templateEngine.process("detallePonencias", context, response.getWriter());
				} else {
					context.setVariable("totalPonencias",       repo.contarPonencias());
					context.setVariable("ponenciasPorTipo",     repo.ponenciasPorTipo());
					context.setVariable("ponenciasPorNivel",    repo.ponenciasPorNivel());
					context.setVariable("ponenciasPorFormato",  repo.ponenciasPorFormato());
					context.setVariable("ponenciasPorTematica", repo.ponenciasPorTematica());
					templateEngine.process("dashboardPonencias", context, response.getWriter());
				}
				break;

			// ── Ponentes ──────────────────────────────────────────────────────
			case "ponentes":
				if ("detalle".equals(detalle)) {
					context.setVariable("ponentes",                    repo.detallePonentes());
					context.setVariable("ponentesPorEspecialidad",     repo.ponentesPorEspecialidad());
					context.setVariable("ponentesPorNivelImparticion", repo.ponentesPorNivelImparticion());
					templateEngine.process("detallePonentes", context, response.getWriter());
				} else {
					context.setVariable("totalPonentes",               repo.contarPonentes());
					context.setVariable("ponentesPorEspecialidad",     repo.ponentesPorEspecialidad());
					context.setVariable("ponentesPorNivelImparticion", repo.ponentesPorNivelImparticion());
					context.setVariable("topPonentes",                 repo.topPonentes());
					templateEngine.process("dashboardPonentes", context, response.getWriter());
				}
				break;

			// ── Ventas ────────────────────────────────────────────────────────
			case "ventas":
				if ("detalle".equals(detalle)) {
					context.setVariable("participantes",   repo.detalleParticipantes());
					context.setVariable("clientesPorCiudad", repo.clientesPorCiudad());
					templateEngine.process("detalleVentas", context, response.getWriter());
				} else {
					context.setVariable("totalClientes",   repo.contarClientes());
					context.setVariable("clientesPorCiudad", repo.clientesPorCiudad());
					templateEngine.process("dashboardVentas", context, response.getWriter());
				}
				break;

			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND,
						"Dashboard no reconocido: " + accion);
		}
	}
}