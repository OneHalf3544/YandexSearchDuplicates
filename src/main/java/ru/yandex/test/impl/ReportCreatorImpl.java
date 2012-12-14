package ru.yandex.test.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.yandex.test.Duplicate;
import ru.yandex.test.ReportCreator;
import ru.yandex.test.Vacancy;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Класс объектов, создающих отчеты
 * 
 * @author OneHalf
 */
@Service("reportCreator")
public class ReportCreatorImpl implements ReportCreator {

    private static final Logger log = Logger.getLogger(ReportCreatorImpl.class);

    @Override
    public File getReport(Collection<Duplicate> vacancies) {
        File fileResult = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.newDocument();
            
            Element root = document.createElement("root");
            document.appendChild(root);
            for (Duplicate duplicate : vacancies) {
                Element duplElem = createDuplicateXmlElement(document, duplicate);
                root.appendChild(duplElem);
            }
                        
            fileResult = File.createTempFile("dupl", ".html");
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource("conf/report.xsl"));
            transformer.transform(new DOMSource(document), new StreamResult(fileResult));
                    
        } catch (TransformerException ex) {
            log.error("xml transformer error", ex);
        } catch (IOException ex) {
            log.error("I/O Error", ex);
        } catch (ParserConfigurationException ex) {
            log.error("parse config error", ex);
        }
        return fileResult;
    }

    /**
     * Создание xml-элемента с данными компании
     * @param document Документ, в котором должен создаться элемент
     * @param vacancy Вакансия для которой создается элемент
     * @return Xml-элемент, который содержит данные о компании-работодателе
     * @throws DOMException Ошибка построения DOM
     */
    private Element createCompanyXmlElement(Document document, Vacancy vacancy) throws DOMException {
        Element companyName = document.createElement("name");
        companyName.appendChild(document.createTextNode(vacancy.getCompanyName()));
        
        Element companyUrl = document.createElement("url");
        companyUrl.appendChild(document.createTextNode(vacancy.getCompanyUrl()));
        
        Element company = document.createElement("company");
        company.appendChild(companyName);
        company.appendChild(companyUrl);
        
        return company;
    }

    /**
     * Создание xml-элемента, представляющего одинаковые вакансии
     * @param document Документ-владелец элемента
     * @param duplicate Дубликат, который нужно добавить в отчет
     * @return xml-представление объекта
     * @throws DOMException Ошибка построения DOM
     */
    private Element createDuplicateXmlElement(Document document, Duplicate duplicate) throws DOMException {
        Element result = document.createElement("duplicates");
        
        Iterator<Vacancy> iterator = duplicate.getDuplicates().iterator();
        Element equalency = document.createElement("equalency");
        final Double levelOfSimilarity = iterator.next().getLevelOfSimilarity(iterator.next());
        equalency.appendChild(document.createTextNode(
                String.valueOf((int)(100*levelOfSimilarity))));
        result.appendChild(equalency);
        
        for (Vacancy vacancy : duplicate.getDuplicates()) {
            result.appendChild(createVacancyXmlElement(document, vacancy));
        }
        return result;
    }
    
    /**
     * Создание xml-элемента для вакансии
     * @param document Документ, в который нужно добавить элемент
     * @param vacancy Вакансия, которую нужно добавить в отчет
     * @return Xml-представление вакансии
     * @throws DOMException Ошибка построения DOM
     */
    private Element createVacancyXmlElement(Document document, Vacancy vacancy) throws DOMException {
        Element vacancyElem = document.createElement("vacancy");
        
        Element url = document.createElement("url");
        url.appendChild(document.createTextNode(vacancy.getVacancyUrl()));
        
        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(vacancy.getVacancyName()));
        
        Element company = createCompanyXmlElement(document, vacancy);
        
        Element city = document.createElement("city");
        city.appendChild(document.createTextNode(vacancy.getCity()));
        
        Element salary = document.createElement("salary");
        salary.appendChild(document.createTextNode(vacancy.getSalary().toString()));
        
        vacancyElem.appendChild(url);
        vacancyElem.appendChild(name);
        vacancyElem.appendChild(company);
        vacancyElem.appendChild(city);
        vacancyElem.appendChild(salary);
        
        return vacancyElem;
    }
}
