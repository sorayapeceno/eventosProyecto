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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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

		System.out.println("En el doGet ParticipantesServlet");
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
		} else if ("/participantes".equals(servletPath)) {

			if (path.isEmpty() || path.equals("/")) {
				templateEngine.process("indexParticipantes", context, response.getWriter());
			} else {
				String[] partes = path.substring(1).split("/");
				String accion = partes[0];

				System.out.println("doGet accion: " + accion);

				switch (accion) {

					case "listaParticipantes":
						templateEngine.process("indexParticipantes", context, response.getWriter());
						break;

					case "Crear_Evento":
						context.setVariable("estados", Set.of(Estado.values()));
						context.setVariable("modalidades", Set.of(Modalidad.values()));
						templateEngine.process("Crear_Evento", context, response.getWriter());
						break;

					case "Crear_Ponencia":
						context.setVariable("niveles", Set.of(Nivel.values()));
						context.setVariable("tipos", Set.of(Tipo.values()));
						context.setVariable("formatos", Set.of(Formato.values()));
						templateEngine.process("Crear_Ponencia", context, response.getWriter());
						break;

					case "Registrar_Ponente":
						context.setVariable("niveles", Set.of(NivelImparticion.values()));
						templateEngine.process("Registrar_Ponente", context, response.getWriter());
						break;

					case "Listado_Eventos":
						RepoEventos repoEventos = new RepoEventos();
						List<Evento> eventos = repoEventos.listarEvento();
						context.setVariable("eventos", eventos);
						templateEngine.process("Listado_Eventos", context, response.getWriter());
						break;

					case "Listado_Ponencias":
						PonenciaRepo repPonencias = new PonenciaRepo();
						context.setVariable("ponencias", repPonencias.listarPonencias());
						context.setVariable("niveles", Set.of(Nivel.values()));
						context.setVariable("tipos", Set.of(Tipo.values()));
						context.setVariable("formatos", Set.of(Formato.values()));
						templateEngine.process("Listado_Ponencias", context, response.getWriter());
						break;

					case "Listado_Ponentes":
						PonenteRepo repPonentes = new PonenteRepo();
						context.setVariable("ponentes", repPonentes.listarPonente());
						context.setVariable("niveles", Set.of(NivelImparticion.values()));
						templateEngine.process("Listado_Ponentes", context, response.getWriter());
						break;

					case "Detalle_Evento":
						String idParamDetalle = request.getParameter("id");
						if (idParamDetalle != null && !idParamDetalle.isEmpty()) {
							RepoEventos re = new RepoEventos();
							Evento evento = re.mostrarEvento(Integer.parseInt(idParamDetalle));
							context.setVariable("evento", evento);
							templateEngine.process("Detalle_Evento", context, response.getWriter());
						}
						break;

					case "Modificar_Evento":
						String idParamModificar = request.getParameter("id_Evento");
						if (idParamModificar != null && !idParamModificar.isEmpty()) {
							RepoEventos repoMod = new RepoEventos();
							Evento evento = repoMod.mostrarEvento(Integer.parseInt(idParamModificar));
							context.setVariable("evento", evento);
							context.setVariable("estados", Set.of(Estado.values()));
							context.setVariable("modalidades", Set.of(Modalidad.values()));
							templateEngine.process("Modificar_Evento", context, response.getWriter());
						} else {
							response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro id_Evento");
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

		System.out.println("En doPost - Path: " + path);

		switch (path) {

			case "/Crear_Evento":
				try {
					Evento evento = new Evento();
					evento.setNombre(request.getParameter("nombre"));
					evento.setDescripcion(request.getParameter("descripcion"));
					evento.setDireccion(request.getParameter("direccion"));
					evento.setCiudad(request.getParameter("ciudad"));
					evento.setLugar(request.getParameter("lugar"));
					evento.setCapacidad(Integer.parseInt(request.getParameter("capacidad")));
					evento.setFechaInicio(LocalDate.parse(request.getParameter("fechaInicio")));
					evento.setFechaFin(LocalDate.parse(request.getParameter("fechaFin")));
					evento.setEstado(Estado.valueOf(request.getParameter("estado")));
					evento.setModalidad(Modalidad.valueOf(request.getParameter("modalidad")));

					new RepoEventos().crearEvento(evento);
					response.sendRedirect(request.getContextPath() + "/participantes/Listado_Eventos");
				} catch (Exception e) {
					e.printStackTrace();
					response.getWriter().println("Error al crear evento: " + e.getMessage());
				}
				break;

			case "/Modificar_Evento":
				try {
					Evento evento = new Evento();
					evento.setId_Evento(Integer.parseInt(request.getParameter("idEvento")));
					evento.setNombre(request.getParameter("nombre"));
					evento.setDescripcion(request.getParameter("descripcion"));
					evento.setDireccion(request.getParameter("direccion"));
					evento.setCiudad(request.getParameter("ciudad"));
					evento.setLugar(request.getParameter("lugar"));
					evento.setCapacidad(Integer.parseInt(request.getParameter("capacidad")));
					evento.setFechaInicio(LocalDate.parse(request.getParameter("fechaInicio")));
					evento.setFechaFin(LocalDate.parse(request.getParameter("fechaFin")));
					evento.setEstado(Estado.valueOf(request.getParameter("estado")));
					evento.setModalidad(Modalidad.valueOf(request.getParameter("modalidad")));

					new RepoEventos().modificarEvento(evento);
					response.sendRedirect(request.getContextPath() + "/participantes/Listado_Eventos");
				} catch (Exception e) {
					e.printStackTrace();
					response.getWriter().println("Error al modificar evento: " + e.getMessage());
				}
				break;

			// FIX: POST de Crear_Ponencia implementado
			case "/Crear_Ponencia":
				try {
					Ponencia ponencia = new Ponencia();
					ponencia.setId_Evento(Integer.parseInt(request.getParameter("idEvento")));
					ponencia.setTitulo(request.getParameter("titulo"));
					ponencia.setDuracion(Integer.parseInt(request.getParameter("duracion")));
					ponencia.setFecha(LocalDate.parse(request.getParameter("fecha")));
					ponencia.setHora(LocalDate.parse(request.getParameter("fecha")).atTime(LocalTime.parse(request.getParameter("hora"))));
					ponencia.setUbicacion(request.getParameter("ubicacion"));
					ponencia.setNivel(Nivel.valueOf(request.getParameter("nivel")));
					ponencia.setTipo(Tipo.valueOf(request.getParameter("tipo")));
					ponencia.setFormato(Formato.valueOf(request.getParameter("formato")));

					// Tematica: construimos el objeto con el ID que manda el formulario
					int idTematica = Integer.parseInt(request.getParameter("idTematica"));
					ponencia.setTematica(new Tematica(idTematica, ""));

					new PonenciaRepo().crearPonencia(ponencia);
					response.sendRedirect(request.getContextPath() + "/participantes/Listado_Ponencias");
				} catch (Exception e) {
					e.printStackTrace();
					response.getWriter().println("Error al crear ponencia: " + e.getMessage());
				}
				break;

			// FIX: POST de Registrar_Ponente implementado
			case "/Registrar_Ponente":
				try {
					// El modelo Ponente requiere id_Persona (FK a Persona en la BD)
					// El formulario recoge nombre, apellidos, correo, telefono → esos van a la tabla Persona
					// Por ahora creamos el Ponente con los datos disponibles que acepta el repo
					String especialidad = request.getParameter("especialidad");
					String cv = request.getParameter("cv");
					String bio = request.getParameter("nombre") + " " + request.getParameter("apellidos");
					NivelImparticion nivel = NivelImparticion.valueOf(request.getParameter("nivelImparticion"));

					// id_Persona = 0 hasta que se integre con la tabla Persona
					Ponente ponente = new Ponente(0, bio, especialidad, cv, nivel);
					new PonenteRepo().crearPonente(ponente);
					response.sendRedirect(request.getContextPath() + "/participantes/Listado_Ponentes");
				} catch (Exception e) {
					e.printStackTrace();
					response.getWriter().println("Error al registrar ponente: " + e.getMessage());
				}
				break;

			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "POST no reconocido: " + path);
				break;
		}
	}
}