package semit.isppd.studyload2025.core.general.utils.cyrToLatin;

import java.util.*;

public final class UkrainianToLatin {
    private static final int INDEX_0 = 0;
    private static final int INDEX_1 = 1;
    private static final int INDEX_2 = 2;
    private static final int INDEX_3 = 3;
    private static final int INDEX_4 = 4;
    private static final int INDEX_8 = 8;
    private static final int LENGTH_2 = 2;
    private static final int LENGTH_3 = 3;
    private static final int LENGTH_4 = 4;
    private static final int LENGTH_8 = 8;
    private static final Set<String> PUNCTUATIONS = new HashSet<String>(Arrays.asList(
            ",", "-", "!", "?", ":", ";", ".", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "…", "—", "“", "”",
            "«", "»", "[", "]", "–", "(", ")", "№", "{", "}", "/", "\\"));

    private enum Convert {
        AA("Аа"),
        BB("Бб"),
        VV("Вв"),
        HH("Гг"),
        GG("Ґґ"),
        DD("Дд"),
        EE("Ее"),
        YeIe("Єє"),
        ZhZh("Жж"),
        ZZ("Зз"),
        YY("Ии"),
        II("Іі"),
        YiI("Її"),
        YI("Йй"),
        KK("Кк"),
        LL("Лл"),
        MM("Мм"),
        NN("Нн"),
        OO("Оо"),
        PP("Пп"),
        RR("Рр"),
        SS("Сс"),
        TT("Тт"),
        UU("Уу"),
        FF("Фф"),
        KhKh("Хх"),
        TsTs("Цц"),
        ChCh("Чч"),
        ShSh("Шш"),
        ShchShch("Щщ"),
        YuIu("Юю"),
        YaIa("Яя");
        private String cyrilic;

        private Convert(String cyrilic) {
            this.cyrilic = cyrilic;
        }

        /**
         * Gets cyrilic.
         *
         * @return the cyrilic
         */
        public String getCyrilic() {
            return cyrilic;
        }

    }

    private static Map<String, ConvertCase> cyrToLat;

    private static class ConvertCase {
        private final Convert convert;
        private final boolean lowcase;

        public ConvertCase(Convert convert, boolean lowcase) {
            this.convert = convert;
            this.lowcase = lowcase;
        }

        public Convert getConvert() {
            return convert;
        }

        public boolean isLowcase() {
            return lowcase;
        }
    }

    static {
        cyrToLat = new HashMap<String, ConvertCase>();
        for (Convert convert : Convert.values()) {
            cyrToLat.put(convert.getCyrilic().substring(INDEX_0, INDEX_1), new ConvertCase(convert, false));
            cyrToLat.put(convert.getCyrilic().substring(INDEX_1, INDEX_2), new ConvertCase(convert, true));
            if (convert == Convert.EE) {
                cyrToLat.put("Ё", new ConvertCase(convert, false));
                cyrToLat.put("ё", new ConvertCase(convert, true));
            }
        }
    }

    /**
     * Generates latinic from cyrilic.
     *
     * @param name the name
     * @return the result
     */
    public static String generateLat(String name) {
        StringBuilder result = new StringBuilder();
        ConvertCase prevConvertCase = null;
        for (int index = 0; index < name.length(); index += 1) {
            String curChar = name.substring(index, index + INDEX_1);
            String nextChar = index == name.length() - 1 ? null : name.substring(index + INDEX_1, index + INDEX_2);
            if (cyrToLat.get(curChar) == null) {
                if (" ".equals(curChar)) {
                    prevConvertCase = null;
                    result.append(' ');
                } else if (curChar.matches("\\n") || PUNCTUATIONS.contains(curChar)) {
                    result.append(curChar);
                }
                continue;
            }
            ConvertCase convertCase = cyrToLat.get(curChar);
            if (prevConvertCase == null) {
                checkFirstChar(result, convertCase, cyrToLat.get(nextChar) == null ? convertCase : cyrToLat
                        .get(nextChar));
            } else {
                checkMiddleChar(result, convertCase, cyrToLat.get(nextChar) == null ? convertCase : cyrToLat
                        .get(nextChar));
            }
            prevConvertCase = convertCase;
        }
        return result.toString();
    }

    /**
     * Converts first character in the word.
     *
     * @param result          resut buffer to store string in latin
     * @param convertCase     current character object
     * @param nextConvertCase next character object
     */
    private static void checkFirstChar(StringBuilder result, ConvertCase convertCase, ConvertCase nextConvertCase) {
        String latName = convertCase.getConvert().name();
        switch (latName.length()) {
            case LENGTH_2:
                result.append(convertCase.isLowcase() ? latName.substring(INDEX_0, INDEX_1).toLowerCase() : nextConvertCase
                        .isLowcase() ? latName.substring(INDEX_0, INDEX_1) : latName.substring(INDEX_0, INDEX_1)
                        .toUpperCase());
                if (convertCase.getConvert() == Convert.ZZ && nextConvertCase.getConvert() == Convert.HH) {
                    result.append(nextConvertCase.isLowcase() ? "g" : "G");
                }
                break;
            case LENGTH_3:
            case LENGTH_4:
                result.append(convertCase.isLowcase() ? latName.substring(INDEX_0, INDEX_2).toLowerCase() : nextConvertCase
                        .isLowcase() ? latName.substring(INDEX_0, INDEX_2) : latName.substring(INDEX_0, INDEX_2)
                        .toUpperCase());
                break;
            case LENGTH_8:
                result.append(convertCase.isLowcase() ? latName.substring(INDEX_0, INDEX_4).toLowerCase() : nextConvertCase
                        .isLowcase() ? latName.substring(INDEX_0, INDEX_4) : latName.substring(INDEX_0, INDEX_4)
                        .toUpperCase());
                break;
            default:
                break;
        }
    }

    /**
     * Converts middle or last character in the word.
     *
     * @param result          resut buffer to store string in latin
     * @param convertCase     current character object
     * @param nextConvertCase next character object
     */
    private static void checkMiddleChar(StringBuilder result, ConvertCase convertCase, ConvertCase nextConvertCase) {
        String latName = convertCase.getConvert().name();
        switch (latName.length()) {
            case LENGTH_2:
                result.append(convertCase.isLowcase() ? latName.substring(INDEX_1, INDEX_2).toLowerCase() : nextConvertCase
                        .isLowcase() ? latName.substring(INDEX_1, INDEX_2) : latName.substring(INDEX_1, INDEX_2)
                        .toUpperCase());
                if (convertCase.getConvert() == Convert.ZZ && nextConvertCase.getConvert() == Convert.HH) {
                    result.append(nextConvertCase.isLowcase() ? "g" : "G");
                }
                break;
            case LENGTH_3:
                result.append(convertCase.isLowcase() ? latName.substring(INDEX_2, INDEX_3).toLowerCase() : nextConvertCase
                        .isLowcase() ? latName.substring(INDEX_2, INDEX_3) : latName.substring(INDEX_2, INDEX_3)
                        .toUpperCase());
                break;
            case LENGTH_4:
                result.append(convertCase.isLowcase() ? latName.substring(INDEX_2, INDEX_4).toLowerCase() : nextConvertCase
                        .isLowcase() ? latName.substring(INDEX_2, INDEX_4) : latName.substring(INDEX_2, INDEX_4)
                        .toUpperCase());
                break;
            case LENGTH_8:
                result.append(convertCase.isLowcase() ? latName.substring(INDEX_4, INDEX_8).toLowerCase() : nextConvertCase
                        .isLowcase() ? latName.substring(INDEX_4, INDEX_8) : latName.substring(INDEX_4, INDEX_8)
                        .toUpperCase());
                break;
            default:
                break;
        }
    }
}