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
import java.time.LocalDateTime;
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

		String servletPath = (request.getServletPath() != null) ? request.getServletPath().trim() : "";
		String pathInfo    = request.getPathInfo();
		String path        = (pathInfo != null) ? pathInfo.trim() : "";

		if ("/".equals(servletPath) && path.isEmpty()) {
			templateEngine.process("index", context, response.getWriter());
			return;
		}

		if (!"/participantes".equals(servletPath)) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if (path.isEmpty() || path.equals("/")) {
			templateEngine.process("indexParticipantes", context, response.getWriter());
			return;
		}

		String accion = path.substring(1).split("/")[0];

		switch (accion) {

			case "listaParticipantes":
				templateEngine.process("indexParticipantes", context, response.getWriter());
				break;

			// ── EVENTOS ────────────────────────────────────────────────
			case "Crear_Evento":
				context.setVariable("estados",    Set.of(Estado.values()));
				context.setVariable("modalidades", Set.of(Modalidad.values()));
				templateEngine.process("Crear_Evento", context, response.getWriter());
				break;

			case "Listado_Eventos": {
				List<Evento> eventos = new RepoEventos().listarEvento();
				context.setVariable("eventos", eventos);
				templateEngine.process("Listado_Eventos", context, response.getWriter());
				break;
			}

			case "Detalle_Evento": {
				String idParam = request.getParameter("id");
				if (idParam != null && !idParam.isEmpty()) {
					Evento evento = new RepoEventos().mostrarEvento(Integer.parseInt(idParam));
					context.setVariable("evento", evento);
					templateEngine.process("Detalle_Evento", context, response.getWriter());
				}
				break;
			}

			case "Modificar_Evento": {
				String idParam = request.getParameter("id_Evento");
				if (idParam == null || idParam.isEmpty()) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta id_Evento");
					break;
				}
				Evento evento = new RepoEventos().mostrarEvento(Integer.parseInt(idParam));
				context.setVariable("evento",     evento);
				context.setVariable("estados",    Set.of(Estado.values()));
				context.setVariable("modalidades", Set.of(Modalidad.values()));
				templateEngine.process("Modificar_Evento", context, response.getWriter());
				break;
			}

			// ── PONENCIAS ──────────────────────────────────────────────
			case "Crear_Ponencia": {
				List<Evento> eventos = new RepoEventos().listarEvento();
				context.setVariable("eventos",   eventos);
				context.setVariable("niveles",   Set.of(Nivel.values()));
				context.setVariable("tipos",     Set.of(Tipo.values()));
				context.setVariable("formatos",  Set.of(Formato.values()));
				context.setVariable("tematicas", new TematicaRepo().listarTematicas());
				templateEngine.process("Crear_Ponencia", context, response.getWriter());
				break;
			}

			case "Listado_Ponencias": {
				context.setVariable("ponencias", new PonenciaRepo().listarPonencias());
				templateEngine.process("Listado_Ponencias", context, response.getWriter());
				break;
			}

			// ── PONENTES ───────────────────────────────────────────────
			case "Registrar_Ponente":
				context.setVariable("niveles", Set.of(NivelImparticion.values()));
				templateEngine.process("Registrar_Ponente", context, response.getWriter());
				break;

			case "Listado_Ponentes":
				context.setVariable("ponentes", new PonenteRepo().listarPonente());
				context.setVariable("niveles",  Set.of(NivelImparticion.values()));
				templateEngine.process("Listado_Ponentes", context, response.getWriter());
				break;

			// ── ASIGNACIONES ───────────────────────────────────────────
			case "Asignacion_Ponencia-Evento": {
				List<Evento> eventos = new RepoEventos().listarEvento();
				context.setVariable("eventos",   eventos);
				context.setVariable("ponencias", new PonenciaRepo().listarPonencias());
				templateEngine.process("Asignacion_Ponencia-Evento", context, response.getWriter());
				break;
			}

			case "Asignacion_Ponente-Evento": {
				List<Evento> eventos = new RepoEventos().listarEvento();
				context.setVariable("eventos",  eventos);
				context.setVariable("ponentes", new PonenteRepo().listarPonente());
				templateEngine.process("Asignacion_Ponente-Evento", context, response.getWriter());
				break;
			}

			case "Asignacion_Ponente-Ponencia": {
				context.setVariable("ponencias", new PonenciaRepo().listarPonencias());
				context.setVariable("ponentes",  new PonenteRepo().listarPonente());
				templateEngine.process("Asignacion_Ponente-Ponencia", context, response.getWriter());
				break;
			}

			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no reconocida: " + accion);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		String path     = (pathInfo != null) ? pathInfo.trim() : "";

		switch (path) {

			// ── CREAR EVENTO ───────────────────────────────────────────
			case "/Crear_Evento": {
				try {
					Evento e = new Evento();
					e.setNombre(request.getParameter("nombre"));
					e.setDescripcion(request.getParameter("descripcion"));
					e.setDireccion(request.getParameter("direccion"));
					e.setCiudad(request.getParameter("ciudad"));
					e.setLugar(request.getParameter("lugar"));
					e.setCapacidad(Integer.parseInt(request.getParameter("capacidad")));
					e.setFechaInicio(LocalDate.parse(request.getParameter("fechaInicio")));
					e.setFechaFin(LocalDate.parse(request.getParameter("fechaFin")));
					e.setEstado(Estado.valueOf(request.getParameter("estado")));
					e.setModalidad(Modalidad.valueOf(request.getParameter("modalidad")));
					new RepoEventos().crearEvento(e);
					response.sendRedirect(request.getContextPath() + "/participantes/Listado_Eventos");
				} catch (Exception ex) {
					ex.printStackTrace();
					response.getWriter().println("Error al crear evento: " + ex.getMessage());
				}
				break;
			}

			// ── MODIFICAR EVENTO ───────────────────────────────────────
			case "/Modificar_Evento": {
				try {
					Evento e = new Evento();
					e.setId_Evento(Integer.parseInt(request.getParameter("idEvento")));
					e.setNombre(request.getParameter("nombre"));
					e.setDescripcion(request.getParameter("descripcion"));
					e.setDireccion(request.getParameter("direccion"));
					e.setCiudad(request.getParameter("ciudad"));
					e.setLugar(request.getParameter("lugar"));
					e.setCapacidad(Integer.parseInt(request.getParameter("capacidad")));
					e.setFechaInicio(LocalDate.parse(request.getParameter("fechaInicio")));
					e.setFechaFin(LocalDate.parse(request.getParameter("fechaFin")));
					e.setEstado(Estado.valueOf(request.getParameter("estado")));
					e.setModalidad(Modalidad.valueOf(request.getParameter("modalidad")));
					new RepoEventos().modificarEvento(e);
					response.sendRedirect(request.getContextPath() + "/participantes/Listado_Eventos");
				} catch (Exception ex) {
					ex.printStackTrace();
					response.getWriter().println("Error al modificar evento: " + ex.getMessage());
				}
				break;
			}

			// ── CREAR PONENCIA ─────────────────────────────────────────
			case "/Crear_Ponencia": {
				try {
					Ponencia p = new Ponencia(0, "", new Tematica(0,""), 0, null, null, "", Nivel.BASICO, Tipo.CHARLA, Formato.PRESENCIAL);
					p.setId_Evento(Integer.parseInt(request.getParameter("idEvento")));
					p.setTitulo(request.getParameter("titulo"));
					p.setDuracion(Integer.parseInt(request.getParameter("duracion")));
					p.setFecha(LocalDate.parse(request.getParameter("fecha")));
					LocalTime hora = LocalTime.parse(request.getParameter("hora"));
					p.setHora(LocalDate.parse(request.getParameter("fecha")).atTime(hora));
					p.setUbicacion(request.getParameter("ubicacion"));
					p.setNivel(Nivel.valueOf(request.getParameter("nivel")));
					p.setTipo(Tipo.valueOf(request.getParameter("tipo")));
					p.setFormato(Formato.valueOf(request.getParameter("formato")));
					int idTematica = Integer.parseInt(request.getParameter("idTematica"));
					p.setTematica(new Tematica(idTematica, ""));
					new PonenciaRepo().crearPonencia(p);
					response.sendRedirect(request.getContextPath() + "/participantes/Listado_Ponencias");
				} catch (Exception ex) {
					ex.printStackTrace();
					response.getWriter().println("Error al crear ponencia: " + ex.getMessage());
				}
				break;
			}

			// ── REGISTRAR PONENTE ──────────────────────────────────────
			case "/Registrar_Ponente": {
				try {
					String nombre      = request.getParameter("nombre");
					String apellidos   = request.getParameter("apellidos");
					String especialidad = request.getParameter("especialidad");
					String correo      = request.getParameter("correo");
					String telefono    = request.getParameter("telefono");
					String cv          = request.getParameter("cv");
					NivelImparticion nivel = NivelImparticion.valueOf(request.getParameter("nivelImparticion"));
					String bio = nombre + " " + apellidos;
					new PonenteRepo().crearPonente(nombre, apellidos, correo, telefono, bio, especialidad, cv, nivel);
					response.sendRedirect(request.getContextPath() + "/participantes/Listado_Ponentes");
				} catch (Exception ex) {
					ex.printStackTrace();
					response.getWriter().println("Error al registrar ponente: " + ex.getMessage());
				}
				break;
			}

			// ── ASIGNACIÓN PONENCIA-EVENTO ─────────────────────────────
			case "/Asignacion_Ponencia-Evento": {
				try {
					int idPonencia = Integer.parseInt(request.getParameter("idPonencia"));
					int idEvento   = Integer.parseInt(request.getParameter("idEvento"));
					new PonenciaRepo().asignarPonenciaEvento(idPonencia, idEvento);
					response.sendRedirect(request.getContextPath() + "/participantes/Asignacion_Ponencia-Evento");
				} catch (Exception ex) {
					ex.printStackTrace();
					response.getWriter().println("Error en asignación: " + ex.getMessage());
				}
				break;
			}

			// ── ASIGNACIÓN PONENTE-EVENTO ──────────────────────────────
			case "/Asignacion_Ponente-Evento": {
				try {
					int idPonente = Integer.parseInt(request.getParameter("idPonente"));
					int idEvento  = Integer.parseInt(request.getParameter("idEvento"));
					new PonenteRepo().asignarPonenteEvento(idPonente, idEvento);
					response.sendRedirect(request.getContextPath() + "/participantes/Asignacion_Ponente-Evento");
				} catch (Exception ex) {
					ex.printStackTrace();
					response.getWriter().println("Error en asignación: " + ex.getMessage());
				}
				break;
			}

			// ── ASIGNACIÓN PONENTE-PONENCIA ────────────────────────────
			case "/Asignacion_Ponente-Ponencia": {
				try {
					int idPonente  = Integer.parseInt(request.getParameter("idPonente"));
					int idPonencia = Integer.parseInt(request.getParameter("idPonencia"));
					new PonenteRepo().asignarPonentePonencia(idPonente, idPonencia);
					response.sendRedirect(request.getContextPath() + "/participantes/Asignacion_Ponente-Ponencia");
				} catch (Exception ex) {
					ex.printStackTrace();
					response.getWriter().println("Error en asignación: " + ex.getMessage());
				}
				break;
			}

			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "POST no reconocido: " + path);
		}
	}
}