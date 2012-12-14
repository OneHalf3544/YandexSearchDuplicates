package ru.yandex.test;

import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import ru.yandex.test.impl.SiteParserImpl;
import ru.yandex.test.impl.VacancyXmlFileParserImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для считывания конфигурационных данных из файла настроек
 *
 * Date: 13.10.11
 * Time: 10:01
 *
 * @author OneHalf
 */
public class Configuration {

    private static final Pattern PREPARSED_PATTERN = Pattern.compile("(preparsedfiles\\.\\w+).name");
    private static final Pattern PARSER_PATTERN = Pattern.compile("(sites\\.[\\w]+).name");

    private final Set<SiteParser> siteParsers = Sets.newHashSet();

    private final Set<VacancyXmlFileParser> preparsedFiles = Sets.newHashSet();

    Configuration(File configFile) throws IOException {

        FileReader reader = new FileReader(configFile);
        try{
            Properties properties = new Properties();
            properties.load(reader);

            loadProperties(properties);
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
    }

    final void loadProperties(Properties properties) throws FileNotFoundException {

        for (Object o : properties.keySet()) {
            String key = o.toString();

            Matcher matcher;

            matcher = PARSER_PATTERN.matcher(key);
            if (matcher.find()) {
                String fiieKey = matcher.group(1) + ".file";

                String name = properties.getProperty(key);
                String file = properties.getProperty(fiieKey);

                siteParsers.add(new SiteParserImpl(name, file));

                continue;
            }

            matcher = PREPARSED_PATTERN.matcher(key);
            if (matcher.find()) {
                String fiieKey = matcher.group(1) + ".file";

                String name = properties.getProperty(key);
                String file = properties.getProperty(fiieKey);

                preparsedFiles.add(new VacancyXmlFileParserImpl(name, file));
            }
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    Set<SiteParser> getSiteParsers() {
        return siteParsers;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    Set<VacancyXmlFileParser> getPreparsedXmlFiles() {
        return preparsedFiles;
    }
}
