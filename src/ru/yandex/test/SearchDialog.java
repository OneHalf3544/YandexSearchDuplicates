/*
 * SearchDialog.java
 *
 * Created on 02.07.2011, 19:25:40
 */
package ru.yandex.test;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import java.awt.event.ActionListener;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import ru.yandex.test.impl.SiteParser;
import ru.yandex.test.impl.VacancyXmlFileParser;

/**
 *
 * @author OneHalf
 */
public class SearchDialog extends javax.swing.JFrame {

    private static BeanFactory beanFactory;
    private static final Logger LOGGER = Logger.getLogger(SearchDialog.class.getName());

    static {
        Resource resource = new FileSystemResource("src/ru/yandex/test/config.xml");
        beanFactory = new XmlBeanFactory(resource);
    }
    
    private final Double THRESHOLD_OF_EQUIVALENCE;
    private ReportCreator reportCreator;
    private VacancySource[] vacancySources;
    /** Последний результат сравнения */
    private Set<Duplicate> lastResult = new HashSet<Duplicate>();
    
    /** Соответствие CheckBox'ов и файлов с данными */
    private Map<JCheckBox, VacancyXmlFileParser> preparsedCheckBoxes = new HashMap<JCheckBox, VacancyXmlFileParser>();
    /** Соответствие CheckBox'ов и сайтов с которых берутся данные */
    private Map<JCheckBox, SiteParser> siteCheckBoxes = new HashMap<JCheckBox, SiteParser>();

    /** Creates new form SearchDialog
     * @param thresholdOfEquivalence Порог эквивалентости определяющий уровень 
     * "похожести" ввакансий при котором они считаются равными и попадают в отчет
     */
    public SearchDialog(Double thresholdOfEquivalence) {
        this.THRESHOLD_OF_EQUIVALENCE = thresholdOfEquivalence;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(SearchDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
    }

    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        taResult = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        pnlSiteSearch = new javax.swing.JPanel();
        pnlSearchQuery = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfSearchQuery = new javax.swing.JTextField();
        pnlItemsCount = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfItemCount = new javax.swing.JTextField();
        pnlSites = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pnlFiles = new javax.swing.JPanel();
        pnlButtons = new javax.swing.JPanel();
        cbSearchInSelf = new javax.swing.JCheckBox();
        btnSearch = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Сравнение вакансий");

        taResult.setColumns(20);
        taResult.setFont(new java.awt.Font("Monospaced", 0, 14));
        taResult.setRows(5);
        jScrollPane1.setViewportView(taResult);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.PAGE_AXIS));

        pnlSiteSearch.setBorder(javax.swing.BorderFactory.createTitledBorder("Поиск по сайтам"));
        pnlSiteSearch.setLayout(new javax.swing.BoxLayout(pnlSiteSearch, javax.swing.BoxLayout.PAGE_AXIS));

        pnlSearchQuery.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Что ищем?");
        pnlSearchQuery.add(jLabel1);

        tfSearchQuery.setPreferredSize(new java.awt.Dimension(120, 20));
        pnlSearchQuery.add(tfSearchQuery);

        pnlSiteSearch.add(pnlSearchQuery);

        pnlItemsCount.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setText("Сколько ищем? (на каждый источник)");
        pnlItemsCount.add(jLabel3);

        tfItemCount.setText("25");
        tfItemCount.setMinimumSize(new java.awt.Dimension(60, 20));
        tfItemCount.setPreferredSize(new java.awt.Dimension(60, 20));
        pnlItemsCount.add(tfItemCount);

        pnlSiteSearch.add(pnlItemsCount);

        pnlSites.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setText("Где ищем?");
        pnlSites.add(jLabel2);

        pnlSiteSearch.add(pnlSites);

        jPanel2.add(pnlSiteSearch);

        pnlFiles.setBorder(javax.swing.BorderFactory.createTitledBorder("Уже собранная информация"));
        pnlFiles.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jPanel2.add(pnlFiles);

        pnlButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        cbSearchInSelf.setText("Искать дубли и в пределах одного сайта");
        pnlButtons.add(cbSearchInSelf);

        btnSearch.setText("Найти");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        pnlButtons.add(btnSearch);

        btnReport.setText("Отчет");
        pnlButtons.add(btnReport);

        jPanel2.add(pnlButtons);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }

    /**
     * Реакция на нажатие кнопки поиска
     * @param evt 
     */
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        loadVacancySet();
        Set<Duplicate> searchDuplicateVacancy = searchDuplicateVacancy();
        setDuplicateVacancy(searchDuplicateVacancy);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReport;
    private javax.swing.JButton btnSearch;
    private javax.swing.JCheckBox cbSearchInSelf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlFiles;
    private javax.swing.JPanel pnlItemsCount;
    private javax.swing.JPanel pnlSearchQuery;
    private javax.swing.JPanel pnlSiteSearch;
    private javax.swing.JPanel pnlSites;
    private javax.swing.JTextArea taResult;
    private javax.swing.JTextField tfItemCount;
    private javax.swing.JTextField tfSearchQuery;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Получение строки запроса для поиска по сайтам
     * @return Строка запроса
     */
    private String getSearchString() {
        return tfSearchQuery.getText();
    }

    /**
     * Поиск дублирующихся данных в пределах одного источника
     * @param vacancySource Источник вакансий
     * @return Дубликаты вакансий
     */
    private Set<Duplicate> searchDuplicatesInVacancySource(VacancySource vacancySource) {
        Set<Duplicate> result = new HashSet<Duplicate>();
        Vacancy[] vacancies = vacancySource.getVacancies().toArray(new Vacancy[]{});
        
        for (int i = 0; i < vacancies.length; i++) {
            for (int j = i+1; j < vacancies.length; j++) {
                 if (vacancies[i].getLevelOfSimilarity(vacancies[j]) > THRESHOLD_OF_EQUIVALENCE) {
                    result.add(new Duplicate(vacancies[i], vacancies[j]));
                    LOGGER.log(Level.FINE, "Найдены дубликаты: \n{0}, \n{1}",
                            new Object[]{vacancies[i], vacancies[j]});
                }
            }
        }
        return result;
    }

    /**
     * Установка результата в диалог. Запоминает последний результат, 
     * отображает данные в диалоге
     * @param duplicates Набор дублирующихся вакансий
     */
    private void setDuplicateVacancy(Set<Duplicate> duplicates) {
        lastResult = duplicates;
        StringBuilder sb = new StringBuilder();
        for (Duplicate duplicate : duplicates) {
            Vacancy firstVacancy = duplicate.getDuplicates().iterator().next();
            for (Vacancy vacancy : duplicate.getDuplicates()) {
                sb.append(vacancy.getVacancyName()).append(" ")
                        .append(vacancy.getVacancyUrl()).append("\n");
                if (vacancy != firstVacancy) {
                    sb.append("Похожесть: ")
                            .append((int)(100*firstVacancy.getLevelOfSimilarity(vacancy)))
                            .append("% \n");
                }
            }
            sb.append("\n");
        }
        
        taResult.setText(sb.toString());
    }

    /**
     * Установка сайтов-источников. Добавляет сайты источники в программу и ChekBox'ы в диалог
     * @param siteParsers Сайты-источники
     */
    public void setSiteParsers(Set<SiteParser> siteParsers) {
        for (JCheckBox checkBox : siteCheckBoxes.keySet()) {
            pnlSites.remove(checkBox);
        }
        siteCheckBoxes.clear();
        
        for (SiteParser siteParser : siteParsers) {
            final JCheckBox jCheckBox = new JCheckBox(siteParser.getSourceName(), true);
            pnlSites.add(jCheckBox);
            siteCheckBoxes.put(jCheckBox, siteParser);
        }
    }

    /**
     * Установка файлов-источников. Добавляет файлы-источники в программу и ChekBox'ы в диалог
     * @param files Файлы-источники
     */
    public void setPreparsedXmlFiles(Set<VacancyXmlFileParser> files) {
        for (JCheckBox checkBox : preparsedCheckBoxes.keySet()) {
            pnlFiles.remove(checkBox);
        }
        preparsedCheckBoxes.clear();
        
        for (VacancyXmlFileParser xmlFile : files) {
            final JCheckBox jCheckBox = new JCheckBox(xmlFile.getSourceName(), true);
            pnlFiles.add(jCheckBox);
            preparsedCheckBoxes.put(jCheckBox, xmlFile);
        }
    }

    /**
     * Получение источников, зарегистрированных в программе
     * @return Источники вакйнсий
     */
    private Set<VacancySource> getVacancySources() {
        Set<VacancySource> result = new HashSet<VacancySource>();
        
        for (JCheckBox checkbox : siteCheckBoxes.keySet()) {
            if (checkbox.isSelected()) {
                result.add(siteCheckBoxes.get(checkbox));
            }
        }
        for (JCheckBox checkbox : preparsedCheckBoxes.keySet()) {
            if (checkbox.isSelected()) {
                result.add(preparsedCheckBoxes.get(checkbox));
            }
        }
        return result;
    }

    /**
     * Получить число вакансий, которые нужно собрать с каждого сайта
     * @return Число вакансий, которые нужно собрать с каждого сайта
     */
    private int getItemsCount() {
        return Integer.parseInt(tfItemCount.getText());
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        beanFactory.getBean("dialog");
    }

    /**
     * Установка объекта, создающего отчеты
     * @param reportCreator Создатель отчетов
     */
    public void setReportCreator(ReportCreator reportCreator) {
        this.reportCreator = reportCreator;
        this.btnReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File fileResult = SearchDialog.this.reportCreator.getReport(lastResult);
                    Desktop.getDesktop().open(fileResult);
                } catch (IOException ex) {
                    Logger.getLogger(SearchDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * Отбор источников вакансий в соответствии с состояниями CheckBox'ов
     */
    public void loadVacancySet() {
        vacancySources = this.getVacancySources().toArray(new VacancySource[]{});
        for (SiteParser siteParser : siteCheckBoxes.values()) {
            siteParser.setSearchText(this.getSearchString());
            siteParser.setItemsCount(this.getItemsCount());
        }
    }
    
    /**
     * Поиск дублирующихся вакансий
     * @return Набор дубликатов
     */
    private Set<Duplicate> searchDuplicateVacancy() {
        LOGGER.log(Level.FINE, "Поиск дубликатов");
        
        Set<Duplicate> result = new HashSet<Duplicate>();
        
        for (int i = 0; i < vacancySources.length; i++) {
            for (int j = i+1; j < vacancySources.length; j++) {
                for (Vacancy k : vacancySources[i].getVacancies()) {
                    for (Vacancy l : vacancySources[j].getVacancies()) {
                        if (k.getLevelOfSimilarity(l) > THRESHOLD_OF_EQUIVALENCE) {
                            result.add(new Duplicate(k, l));
                            LOGGER.log(Level.FINE, "Найдены дубликаты: \n{0}, \n{1}",
                                    new Object[]{k, l});
                        }
                    }                    
                }
            }
        }
        LOGGER.log(Level.FINER, "Окончание поиска дубликатов");
        
        if (cbSearchInSelf.isSelected()) {
            for (VacancySource vacancySource : vacancySources) {
                result.addAll(searchDuplicatesInVacancySource(vacancySource));
            }
        }
        return result;
    }
}
