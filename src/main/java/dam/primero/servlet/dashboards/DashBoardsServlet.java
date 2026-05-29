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
		WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
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
			case "eventos":
				if ("detalle".equals(detalle)) {
					context.setVariable("eventos", repo.detalleEventos());
					context.setVariable("eventosPorEstado", repo.eventosPorEstado());
					context.setVariable("eventosPorModalidad", repo.eventosPorModalidad());
					templateEngine.process("detalleEventos", context, response.getWriter());
				} else {
					context.setVariable("totalEventos", repo.contarEventos());
					context.setVariable("eventosPorEstado", repo.eventosPorEstado());
					context.setVariable("eventosPorModalidad", repo.eventosPorModalidad());
					context.setVariable("eventosPorCiudad", repo.eventosPorCiudad());
					templateEngine.process("dashboardEventos", context, response.getWriter());
				}
				break;
			case "ponencias":
				if ("detalle".equals(detalle)) {
					context.setVariable("ponencias", repo.detallePonencias());
					context.setVariable("ponenciasPorTipo", repo.ponenciasPorTipo());
					context.setVariable("ponenciasPorNivel", repo.ponenciasPorNivel());
					templateEngine.process("detallePonencias", context, response.getWriter());
				} else {
					context.setVariable("totalPonencias", repo.contarPonencias());
					context.setVariable("ponenciasPorTipo", repo.ponenciasPorTipo());
					context.setVariable("ponenciasPorNivel", repo.ponenciasPorNivel());
					context.setVariable("ponenciasPorFormato", repo.ponenciasPorFormato());
					context.setVariable("ponenciasPorTematica", repo.ponenciasPorTematica());
					templateEngine.process("dashboardPonencias", context, response.getWriter());
				}
				break;
			case "ponentes":
				if ("detalle".equals(detalle)) {
					context.setVariable("ponentes", repo.detallePonentes());
					context.setVariable("ponentesPorEspecialidad", repo.ponentesPorEspecialidad());
					context.setVariable("ponentesPorNivelImparticion", repo.ponentesPorNivelImparticion());
					templateEngine.process("detallePonentes", context, response.getWriter());
				} else {
					context.setVariable("totalPonentes", repo.contarPonentes());
					context.setVariable("ponentesPorEspecialidad", repo.ponentesPorEspecialidad());
					context.setVariable("ponentesPorNivelImparticion", repo.ponentesPorNivelImparticion());
					context.setVariable("topPonentes", repo.topPonentes());
					templateEngine.process("dashboardPonentes", context, response.getWriter());
				}
				break;
			case "ventas":
				if ("detalle".equals(detalle)) {
					context.setVariable("tickets", repo.detalleTickets());
					context.setVariable("productos", repo.detalleProductosVentas());
					context.setVariable("ticketsPorMetodoPago", repo.ticketsPorMetodoPago());
					context.setVariable("stockProductos", repo.stockProductos());
					templateEngine.process("detalleVentas", context, response.getWriter());
				} else {
					context.setVariable("totalAsistentes", repo.contarAsistentes());
					context.setVariable("totalTickets", repo.contarTickets());
					context.setVariable("totalProductos", repo.contarProductosVentas());
					context.setVariable("ticketsPorMetodoPago", repo.ticketsPorMetodoPago());
					context.setVariable("asistentePorNivel", repo.asistentePorNivel());
					context.setVariable("asistentePorTematica", repo.asistentePorTematica());
					context.setVariable("stockProductos", repo.stockProductos());
					context.setVariable("entradasPorEstado", repo.entradasPorEstado());
					templateEngine.process("dashboardVentas", context, response.getWriter());
				}
				break;
			case "crm":
				if ("detalle".equals(detalle)) {
					context.setVariable("paginas", repo.detallePaginasWeb());
					context.setVariable("paginasPorTipo", repo.paginasPorTipo());
					templateEngine.process("detalleCRM", context, response.getWriter());
				} else {
					context.setVariable("paginasPorTipo", repo.paginasPorTipo());
					templateEngine.process("dashboardCRM", context, response.getWriter());
				}
				break;
			case "relaciones":
				if ("detalle".equals(detalle)) {
					context.setVariable("organizaciones", repo.detalleOrganizacionesRel());
					context.setVariable("oportunidades", repo.detalleOportunidadesRel());
					context.setVariable("colaboraciones", repo.detalleColaboraciones());
					context.setVariable("oportunidadesPorEstado", repo.oportunidadesPorEstado());
					context.setVariable("patrociniosPorTipo", repo.patrociniosPorTipo());
					templateEngine.process("detalleRelaciones", context, response.getWriter());
				} else {
					context.setVariable("totalOrganizaciones", repo.contarOrganizacionesRel());
					context.setVariable("totalOportunidades", repo.contarOportunidadesRel());
					context.setVariable("totalRecintos", repo.contarRecintos());
					context.setVariable("totalPatrocinios", repo.contarPatrocinios());
					context.setVariable("oportunidadesPorEstado", repo.oportunidadesPorEstado());
					context.setVariable("patrociniosPorTipo", repo.patrociniosPorTipo());
					context.setVariable("colaboracionesPorTipo", repo.colaboracionesPorTipo());
					context.setVariable("organizacionesPorCiudad", repo.organizacionesPorCiudad());
					context.setVariable("recintoPorCapacidad", repo.recintoPorCapacidad());
					templateEngine.process("dashboardRelaciones", context, response.getWriter());
				}
				break;
			case "logistica":
				if ("detalle".equals(detalle)) {
					context.setVariable("proveedores", repo.detalleProveedores());
					context.setVariable("pedidos", repo.detallePedidos());
					context.setVariable("mercancias", repo.detalleMercancias());
					context.setVariable("pedidosPorEstado", repo.pedidosPorEstado());
					context.setVariable("albAranesPorEstado", repo.albAranesPorEstado());
					templateEngine.process("detalleLogistica", context, response.getWriter());
				} else {
					context.setVariable("totalProveedores", repo.contarProveedores());
					context.setVariable("totalPedidos", repo.contarPedidos());
					context.setVariable("totalMercancias", repo.contarMercancias());
					context.setVariable("totalAlbaranes", repo.contarAlbaranes());
					context.setVariable("proveedoresPorEstado", repo.proveedoresPorEstado());
					context.setVariable("pedidosPorEstado", repo.pedidosPorEstado());
					context.setVariable("mercanciasPorCategoria", repo.mercanciasPorCategoria());
					context.setVariable("stockMercancias", repo.stockMercancias());
					context.setVariable("albAranesPorEstado", repo.albAranesPorEstado());
					context.setVariable("pedidosPorProveedor", repo.pedidosPorProveedor());
					templateEngine.process("dashboardLogistica", context, response.getWriter());
				}
				break;
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Dashboard no reconocido: " + accion);
		}
	}
}