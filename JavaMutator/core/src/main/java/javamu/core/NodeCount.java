package javamu.core;

import javamu.common.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class NodeCount {
    private static Map<String,Integer> featureMap = new HashMap<>();

    public static void calc(File dir) {
        List<File> files = new ArrayList<>(Arrays.asList(dir.listFiles()));
        for (File file : files) {
            if(file.getName().equalsIgnoreCase("feature_table.txt"))
                continue;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.toString()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String feature = line.split(":")[0];
                    Integer count = Integer.parseInt(line.split(":")[1]);
                    if (featureMap.containsKey(feature)) {
                        featureMap.put(feature, featureMap.get(feature) + count);
                    } else {
                        featureMap.put(feature, count);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        StringBuilder nodeMap = new StringBuilder();
        long total = 0;
        for(Map.Entry<String,Integer> entry : featureMap.entrySet()){
            total += entry.getValue().longValue();
        }
        double tt =0;
        for(Map.Entry<String,Integer> entry : featureMap.entrySet()){
            double percentage = (entry.getValue().doubleValue()/total) * 100;
            tt += percentage;
            nodeMap.append(entry.getKey() + ":" +  percentage +"\n");
        }
        Path output = Paths.get(dir.toString(),"feature_table.txt");
        Util.writeToFile(output.toString(),nodeMap.toString());
    }
}
