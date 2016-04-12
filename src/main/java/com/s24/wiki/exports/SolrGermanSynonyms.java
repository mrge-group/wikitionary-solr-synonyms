package com.s24.wiki.exports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.s24.wiki.GermanGrammarPageParser;
import com.s24.wiki.GermanNounPageParser;
import com.s24.wiki.GermanSubwordPageParser;
import com.s24.wiki.GermanWikiParser;
import com.s24.wiki.PageParserCallback;

/**
 * 
 * 
 * @author Shopping24 GmbH, Torsten Bøgh Köster (@tboeghk)
 */
public class SolrGermanSynonyms {

   private static final String stem = "target/stem_de.sql";
   private static final String subword = "target/subword_de.txt";
   private static final String nouns = "target/nouns_de.sql";

   /**
    * @param args
    */
   public static void main(String[] args) {

      // configure sinks
      final File stemout = new File(stem);
      final File subwordout = new File(subword);
      final File nounsout = new File(nouns);

      final Map<String, Pair<List<String>, List<String>>> subwortmap = new TreeMap<String, Pair<List<String>, List<String>>>();
      final Map<String, String> stemmap = new TreeMap<String, String>();
      final Collection<String> nounsfound = new HashSet<>();

      // create parser & add add parser callbacks
      GermanWikiParser wp = new GermanWikiParser();
      wp.addParser(new GermanSubwordPageParser(new PageParserCallback() {

         @Override
         public void callback(List<String> left, List<String> right) {

            if (left.size() < right.size()) {
               subwortmap.put(left.get(0), Pair.of(left, right));
            }
         }
      }));
      wp.addParser(new GermanGrammarPageParser(new PageParserCallback() {

         @Override
         public void callback(List<String> left, List<String> right) {

            if (left.size() > right.size()) {
               stemmap.put(StringUtils.join(left, ","), StringUtils.join(right, ","));
            }
         }
      }));
      wp.addParser(new GermanNounPageParser(new PageParserCallback() {

         @Override
         public void callback(List<String> left, List<String> right) {
            nounsfound.addAll(left);
         }
      }));

      // parse dump
      wp.parse(args[0]);

      // write subwords
      try {

         SortedSet<String> keys = new TreeSet<String>(subwortmap.keySet());

         FileUtils.writeStringToFile(subwordout, "");

         for (String key : keys) {
            Pair<List<String>, List<String>> pair = subwortmap.get(key);
            List<String> right = pair.getRight();

            SortedSet<String> newright = new TreeSet<String>(right);

            Iterator<String> iterator = right.iterator();
            while (iterator.hasNext()) {
               // is subword a generic word ?
               String subword = iterator.next();

               if (keys.contains(subword)) {
                  Pair<List<String>, List<String>> subpair = subwortmap.get(subword);
                  newright.addAll(subpair.getRight());
               }
            }

            FileUtils.writeStringToFile(subwordout,
                  StringUtils.join(pair.getLeft(), ",") + " => " + StringUtils.join(newright, ",") + "\n", "utf-8",
                  true);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      // write stems
      try {
         SortedSet<String> keys = new TreeSet<String>(stemmap.keySet());
         FileUtils.writeStringToFile(stemout, "delete from normalization where type='stemming-wiktionary-de';");

         for (String key : keys) {
            FileUtils.writeStringToFile(stemout,
                  "insert into normalization (type, origin, normalization, modified_by, modified_at, active) values ('stemming-wiktionary-de', '"
                        + key
                        + "','"
                        + stemmap.get(key)
                        + "','TorstenKoester',now(),true);\n",
                  "utf-8", true);
         }

      } catch (IOException e) {
         e.printStackTrace();
      }

      // write nouns as sql
      try {

         List<String> sortednouns = new ArrayList<>(nounsfound);
         Collections.sort(sortednouns);
         FileUtils.writeStringToFile(nounsout, "delete from lists where type='noun-wiktionary-de';");
         for (String n : sortednouns) {
            FileUtils.writeStringToFile(nounsout,
                  "insert into lists (type, entry, modified_by, modified_at, active) values ('noun-wiktionary-de', '"
                        + n.trim() 
                        + "','TorstenKoester',now(),true);\n",
                  "utf-8", true);
         }

      } catch (IOException e) {
         e.printStackTrace();
      }

   }

}
