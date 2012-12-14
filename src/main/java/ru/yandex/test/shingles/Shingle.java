package ru.yandex.test.shingles;

/**
 * Класс для вычисления шинглов текста
 * Алгоритм работы описан здесь: http://www.codeisart.ru/python-shingles-algorithm/
 * 
 * @author OneHalf
 */
public class Shingle {
    private static final int[] EMPTY_ARRAY = new int[0];
    /** Число слов в шингле */
    private final int SHINGLE_LENGTH;

    /** Число слов в шингле по умолчанию */
    private final static int DEFAULT_SHINGLE_LENGTH = 5;

    /** Вычисленные шинглы текста */
    private int[] shingle;

    /**
     * Вычисление шинглов с числом слов по умолчанию
     * @param text Текст, для которого вычисляются шинглы
     */
    public Shingle(String text) {
        this(text, DEFAULT_SHINGLE_LENGTH);
    }

    /**
     * Вычисление шинглов с указанным числом слов в шингле
     * @param text Текст, для которого вычисляются шинглы
     * @param shingleLength Число слов в шингле
     */
    public Shingle(String text, int shingleLength) {
        SHINGLE_LENGTH = shingleLength;

        String[] words = canonize(text).split(" ");
        final int shinglesCount = words.length - SHINGLE_LENGTH + 1;
        if (shinglesCount > 0) {
            shingle = new int[shinglesCount];
        }
        else {
            shingle = EMPTY_ARRAY;
        }
        for (int i = 0; i < shingle.length; i++) {
            StringBuilder sb = new StringBuilder(words[i]);
            for (int j = 1; j < SHINGLE_LENGTH; j++) {
                sb.append(" ").append(words[i+j]);
            }
            shingle[i] = sb.toString().hashCode();
        }
    }

    /**
     * Получение вычисленных шинглов для текста
     * @return Вычисленные шинглы
     */
    int[] getShingle() {
        return shingle;
    }
    
    /**
     * Удаление из текста лишних символов и слов
     * @param text Обрабатываемый текст
     * @return Канонизированный текст
     */
    static String canonize(String text) {

        return String.valueOf(text).toLowerCase()
                .replaceAll("[\\*•.,!?:;—\\-\\n\\r()]", " ")
                .replaceAll(
                "(?<=\\s)(это|как|так|и|в|над|к|до|не|на|но|за|то|с|ли"
                + "|а|во|от|со|для|о|же|ну|вы|бы|что|кто|он|она"
                + "|inc|llc|corp|ltd)(?=\\s)", "")
                .replaceAll("(\\s{2,})", " ")
                .trim();
    }
    
    /**
     * Сравнение двух наборов шинглов
     * @param shingle1 Первый набор шинглов
     * @param shingle2 Второй набор шинглов
     * @return Сходство шинглов. От 0.0 до 1.0
     */
    public static double correlation(Shingle shingle1, Shingle shingle2) {
        if (shingle1.SHINGLE_LENGTH != shingle2.SHINGLE_LENGTH) {
            return 0.0;
        }
        int[] sh1 = shingle1.getShingle();
        int[] sh2 = shingle2.getShingle();

        if (sh1.length == 0 || sh2.length == 0) {
            return 0.0;
        }

        int preResult = 0;
        for (int aSh1 : sh1) {
            for (int aSh2 : sh2) {
                if (aSh1 == aSh2) {
                    preResult++;
                    break;
                }
            }
        }
        return 2.0*preResult/(sh1.length + sh2.length);
    }
}
