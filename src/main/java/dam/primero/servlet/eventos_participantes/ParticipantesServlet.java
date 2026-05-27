package dam.primero.servlet.eventos_participantes;

import dam.primero.modelos.eventos_participantes.Modelo.*;

import dam.primero.repositorio.eventos_participantes.*;
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
import java.util.ArrayList;
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
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("En el doGet PartipantesServlet");
		response.setContentType(TEXT_HTML_CHARSET_UTF_8);

		IServletWebExchange webExchange = application.buildExchange(request, response);
		WebContext context = new WebContext(webExchange, request.getLocale());

		String servletPath = (request.getServletPath()!= null) ? request.getServletPath().trim() : "";
		String pathInfo = request.getPathInfo();
		String path = (pathInfo != null) ? pathInfo.trim() : "";

		System.out.println("doGet servletPath: " + servletPath);
		System.out.println("doGet pathInfo:    " + pathInfo);

		if ("/".equals(servletPath) && path.isEmpty()) {
			templateEngine.process("index", context, response.getWriter());
		}
		else if ("/participantes".equals(servletPath)) {

			if (path.isEmpty() || path.equals("/")) {
				templateEngine.process("indexParticipantes", context, response.getWriter());
			}
			else {
				String[] partes = path.substring(1).split("/");
				String accion = partes[0];
				String subaccion = partes.length > 1 ? partes[1] : null;

				System.out.println("doGet accion:    " + accion);
				System.out.println("doGet subaccion: " + subaccion);

				switch (accion) {
					case "listaParticipantes":
						templateEngine.process("indexParticipantes", context, response.getWriter());
						break;
					case "eventos":
						break;
					case "Crear_Evento":
						EstadoRepo repo = new EstadoRepo();
						ModalidadRepo r = new ModalidadRepo();
						List <Estado> estados = repo.listarEstados();
						List<Modalidad> modalidades = r.listarModalidad();
						context.setVariable("estados", estados);
						context.setVariable("modalidades",modalidades);
						templateEngine.process("Crear_Evento", context, response.getWriter());
						break;

					case "Crear_Ponencia":
						PonenciaRepo repo1 = new PonenciaRepo();
						templateEngine.process("Crear_Ponencia",context,response.getWriter());
						break;
					case "Listado_Eventos":
						RepoEventos repoEventos = new RepoEventos();
						List<Evento> eventos = new ArrayList<Evento>();
						context.setVariable("eventos", eventos);
						templateEngine.process("Listado_Eventos", context, response.getWriter());
						break;
					case "Listado_Ponencias":
						try {
							PonenciaRepo rep = new PonenciaRepo();
							List<Ponencia> ponencias = rep.listarPonencias();
							context.setVariable("ponencias", ponencias);
							templateEngine.process("Listado_Ponencias", context, response.getWriter());
						} catch (Exception e) {
							response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en Base de Datos: " + e.getMessage());
						}
						break;
					case "Listado_Ponentes":
						PonenteRepo repPonentes = new PonenteRepo();
						List<Ponente> ponentes = new ArrayList<Ponente>();
						context.setVariable("ponentes", ponentes);
						templateEngine.process("Listado_Ponentes", context, response.getWriter());
						break;

					case "Detalle_Evento":
						RepoEventos rep = new RepoEventos();
						String idParam = request.getParameter("id");

						if (idParam != null && !idParam.isEmpty()) {
							int idEvento = Integer.parseInt(idParam);
							Evento evento = rep.mostrarEvento(idEvento);
							context.setVariable("evento", evento);
							templateEngine.process("Detalle_Evento", context, response.getWriter());
						}
						break;

					default:
						response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no reconocida: " + accion);
				}
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		String path = (pathInfo != null) ? pathInfo.trim() : "";

		System.out.println("En doPost");
		System.out.println("Path: " + path);

		if (path.equals("/Crear_Evento")) {
			try {
				Evento evento = new Evento();
				evento.setNombre(request.getParameter("nombre"));
				evento.setDescripcion(request.getParameter("descripcion"));
				evento.setDireccion(request.getParameter("direccion"));
				evento.setCiudad(request.getParameter("ciudad"));
				evento.setLugar(request.getParameter("lugar"));
				evento.setCapacidad(Integer.parseInt(request.getParameter("capacidad")));
				evento.setFechaInicio(java.time.LocalDate.parse(request.getParameter("fechaInicio")));
				evento.setFechaFin(java.time.LocalDate.parse(request.getParameter("fechaFin")));
				evento.setEstado(Estado.valueOf(request.getParameter("estado")));
				evento.setModalidad(dam.primero.modelos.eventos_participantes.Modelo.Modalidad.valueOf(request.getParameter("modalidad")));

				RepoEventos repo = new RepoEventos();
				repo.crearEvento(evento);

				response.sendRedirect(request.getContextPath() + "/participantes/Listado_Eventos");
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().println("Error al crear evento: " + e.getMessage());
			}
		}
	}
}