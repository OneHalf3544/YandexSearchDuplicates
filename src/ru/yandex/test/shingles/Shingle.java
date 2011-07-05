package ru.yandex.test.shingles;

/**
 *
 * @author OneHalf
 */
public class Shingle {
    private final int SHINGLE_LENGTH;
    private final static int DEFAULT_SHINGLE_LENGTH = 5;

    private int[] shingle;

    public Shingle(String text) {
        this(text, DEFAULT_SHINGLE_LENGTH);
    }
    
    public Shingle(String text, int shingleLength) {
        SHINGLE_LENGTH = shingleLength;
        
        String[] words = canonize(text).split(" ");
        final int shinglesCount = words.length - SHINGLE_LENGTH + 1;
        if (shinglesCount > 0) {
            shingle = new int[shinglesCount];
        }
        else {
            shingle = new int[0];
        }
        for (int i = 0; i < shingle.length; i++) {
            StringBuilder sb = new StringBuilder(words[i]);
            for (int j = 1; j < SHINGLE_LENGTH; j++) {
                sb.append(" ").append(words[i+j]);
            }
            shingle[i] = sb.toString().hashCode();
        }
    }

    public int[] getShingle() {
        return shingle;
    }
    
    static String canonize(String text) {
        String result = String.valueOf(text).toLowerCase()
                .replaceAll("[.,!?:;\\-\\n\\r()]", " ")
                .replaceAll(
                "(?<=\\s)(это|как|так|и|в|над|к|до|не|на|но|за|то|с|ли"
                + "|а|во|от|со|для|о|же|ну|вы|бы|что|кто|он|она"
                + "|inc|llc|corp|ltd)(?=\\s)", "")
                .replaceAll("(\\s{2,})", " ")
                .trim();
        
        return result;
    }
    
    public static double corellation(Shingle shingle1, Shingle shingle2) {
        if (shingle1.SHINGLE_LENGTH != shingle2.SHINGLE_LENGTH) {
            return 0.0;
        }        
        int preResult = 0;
        final int[] sh1 = shingle1.getShingle();
        final int[] sh2 = shingle2.getShingle();
        
        for (int i = 0; i < sh1.length; i++) {
            for (int j = 0; j < sh2.length; j++) {
                if (sh1[i] == sh2[j]) {
                    preResult++;
                }
            }
        }
        return 2*100.0*preResult/(sh1.length + sh2.length);
    }
}
