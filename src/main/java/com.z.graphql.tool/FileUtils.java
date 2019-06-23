package com.z.graphql.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * We are not dealing with much of file reading here, so depending on a library like apache-commons would be an
 * overkill.
 */
public class FileUtils {
    public static String readFileToString(final String filePath) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream inputStream = new FileInputStream(filePath); Scanner sc = new Scanner(inputStream, "UTF-8")) {
            while (sc.hasNextLine()) {
                stringBuilder.append(sc.nextLine());
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        }
        return stringBuilder.toString();
    }
}
