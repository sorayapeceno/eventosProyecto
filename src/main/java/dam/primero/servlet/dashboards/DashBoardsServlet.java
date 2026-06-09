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

/**
 * Servlet controlador del módulo Dashboard.
 * <p>
 * Gestiona todas las peticiones HTTP GET dirigidas a {@code /dashboards/*}.
 * Para cada módulo del proyecto (eventos, ponencias, ponentes, ventas, CRM,
 * relaciones, logística) procesa dos rutas:
 * </p>
 * <ul>
 *   <li>{@code /dashboards/modulo} → muestra el dashboard principal con KPIs y gráficas</li>
 *   <li>{@code /dashboards/modulo/detalle} → muestra la página de detalle con tablas completas</li>
 * </ul>
 * <p>
 * Utiliza Thymeleaf como motor de plantillas para renderizar los HTML,
 * pasando los datos obtenidos del {@link RepoDashboards} mediante el contexto de Thymeleaf.
 * </p>
 *
 * @author Elena Pablo Benítez
 * @version 1.0
 * @see RepoDashboards
 */
public class DashBoardsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/** Tipo de contenido de las respuestas HTTP. */
	public static final String TEXT_HTML_CHARSET_UTF_8 = "text/html;charset=UTF-8";

	/** Ruta base donde Thymeleaf busca las plantillas HTML del módulo dashboard. */
	public static final String TEMPLATES = "/WEB-INF/templates/dashboards/";

	/** Extensión de los archivos de plantilla. */
	public static final String SUFFIX = ".html";

	/** Motor de plantillas Thymeleaf para renderizar los HTML. */
	private TemplateEngine templateEngine;

	/** Aplicación web de Thymeleaf para construir el contexto de las peticiones. */
	private JavaxServletWebApplication application;

	/**
	 * Inicializa el servlet configurando el motor de plantillas Thymeleaf.
	 * <p>
	 * Se ejecuta una sola vez cuando Tomcat arranca el servlet.
	 * Configura el resolver de plantillas apuntando a la carpeta
	 * {@code /WEB-INF/templates/dashboards/} con extensión {@code .html}.
	 * </p>
	 *
	 * @throws ServletException si ocurre un error durante la inicialización
	 */
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

	/**
	 * Maneja las peticiones HTTP GET al módulo dashboard.
	 * <p>
	 * Lee el pathInfo de la URL para determinar qué módulo y vista mostrar.
	 * Obtiene los datos del {@link RepoDashboards} y los pasa al contexto
	 * de Thymeleaf para que la plantilla HTML los renderice.
	 * </p>
	 * <p>
	 * Rutas soportadas:
	 * </p>
	 * <ul>
	 *   <li>{@code /dashboards/} → índice con todas las tarjetas de módulos</li>
	 *   <li>{@code /dashboards/eventos} → dashboard de eventos</li>
	 *   <li>{@code /dashboards/eventos/detalle} → detalle de eventos</li>
	 *   <li>{@code /dashboards/ponencias} → dashboard de ponencias</li>
	 *   <li>{@code /dashboards/ponencias/detalle} → detalle de ponencias</li>
	 *   <li>{@code /dashboards/ponentes} → dashboard de ponentes</li>
	 *   <li>{@code /dashboards/ponentes/detalle} → detalle de ponentes</li>
	 *   <li>{@code /dashboards/ventas} → dashboard de ventas</li>
	 *   <li>{@code /dashboards/ventas/detalle} → detalle de ventas</li>
	 *   <li>{@code /dashboards/crm} → dashboard de CRM</li>
	 *   <li>{@code /dashboards/crm/detalle} → detalle de CRM</li>
	 *   <li>{@code /dashboards/relaciones} → dashboard de relaciones institucionales</li>
	 *   <li>{@code /dashboards/relaciones/detalle} → detalle de relaciones</li>
	 *   <li>{@code /dashboards/logistica} → dashboard de logística</li>
	 *   <li>{@code /dashboards/logistica/detalle} → detalle de logística</li>
	 * </ul>
	 *
	 * @param request  petición HTTP con la URL y parámetros del navegador
	 * @param response respuesta HTTP donde se escribe el HTML generado
	 * @throws ServletException si ocurre un error en el procesamiento del servlet
	 * @throws IOException      si ocurre un error de entrada/salida al escribir la respuesta
	 */
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
					context.setVariable("totalPersonas", repo.contarPersonas());
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