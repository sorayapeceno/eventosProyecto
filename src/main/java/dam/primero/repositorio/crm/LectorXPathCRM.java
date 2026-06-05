package dam.primero.repositorio.crm;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LectorXPathCRM {

    private static final String RUTA_XML = "/crm/formulariosCRM.xml";

    public List<String> consultar(String expresionXPath) {
        List<String> resultados = new ArrayList<>();
        InputStream inputStream = null;

        try {
            inputStream = getClass().getResourceAsStream(RUTA_XML);

            if (inputStream == null) {
                resultados.add("No se encontró el fichero XML de formularios.");
            } else {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);

                XPath xpath = XPathFactory.newInstance().newXPath();
                NodeList nodos = (NodeList) xpath.evaluate(expresionXPath, document, XPathConstants.NODESET);

                for (int i = 0; i < nodos.getLength(); i++) {
                    Node nodo = nodos.item(i);
                    String valor = nodo.getNodeValue();

                    if (valor == null) {
                        valor = nodo.getTextContent().trim();
                    }

                    resultados.add(valor);
                }
            }
        } catch (Exception e) {
            resultados.add("Error ejecutando XPath: " + e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                resultados.add("Error cerrando el XML.");
            }
        }

        return resultados;
    }
}
