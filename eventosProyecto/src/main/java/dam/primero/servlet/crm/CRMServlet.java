package dam.primero.servlet.crm;


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
import java.util.Set;

public class CRMServlet extends HttpServlet {
	private static final long serialVersionUID = 2051990309999713971L;
	public static final String TEXT_HTML_CHARSET_UTF_8 = "text/html;charset=UTF-8";
	public static final String TEMPLATES = "/WEB-INF/templates/crm/";
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

		System.out.println("En el doGet CRMServlet");
		response.setContentType(TEXT_HTML_CHARSET_UTF_8);

		IServletWebExchange webExchange = application.buildExchange(request, response);
		WebContext context = new WebContext(webExchange, request.getLocale());

		// getServletPath() → "/" o "/crm"
		// getPathInfo()    → lo que hay DESPUÉS del patrón mapeado
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

		// ── Rutas bajo /crm/*: servletPath="/crm" ─────────────────────────────
		else if ("/crm".equals(servletPath)) {

			// GET /eventosProyectos/crm/   →  pathInfo = null o "/"
			if (path.isEmpty() || path.equals("/")) {
				templateEngine.process("indexCRM", context, response.getWriter());

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
			switch (accion.toLowerCase()) {
				case "clientes":
					// templateEngine.process("clientes", context, response.getWriter());
					break;
				case "eventos":
					// templateEngine.process("eventos", context, response.getWriter());
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
