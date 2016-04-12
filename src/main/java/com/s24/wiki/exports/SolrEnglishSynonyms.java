package com.s24.wiki.exports;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.s24.wiki.EnglishGrammarPageParser;
import com.s24.wiki.EnglishWikiParser;
import com.s24.wiki.PageParserCallback;

/**
 * 
 * 
 * @author Shopping24 GmbH, Torsten Bøgh Köster (@tboeghk)
 */
public class SolrEnglishSynonyms {

   private static final String stem = "target/stem_en.sql";

   /**
    * @param args
    */
   public static void main(String[] args) {

      // configure sinks
      final File stemout = new File(stem);

      final Map<String, String> stemmap = new TreeMap<String, String>();

      // create parser & add add parser callbacks
      EnglishWikiParser wp = new EnglishWikiParser();
      wp.addParser(new EnglishGrammarPageParser(new PageParserCallback() {

         @Override
         public void callback(List<String> left, List<String> right) {

            if (left.size() > right.size()) {
               stemmap.put("\"" + StringUtils.join(left, "\",\"") + "\"", right.get(0));
            }
         }
      }, false));

      // parse dump
      wp.parse(args[0]);

      // write stems
      try {
         SortedSet<String> keys = new TreeSet<String>(stemmap.keySet());
         FileUtils.writeStringToFile(stemout, "delete from normalization where type='stemming-wiktionary-en-irregular';\n", "utf-8");

         for (String key : keys) {
            FileUtils.writeStringToFile(stemout,
                  "insert into normalization (type, origin, normalization, modified_by, modified_at, active) values ('stemming-wiktionary-en-irregular', '{"
                        + key
                        + "}','"
                        + stemmap.get(key)
                        + "','TorstenKoester',now(),true);\n",
                  "utf-8", true);
         }

      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
