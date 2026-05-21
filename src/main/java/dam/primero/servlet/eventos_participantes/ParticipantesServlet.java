package dam.primero.servlet.eventos_participantes;


import dam.primero.modelos.eventos_participantes.Modelo.Estado;
import dam.primero.modelos.eventos_participantes.Modelo.Ponencia;
import dam.primero.repositorio.eventos_participantes.EstadoRepo;
import dam.primero.repositorio.eventos_participantes.PonenciaRepo;
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

public class ParticipantesServlet extends HttpServlet {
	private static final long serialVersionUID = 2051990309999713971L;
	public static final String TEXT_HTML_CHARSET_UTF_8 = "text/html;charset=UTF-8";
	public static final String TEMPLATES = "/WEB-INF/templates/eventos_participantes/";
	public static final String SUFFIX = ".html";
	private TemplateEngine templateEngine;
	private JavaxServletWebApplication application;



	@Override
	public void init() throws ServletException {
		System.out.println("En el init");
		ServletContext servletContext = getServletContext();
		application = JavaxServletWebApplication.buildApplication(servletContext);
		WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
		templateResolver.setPrefix(TEMPLATES);
		templateResolver.setSuffix(SUFFIX);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		//Inicializa repositorios
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("En el doGet PartipantesServlet");
		response.setContentType(TEXT_HTML_CHARSET_UTF_8);

		IServletWebExchange webExchange = application.buildExchange(request, response);
		WebContext context = new WebContext(webExchange, request.getLocale());


		String servletPath = (request.getServletPath()!= null) ? request.getServletPath().trim() : "";
		String pathInfo = request.getPathInfo();          // puede ser null
		String path = (pathInfo != null) ? pathInfo.trim() : "";

		System.out.println("doGet servletPath: " + servletPath);
		System.out.println("doGet pathInfo:    " + pathInfo);

		// ── Ruta raíz: GET /eventosProyectos/ ─────────────────────────────────
		// Mapping "/": servletPath="/" y pathInfo=null
		if ("/".equals(servletPath) && path.isEmpty()) {
			templateEngine.process("index", context, response.getWriter());

		}

		else if ("/participantes".equals(servletPath)) {

			if (path.isEmpty() || path.equals("/")) {
				templateEngine.process("indexParticipantes", context, response.getWriter());

			}
			else{

			// Descomponemos el pathInfo para obtener acción y subacción
			// path ejemplo: "/clientes"  →  partes = ["clientes"]
			// path ejemplo: "/clientes/editar" →  partes = ["clientes","editar"]
			String[] partes = path.substring(1).split("/");
			String accion = partes[0];
			String subaccion = partes.length > 1 ? partes[1] : null;

			System.out.println("doGet accion:    " + accion);
			System.out.println("doGet subaccion: " + subaccion);

			// Aquí tu lógica de negocio por acción
			switch (accion) {
				case "listaParticipantes":
					 templateEngine.process("indexParticipantes", context, response.getWriter());
					break;
				case "eventos":
					// templateEngine.process("eventos", context, response.getWriter());
					break;
				case "Crear_Eventos":
					EstadoRepo repo = new EstadoRepo();
					List<Estado> estados = repo.listarEstados();
					context.setVariable("estados", estados);

					templateEngine.process("Crear_Eventos", context, response.getWriter());

					break;

				case "Crear_Ponencia":
					PonenciaRepo rep = new PonenciaRepo();
					List<Ponencia> ponencias = rep.listarPonencias();
					context.setVariable("ponencias",ponencias); /*Sirve para pasar al HTML una lista llamada ponencias*/

					templateEngine.process("Crear_Ponencia",context,response.getWriter()); /*Dirige a Crear_Ponencias.html*/

					break;

				default:
					response.sendError(HttpServletResponse.SC_NOT_FOUND,
							"Acción no reconocida: " + accion);
			}}

		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
