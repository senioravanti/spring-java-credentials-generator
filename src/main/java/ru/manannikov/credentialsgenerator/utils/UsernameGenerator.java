package ru.manannikov.credentialsgenerator.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static java.util.Map.*;


public class UsernameGenerator {
    private static final Logger logger = LogManager.getLogger(UsernameGenerator.class);

    private static final Map<Character, String> translit = Map.<Character, String>ofEntries(
        entry('А', "A"),
        entry('Б', "B"),
        entry('В', "V"),
        entry('Г', "G"),
        entry('Д', "D"),
        entry('Е', "E"),
        entry('Ё', "E"),
        entry('Ж', "Zh"),
        entry('З', "Z"),
        entry('И', "I"),
        entry('Й', "I"),
        entry('К', "K"),
        entry('Л', "L"),
        entry('М', "M"),
        entry('Н', "N"),
        entry('О', "O"),
        entry('П', "P"),
        entry('Р', "R"),
        entry('С', "S"),
        entry('Т', "T"),
        entry('У', "U"),
        entry('Ф', "F"),
        entry('Х', "Kh"),
        entry('Ц', "C"),
        entry('Ч', "Ch"),
        entry('Ш', "Sh"),
        entry('Щ', "Sch"),
        entry('Ъ', "'"),
        entry('Ы', "Y"),
        entry('Ь', "'"),
        entry('Э', "E"),
        entry('Ю', "Yu"),
        entry('Я', "Ya"),
        entry('а', "a"),
        entry('б', "b"),
        entry('в', "v"),
        entry('г', "g"),
        entry('д', "d"),
        entry('е', "e"),
        entry('ё', "e"),
        entry('ж', "zh"),
        entry('з', "z"),
        entry('и', "i"),
        entry('й', "i"),
        entry('к', "k"),
        entry('л', "l"),
        entry('м', "m"),
        entry('н', "n"),
        entry('о', "o"),
        entry('п', "p"),
        entry('р', "r"),
        entry('с', "s"),
        entry('т', "t"),
        entry('у', "u"),
        entry('ф', "f"),
        entry('х', "h"),
        entry('ц', "c"),
        entry('ч', "ch"),
        entry('ш', "sh"),
        entry('щ', "sch"),
        entry('ъ', "'"),
        entry('ы', "y"),
        entry('ь', "'"),
        entry('э', "e"),
        entry('ю', "yu"),
        entry('я', "ya"),
        entry(' ', "-")
    );
    private static final String FULL_NAME_REGEX = "[\\s.]+";

    public static String transliterate(String fullName) {
        var nameParts = fullName.strip().split(FULL_NAME_REGEX);
        var username = new StringBuilder();

        String lastName = Character.toUpperCase(nameParts[0].charAt(0)) + nameParts[0].substring(1).toLowerCase();

        for (int i = 0; i < lastName.length(); ++i) {
            username.append(
                translit.get(lastName.charAt(i))
            );
        }

        try {
            username.append(
                translit.get(nameParts[1].charAt(0))
            );
            username.append(
                translit.get(nameParts[2].charAt(0))
            );
        } catch (ArrayIndexOutOfBoundsException ex) {
            logger.error("Во входных данных отсутствует имя или отчество, или и то и другое:\n{}", ex.toString());
        }

        return username.toString();
    }
}
