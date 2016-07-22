package com.traffic.analytics.commons.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yuhuibin
 */
public class FileUtils {
    public static Map<String, Integer> getLineTitleByPattern(String line, String Pattern) throws IOException {
        String[] seg = line.toLowerCase().replace(" ","").replace("\"","").split(Pattern);
        Map<String, Integer> r = new HashMap<String, Integer>();
        for (int i = 0; i < seg.length; i++) {
            r.put(seg[i].trim(), i);
        }
        return r;
    }
}
