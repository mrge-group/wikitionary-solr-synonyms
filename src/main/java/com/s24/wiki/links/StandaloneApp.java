package com.s24.wiki.links;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class StandaloneApp {
   @Parameter(names = "-in", required = true, description = "File with one keyword per line")
   private String in;

   @Parameter(names = "-out", required = true, description = "Where to save the expanded keywords")
   private String out;
   
   private static final Logger log = LoggerFactory.getLogger(StandaloneApp.class);
   
   /**
    * @param args
    * @throws IOException
    */
   public static void main(String[] args) throws IOException {
      StandaloneApp app = new StandaloneApp();
      JCommander jc = new JCommander(app);
      PrintStream err = System.err;

      try {
         jc.parse(args);
         app.run();
      } catch (ParameterException e) {
         err.println(e.getMessage());
         jc.usage();
         System.exit(1);
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(2);
      }
   }
   
   private void run() {
      ExpanderService service = new ExpanderService();
      service.expander = new WikipediaExpanderDao();
      
      service.init(new File(in));
      Map<String, Set<String>> expandedKeywords = service.expandKeywords();
      
      writeToFile(expandedKeywords, new File(out));
   }
   
   public static void writeToFile(Map<String, Set<String>> terms, File out) {
      try {
         FileUtils.writeStringToFile(out, "");

         for (Entry<String, Set<String>> entry : terms.entrySet()) {
            String line = entry.getKey() + " -> " + StringUtils.join(entry.getValue().toArray(), "|") + "\n";
            FileUtils.writeStringToFile(out, line, Charset.forName("utf-8"), true);
         }
      } catch (IOException e) {
         log.error("", e);
      }
   }
}
