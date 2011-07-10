package ru.yandex.test.impl;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.yandex.test.Duplicate;
import ru.yandex.test.ReportCreator;
import ru.yandex.test.Vacancy;

/**
 *
 * @author OneHalf
 */
public class ReportCreatorImpl implements ReportCreator {
    private final static Logger LOGGER = Logger.getLogger(ReportCreatorImpl.class.getName());

    public ReportCreatorImpl() {
    }
    
    @Override
    public File getReport(Set<Duplicate> vacancies) {
        File fileResult = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.newDocument();
            
            Element root = document.createElement("root");
            for (Duplicate duplicate : vacancies) {
                Element duplElem = createDuplicateXmlElement(document, duplicate);
                root.appendChild(duplElem);
            }
                        
            fileResult = File.createTempFile("dupl", ".html");
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(
                    ReportCreatorImpl.class.getResourceAsStream("report.xsl")));
            transformer.transform(new DOMSource(document), new StreamResult(fileResult));
                    
        } catch (TransformerException ex) {
            Logger.getLogger(ReportCreatorImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return fileResult;
    }

    private Element createDuplicateXmlElement(Document document, Duplicate duplicate) throws DOMException {
        Element result = document.createElement("duplicate");
        
        Element equalency = document.createElement("equalency");
        equalency.appendChild(document.createTextNode(""));
        result.appendChild(equalency);
        
        for (Vacancy vacancy : duplicate.getDuplicates()) {
            result.appendChild(createVacancyXmlElement(document, vacancy));
        }
        return result;
    }
    private Element createVacancyXmlElement(Document document, Vacancy vacancy) throws DOMException {
        Element vacancyElem = document.createElement("vacancy");
        
        Element url = document.createElement("url");
        url.appendChild(document.createTextNode(vacancy.getVacancyUrl()));
        
        Element name = document.createElement("name");
        url.appendChild(document.createTextNode(vacancy.getVacancyName()));
        
        vacancyElem.appendChild(url);
        vacancyElem.appendChild(name);
        
        return vacancyElem;
    }
}
