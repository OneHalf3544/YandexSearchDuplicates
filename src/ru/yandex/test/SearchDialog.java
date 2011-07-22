/*
 * SearchDialog.java
 *
 * Created on 02.07.2011, 19:25:40
 */
package ru.yandex.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OneHalf
 */
public class SearchDialog extends javax.swing.JFrame {

    private static final BeanFactory beanFactory = new ClassPathXmlApplicationContext("/ru/yandex/test/config.xml");
    private static final Logger LOGGER = Logger.getLogger(SearchDialog.class.getName());

    @Autowired
    private ReportCreator reportCreator;

    @Autowired
    private SearchManager searchManager;

    /** Последний результат сравнения */
    private Collection<Duplicate> lastResult = new HashSet<Duplicate>();

    /** Соответствие CheckBox'ов и файлов с данными */
    private Map<JCheckBox, VacancyXmlFileParser> preparsedCheckBoxes = new HashMap<JCheckBox, VacancyXmlFileParser>();

    /** Соответствие CheckBox'ов и сайтов с которых берутся данные */
    private Map<JCheckBox, SiteParser> siteCheckBoxes = new HashMap<JCheckBox, SiteParser>();

    private javax.swing.JTextArea taResult;
    private javax.swing.JTextField tfItemCount;
    private javax.swing.JTextField tfSearchQuery;
    private javax.swing.JLabel waitLabel;
    private JPanel pnlSites;
    private JPanel pnlFiles;
    private JCheckBox cbSearchInSelf;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final JFrame dialog = (JFrame) beanFactory.getBean("dialog");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(true);
            }
        });
    }

    /**
     * Creates new form SearchDialog
     */
    public SearchDialog() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(SearchDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
    }

    private void initComponents() {

        JScrollPane jScrollPane1 = new JScrollPane();
        taResult = new javax.swing.JTextArea();
        JPanel jPanel2 = new JPanel();
        JPanel pnlSiteSearch = new JPanel();
        JPanel pnlSearchQuery = new JPanel();
        JLabel jLabel1 = new JLabel();
        tfSearchQuery = new javax.swing.JTextField();
        JPanel pnlItemsCount = new JPanel();
        JLabel jLabel3 = new JLabel();
        tfItemCount = new javax.swing.JTextField();
        pnlSites = new JPanel();
        JLabel jLabel2 = new JLabel();
        pnlFiles = new JPanel();
        JPanel pnlButtons = new JPanel();
        cbSearchInSelf = new JCheckBox("Искать дубли и в пределах одного сайта", true);
        JButton btnSearch = new JButton();
        JButton btnReport = new JButton();
        waitLabel = new javax.swing.JLabel();

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

        waitLabel.setText("<html><font color='#a0a0a0'>Ждите...</font></html>");
        waitLabel.setVisible(false);
        pnlSearchQuery.add(waitLabel);

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

        pnlButtons.add(cbSearchInSelf);

        btnSearch.setText("Найти");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed();
            }
        });
        pnlButtons.add(btnSearch);

        btnReport.setText("Отчет");
        btnReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnReportActionPerformed();
            }
        });
        pnlButtons.add(btnReport);

        jPanel2.add(pnlButtons);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }

    private void btnReportActionPerformed() {
        try {
            File fileResult = reportCreator.getReport(lastResult);
            Desktop.getDesktop().open(fileResult);
        } catch (IOException ex) {
            Logger.getLogger(SearchDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Реакция на нажатие кнопки поиска
     */
    private void btnSearchActionPerformed() {
        // Запускаем поиск. SearchProcessor сам установит результат в окно
        new SearchProcessor().execute();
    }

    /**
     * Установка результата в диалог. Запоминает последний результат,
     * отображает данные в диалоге
     * @param duplicates Набор дублирующихся вакансий
     */
    private void setDuplicateVacancy(Collection<Duplicate> duplicates) {
        lastResult = duplicates;
        StringBuilder sb = new StringBuilder();

        for (Duplicate duplicate : duplicates) {

            Vacancy firstVacancy = duplicate.getDuplicates().iterator().next();
            for (Vacancy vacancy : duplicate.getDuplicates()) {

                sb.append(vacancy.getVacancyName()).append(" ").append(vacancy.getVacancyUrl()).append("\n");
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
    @SuppressWarnings({"UnusedDeclaration"})
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
    @SuppressWarnings({"UnusedDeclaration"})
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
     * Получить число вакансий, которые нужно собрать с каждого сайта
     * @return Число вакансий, которые нужно собрать с каждого сайта
     */
    private int getItemsCount() {
        return Integer.parseInt(tfItemCount.getText());
    }

    /**
     * Получение строки запроса для поиска по сайтам
     * @return Строка запроса
     */
    private String getSearchString() {
        return tfSearchQuery.getText();
    }

    /**
     * Отбор источников вакансий в соответствии с состояниями CheckBox'ов
     * @throws InterruptedException При прерывании программы
     */
    public void loadVacancySet() throws InterruptedException {
        setWaitState();

        Set<SiteParser> selectedSiteParsers = new HashSet<SiteParser>();
        for (JCheckBox checkBox : siteCheckBoxes.keySet()) {
            if (checkBox.isSelected()) {
                selectedSiteParsers.add(siteCheckBoxes.get(checkBox));
            }
        }

        Set<VacancyXmlFileParser> preparsedFiles = new HashSet<VacancyXmlFileParser>();
        for (JCheckBox checkBox : preparsedCheckBoxes.keySet()) {
            if (checkBox.isSelected()) {
                preparsedFiles.add(preparsedCheckBoxes.get(checkBox));
            }
        }

        searchManager.setSiteParsers(selectedSiteParsers);
        searchManager.setPreparsedXmlFiles(preparsedFiles);

        searchManager.setSearchInSelf(cbSearchInSelf.isSelected());
        searchManager.setItemsCount(getItemsCount());
        searchManager.setQueryString(getSearchString());

        searchManager.initializeSources();

        setReadyState();
    }

    /**
     * Установка фрейма в статус "Ждите" - добавляется надпись "Ждите..." и курсоры меняются
     * на песочные часы
     */
    private void setWaitState() {
        // Показываем пользователю, что нужно подождать
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                waitLabel.setVisible(true);
                taResult.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            }
        });
    }

    /**
     * Установка фрейма в статус "Готово"
     */
    private void setReadyState() {
        // Убираем надпись ожидания и возвращаем курсоры по умолчанию
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setCursor(Cursor.getDefaultCursor());
                taResult.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                waitLabel.setVisible(false);
            }
        });
    }

    class SearchProcessor extends SwingWorker<Set<Duplicate>, Duplicate> {

        @Override
        protected Set<Duplicate> doInBackground() throws Exception {
            loadVacancySet();
            return searchManager.searchDuplicates();
        }

        @Override
        protected void done() {
            try {
                setDuplicateVacancy(this.get());
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Ошибка при поиске дубликатов", e);
            }
        }
    }
}
