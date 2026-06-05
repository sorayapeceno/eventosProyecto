package dam.primero.servlet.logistica;

import dam.primero.exception.MyException;
import dam.primero.modelos.logistica.modelo.*;
import dam.primero.repositorio.logistica.*;

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

	private TemplateEngine engine;
	private JavaxServletWebApplication app;

	@Override
	public void init() {
		ServletContext ctx = getServletContext();

		app = JavaxServletWebApplication.buildApplication(ctx);

		WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(app);
		resolver.setPrefix("/WEB-INF/templates/logistica/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);

		engine = new TemplateEngine();
		engine.setTemplateResolver(resolver);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html;charset=UTF-8");

		IServletWebExchange exchange = app.buildExchange(request, response);
		WebContext context = new WebContext(exchange, request.getLocale());

		String path = request.getPathInfo();

		if (path == null || path.equals("/") || path.isEmpty()) {
			path = "/";
		}

		try {

			switch (path) {

				case "/" -> engine.process("indexLogistica", context, response.getWriter());

				case "/mercancias" -> {
					// saco todas las mercancías y las mando a la vista
					Repositorio_Mercancias repo = new Repositorio_Mercancias();
					List<Mercancia> lista = repo.listarMercancias();
					context.setVariable("mercancias", lista);
					engine.process("mercancias", context, response.getWriter());
				}

				case "/entradamercancia" -> {
					// si funciona:
					if ("1".equals(request.getParameter("ok"))) {
						context.setVariable("mensaje", "Entrada registrada correctamente");
					}

					engine.process("entradaMercancia", context, response.getWriter());
				}

				case "/albaranes" -> {
					// saco los albaranes y los muestro
					Repositorio_Albaranes repo = new Repositorio_Albaranes();
					List<Albaran> lista = repo.listarAlbaranes();

					if (lista.isEmpty()) {
						context.setVariable("mensaje", "No hay albaranes");
					} else {
						context.setVariable("albaranes", lista);
					}

					engine.process("albaranes", context, response.getWriter());
				}

				case "/pedidos" -> {
					// busco un pedido por id que me introducen como parametro
					String idParam = request.getParameter("idPedido");

					if (idParam != null && !idParam.isEmpty()) {

						try {
							int id = Integer.parseInt(idParam);

							Repositorio_Pedidos repo = new Repositorio_Pedidos();
							Pedido pedido = repo.obtenerPedidoConLineas(id);

							context.setVariable("pedido", pedido);
							context.setVariable("totalPedido", pedido.calcularTotal());

						} catch (NumberFormatException e) {
							context.setVariable("error", "El ID debe ser numérico");
						} catch (MyException e) {
							context.setVariable("error", e.getMessage());
						}
					}

					engine.process("pedidos", context, response.getWriter());
				}

				case "/proveedores" -> {
					// muestro todos los proveedores
					Repositorio_Proveedores repo = new Repositorio_Proveedores();
					List<Proveedor> lista = repo.listarProveedores();

					if (lista.isEmpty()) {
						context.setVariable("mensaje", "No hay proveedores");
					} else {
						context.setVariable("proveedores", lista);
					}

					engine.process("proveedores", context, response.getWriter());
				}

				case "/exportarmercancias" -> {

					try {
						// genero el CSV con mercancías con stock mayor a 40
						ExportadorMercanciasCsv exportador = new ExportadorMercanciasCsv();
						exportador.exportarMercanciasStockMayor40();

						context.setVariable("mensaje", "CSV generado correctamente");

					} catch (Exception e) {
						e.printStackTrace();
						context.setVariable("error", "Error al generar el CSV");
					}

					// vuelvo a la vista de mercancías
					engine.process("mercancias", context, response.getWriter());
				}

				default -> response.sendError(404);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(500);
		}
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
			repo.entradaMercancia(id, cantidad);

			response.sendRedirect(
					request.getContextPath() + "/logistica/entradamercancia?ok=1"
			);

		} catch (MyException e) {
			// vuelve al formulario si ocurre un error
			response.sendRedirect(
					request.getContextPath() + "/logistica/entradamercancia"
			);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(500);
		}
	}
}