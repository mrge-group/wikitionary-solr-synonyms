package com.s24.wiki;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class WordContext {

   private static final String filename = "wordcontext.txt";

   /**
    * @param args
    */
   public static void main(String[] args) {

      WikiApi wa = new WikiApi();

      String[] queries = new String[] { "Dirndl", "Gummistiefel", "Jeanskleider", "Jumpsuit", "Overall", "Latzhose",
            "Lederhosen", "Hut", "Waschmaschine", "Fernseher", "Tablet", "Poncho", "Stulpen", "KŸhlschrank",
            "Notebook", "Plasma-TV", "samsung handys"

      };

      final File file = new File(filename);

      try {
         FileUtils.writeStringToFile(file, "");

         for (String query : queries) {
            System.out.println("query " + query + "...");
            Set<String> result = wa.query(query, new String[] { "links", "categories" });

            FileUtils.writeStringToFile(file, query + " -> " + StringUtils.join(result, '|') + "\n", "utf-8", true);

         }

      } catch (IOException e) {
         e.printStackTrace();
      }

      System.out.println("done.");

   }

}
