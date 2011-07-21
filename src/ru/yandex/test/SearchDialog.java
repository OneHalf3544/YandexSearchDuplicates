/*
 * SearchDialog.java
 *
 * Created on 02.07.2011, 19:25:40
 */
package ru.yandex.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.yandex.test.impl.SiteParser;
import ru.yandex.test.impl.VacancyXmlFileParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OneHalf
 */
@SuppressWarnings({"FieldCanBeLocal"})
public class SearchDialog extends javax.swing.JFrame {

    private static BeanFactory beanFactory;
    private static final Logger LOGGER = Logger.getLogger(SearchDialog.class.getName());

    private final Double THRESHOLD_OF_EQUIVALENCE;

    private ReportCreator reportCreator;
    private List<VacancySource> vacancySources;
    /** Последний результат сравнения */
    private Collection<Duplicate> lastResult = new HashSet<Duplicate>();
    /** Соответствие CheckBox'ов и файлов с данными */
    private Map<JCheckBox, VacancyXmlFileParser> preparsedCheckBoxes = new HashMap<JCheckBox, VacancyXmlFileParser>();

    /** Соответствие CheckBox'ов и сайтов с которых берутся данные */
    private Map<JCheckBox, SiteParser> siteCheckBoxes = new HashMap<JCheckBox, SiteParser>();

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
    private javax.swing.JLabel waitLabel;

    static {
        beanFactory = new ClassPathXmlApplicationContext("/ru/yandex/test/config.xml");
    }

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
        cbSearchInSelf = new javax.swing.JCheckBox("Искать дубли и в пределах одного сайта", true);
        btnSearch = new javax.swing.JButton();
        btnReport = new javax.swing.JButton();
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
        pnlButtons.add(btnReport);

        jPanel2.add(pnlButtons);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }

    /**
     * Реакция на нажатие кнопки поиска
     */
    private void btnSearchActionPerformed() {
        // Запускаем поиск. SearchProcessor сам установит результат в окно
        new SearchProcessor().execute();
    }

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

        List<Vacancy> vacancies = vacancySource.getVacancies();

        for (int i = 0; i < vacancies.size(); i++) {
            for (int j = i+1; j < vacancies.size(); j++) {
                 if (vacancies.get(i).getLevelOfSimilarity(vacancies.get(j)) > THRESHOLD_OF_EQUIVALENCE) {
                    result.add(new Duplicate(vacancies.get(i), vacancies.get(j)));
                    LOGGER.log(Level.FINE, "Найдены дубликаты: \n{0}, \n{1}",
                            new Object[]{vacancies.get(i), vacancies.get(j)});
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
    private void setDuplicateVacancy(Collection<Duplicate> duplicates) {
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
     * Получение источников, зарегистрированных в программе
     * @return Источники вакйнсий
     */
    private List<VacancySource> getVacancySources() {
        List<VacancySource> result = new ArrayList<VacancySource>();

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
     * Установка объекта, создающего отчеты
     * @param reportCreator Создатель отчетов
     */
    @SuppressWarnings({"UnusedDeclaration"})
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
    private void loadVacancySet() {

        // Показываем пользователю, что нужно подождать
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                waitLabel.setVisible(true);
                taResult.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                SearchDialog.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            }
        });

        vacancySources = this.getVacancySources();

        Set<SiteParser> selectedSiteParsers = new HashSet<SiteParser>();
        for (JCheckBox checkBox : siteCheckBoxes.keySet()) {
            if (checkBox.isSelected()) {
                selectedSiteParsers.add(siteCheckBoxes.get(checkBox));
            }
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        final CountDownLatch latch = new CountDownLatch(selectedSiteParsers.size());

        for (final SiteParser siteParser : selectedSiteParsers) {
            // Установка данных поиска
            siteParser.setSearchText(this.getSearchString());
            siteParser.setItemsCount(this.getItemsCount());

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // Получаем вакансии чтобы "обмануть" ленивую инициализацию
                    siteParser.getVacancies();
                    latch.countDown();
                }
            });
        }
        executorService.shutdown();
        try {
            latch.await();
            // Убираем надпись ожидания и возвращаем курсоры по умолчанию
            SwingUtilities.invokeLater(new Runnable(){
                public void run(){
                    SearchDialog.this.setCursor(Cursor.getDefaultCursor());
                    taResult.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    waitLabel.setVisible(false);
                }
            });
        } catch (InterruptedException e) {
            System.exit(1);
        }
    }
    
    /**
     * Поиск дублирующихся вакансий
     * @return Набор дубликатов
     */
    private Set<Duplicate> searchDuplicateVacancy() {
        LOGGER.log(Level.FINE, "Поиск дубликатов");
        
        Set<Duplicate> result = new HashSet<Duplicate>();

        // Получаем пару источников вакансий
        for (int i = 0; i < vacancySources.size(); i++) {
            for (int j = i+1; j < vacancySources.size(); j++) {

                // Сравниваем все вакансии из данной пары источников
                for (Vacancy k : vacancySources.get(i).getVacancies()) {
                    for (Vacancy l : vacancySources.get(j).getVacancies()) {

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

    public class SearchProcessor extends SwingWorker<Set<Duplicate>, Duplicate> {

        @Override
        protected Set<Duplicate> doInBackground() throws Exception {
            loadVacancySet();
            return searchDuplicateVacancy();
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
