package dam.primero.servlet.institucionales;

import dam.primero.modelos.institucionales.modelo.*;
import dam.primero.repositorio.institucionales.*;

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

public class RelacionesServlet extends HttpServlet {
	private static final long serialVersionUID = 2051990309999713971L;
	public static final String TEXT_HTML_CHARSET_UTF_8 = "text/html;charset=UTF-8";
	public static final String TEMPLATES = "/WEB-INF/templates/institucionales/";
	public static final String SUFFIX = ".html";
	private TemplateEngine templateEngine;
	private JavaxServletWebApplication application;

	// Declaración de tus 8 repositorios del Módulo 4
	private HistorialOportunidadRepo historialOportunidadRepo;
	private ColaboracionRepo colaboracionRepo;
	private RecintoRepo recintoRepo;
	private EmpresaRepo empresaRepo;
	private CentroEducativoRepo centroEducativoRepo;
	private AyuntamientoRepo ayuntamientoRepo;
	private AsociacionRepo asociacionRepo;
	private AdministracionRepo administracionRepo;

	@Override
	public void init() throws ServletException {
		System.out.println("En el init de RelacionesServlet");
		ServletContext servletContext = getServletContext();
		application = JavaxServletWebApplication.buildApplication(servletContext);
		WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
		templateResolver.setPrefix(TEMPLATES);
		templateResolver.setSuffix(SUFFIX);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		// Inicialización de tus repositorios. Cada uno cargará su db.properties automáticamente
		this.historialOportunidadRepo = new HistorialOportunidadRepo();
		this.colaboracionRepo = new ColaboracionRepo();
		this.recintoRepo = new RecintoRepo();
		this.empresaRepo = new EmpresaRepo();
		this.centroEducativoRepo = new CentroEducativoRepo();
		this.ayuntamientoRepo = new AyuntamientoRepo();
		this.asociacionRepo = new AsociacionRepo();
		this.administracionRepo = new AdministracionRepo();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("En el doGet RelacionesServlet");
		response.setContentType(TEXT_HTML_CHARSET_UTF_8);

		IServletWebExchange webExchange = application.buildExchange(request, response);
		WebContext context = new WebContext(webExchange, request.getLocale());

		String servletPath = (request.getServletPath() != null) ? request.getServletPath().trim() : "";
		String pathInfo = request.getPathInfo();
		String path = (pathInfo != null) ? pathInfo.trim() : "";

		System.out.println("doGet servletPath: " + servletPath);
		System.out.println("doGet pathInfo:    " + pathInfo);

		if ("/".equals(servletPath) && path.isEmpty()) {
			templateEngine.process("index", context, response.getWriter());
		}
		else if ("/relaciones".equals(servletPath)) {

			if (path.isEmpty() || path.equals("/")) {
				templateEngine.process("indexRelaciones", context, response.getWriter());
			}
			else {
				String[] partes = path.substring(1).split("/");
				String accion = partes[0];
				String subaccion = partes.length > 1 ? partes[1] : null;

				System.out.println("doGet accion:    " + accion);
				System.out.println("doGet subaccion: " + subaccion);

				switch (accion.toLowerCase()) {
					case "organizacion":
						// Al entrar a organización, recuperamos los 5 subtipos de golpe para poder mostrarlos en la vista
						List<Empresa> empresas = empresaRepo.listarEmpresas();
						List<CentroEducativo> centros = centroEducativoRepo.listarCentros();
						List<Ayuntamiento> ayuntamientos = ayuntamientoRepo.listarAyuntamientos();
						List<Asociacion> asociaciones = asociacionRepo.listarAsociaciones();
						List<Administracion> administraciones = administracionRepo.listarAdministraciones();

						context.setVariable("empresas", empresas);
						context.setVariable("centros", centros);
						context.setVariable("ayuntamientos", ayuntamientos);
						context.setVariable("asociaciones", asociaciones);
						context.setVariable("administraciones", administraciones);

						templateEngine.process("organizacion_Relaciones", context, response.getWriter());
						break;

					case "colaboracion":
						List<Colaboracion> colaboraciones = colaboracionRepo.listarColaboraciones();
						context.setVariable("colaboraciones", colaboraciones);
						templateEngine.process("colaboracion_Relaciones", context, response.getWriter());
						break;

					case "historial":
						// El "Súper Repositorio" recupera el historial unificado y las oportunidades juntas
						List<HistorialOportunidad> historiales = historialOportunidadRepo.listarHistorialOportunidades();
						context.setVariable("historiales", historiales);
						templateEngine.process("historialOportunidad_Relaciones", context, response.getWriter());
						break;

					case "recinto":
						List<Recinto> recintos = recintoRepo.listarRecintos();
						context.setVariable("recintos", recintos);
						templateEngine.process("recinto_Relaciones", context, response.getWriter());
						break;

					case "actividad":
						templateEngine.process("actividad_Relaciones", context, response.getWriter());
						break;
					case "oportunidad":
						templateEngine.process("oportunidad_Relaciones", context, response.getWriter());
						break;
					case "patrocinio":
						templateEngine.process("patrocinio_Relaciones", context, response.getWriter());
						break;
					default:
						response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no reconocida: " + accion);
				}
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}