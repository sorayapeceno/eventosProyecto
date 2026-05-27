package dam.primero.servlet.logistica;

import dam.primero.modelos.logistica.modelo.Albaran;
import dam.primero.modelos.logistica.modelo.Mercancia;
import dam.primero.modelos.logistica.modelo.Pedido;
import dam.primero.modelos.logistica.modelo.Proveedor;
import dam.primero.repositorio.logistica.Repositorio_Albaranes;
import dam.primero.repositorio.logistica.Repositorio_Mercancias;
import dam.primero.repositorio.logistica.Repositorio_Pedidos;
import dam.primero.repositorio.logistica.Repositorio_Proveedores;

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

				// Comprobamos si el doPost nos ha dejado un error guardado en la sesión
				HttpSession sesion = request.getSession();
				String errorSesion = (String) sesion.getAttribute("errorSesion");

				if (errorSesion != null) {
					context.setVariable("error", errorSesion);
					sesion.removeAttribute("errorSesion"); // Se borra inmediatamente para que no repita al refrescar
				}

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

		if ("/albaranes".equals(path)) {
			try {
				Repositorio_Albaranes repo = new Repositorio_Albaranes();
				List<Albaran> lista = repo.listarAlbaranes();

				if (lista.isEmpty()) {
					context.setVariable("mensaje", "No existen albaranes registrados");
				} else {
					context.setVariable("albaranes", lista);
				}

				templateEngine.process("albaranes", context, response.getWriter());
				return;

			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(500);
				return;
			}
		}

		if ("/pedidos".equals(path)) {
			String idParam = request.getParameter("idPedido");

			if (idParam != null && !idParam.trim().isEmpty()) {
				try {
					int idPedido = Integer.parseInt(idParam);
					Repositorio_Pedidos repo = new Repositorio_Pedidos();

					Pedido pedido = repo.obtenerPedidoConLineas(idPedido);

					context.setVariable("pedido", pedido);
					context.setVariable("totalPedido", pedido.calcularTotal());

				} catch (NumberFormatException e) {
					context.setVariable("error", "El ID introducido debe ser un número entero válido.");
				} catch (dam.primero.exception.MyException e) {
					context.setVariable("error", e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					context.setVariable("error", "Ocurrió un error inesperado en el servidor.");
				}
			}

			templateEngine.process("pedidos", context, response.getWriter());
			return;
		}

		if ("/proveedores".equals(path)) {
			try {
				Repositorio_Proveedores repo = new Repositorio_Proveedores();
				List<Proveedor> lista = repo.listarProveedores();

				if (lista.isEmpty()) {
					context.setVariable("mensaje", "No hay proveedores registrados en el sistema.");
				} else {
					context.setVariable("proveedores", lista);
				}

				templateEngine.process("proveedores", context, response.getWriter());
				return;

			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(500);
				return;
			}
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

		} catch (dam.primero.exception.MyException e) {
			// Captura controlada: guardamos el texto en la sesión y redirigimos al formulario de vuelta
			HttpSession sesion = request.getSession();
			sesion.setAttribute("errorSesion", e.getMessage());

			response.sendRedirect(
					request.getContextPath() + "/logistica/entradamercancia"
			);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(500);
		}
	}
}