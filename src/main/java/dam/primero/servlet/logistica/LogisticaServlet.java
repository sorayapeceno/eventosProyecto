package dam.primero.servlet.logistica;

import dam.primero.modelos.logistica.modelo.Mercancia;
import dam.primero.repositorio.logistica.Repositorio_Mercancias;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class LogisticaServlet extends HttpServlet {

	private TemplateEngine templateEngine;
	private JavaxServletWebApplication application;

	@Override
	public void init() {
		ServletContext ctx = getServletContext();
		application = JavaxServletWebApplication.buildApplication(ctx);

		WebApplicationTemplateResolver resolver =
				new WebApplicationTemplateResolver(application);

		resolver.setPrefix("/WEB-INF/templates/logistica/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);

		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(resolver);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html;charset=UTF-8");

		IServletWebExchange exchange = application.buildExchange(request, response);
		WebContext context = new WebContext(exchange, request.getLocale());

		String path = request.getPathInfo();

		if (path == null || path.equals("/") || path.isEmpty()) {
			templateEngine.process("indexLogistica", context, response.getWriter());
			return;
		}

		if ("/mercancias".equals(path)) {
			try {
				Repositorio_Mercancias repo = new Repositorio_Mercancias();
				List<Mercancia> lista = repo.listarMercancias();

				context.setVariable("mercancias", lista);

				templateEngine.process("mercancias", context, response.getWriter());
				return;

			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(500);
				return;
			}
		}

		if ("/entradamercancia".equals(path)) {
			try {
				Repositorio_Mercancias repo = new Repositorio_Mercancias();
				List<Mercancia> lista = repo.listarMercancias();

				context.setVariable("mercancias", lista);

				if ("1".equals(request.getParameter("ok"))) {
					context.setVariable("mensaje", "Entrada realizada");
				}

				templateEngine.process("entradaMercancia", context, response.getWriter());
				return;

			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(500);
				return;
			}
		}

		if ("/mercancias/entrada".equals(path) && request.getParameter("id") != null) {
			try {
				int id = Integer.parseInt(request.getParameter("id"));
				int cantidad = Integer.parseInt(request.getParameter("cantidad"));

				Repositorio_Mercancias repo = new Repositorio_Mercancias();
				repo.registrarEntradaMercancia(id, cantidad);

				response.sendRedirect(
						request.getContextPath() + "/logistica/entradamercancia?ok=1"
				);
				return;

			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(500);
				return;
			}
		}

		if ("/albaranes".equals(path)) {
			templateEngine.process("albaranes", context, response.getWriter());
			return;
		}

		if ("/pedidos".equals(path)) {
			templateEngine.process("pedidos", context, response.getWriter());
			return;
		}

		if ("/proveedores".equals(path)) {
			templateEngine.process("proveedores", context, response.getWriter());
			return;
		}

		response.sendError(404);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		try {

			String idParam = request.getParameter("id");
			if (idParam == null) idParam = request.getParameter("idMercancia");

			int id = Integer.parseInt(idParam);
			int cantidad = Integer.parseInt(request.getParameter("cantidad"));

			Repositorio_Mercancias repo = new Repositorio_Mercancias();
			repo.registrarEntradaMercancia(id, cantidad);

			response.sendRedirect(
					request.getContextPath() + "/logistica/entradamercancia?ok=1"
			);

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().println("Error: " + e.getMessage());
		}
	}
}