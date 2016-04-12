package com.s24.wiki;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.jhu.nlp.wikipedia.WikiPage;

public class GermanGrammarPageParser extends GermanPageParser {

   public GermanGrammarPageParser(PageParserCallback cb) {
      super(cb);
   }

   @Override
   public void parse(WikiPage page) {

      if (isValidPage(page)) {
         Pattern pattern = Pattern.compile("\\{\\{.*\\n(\\|.*\\n){8}");
         Matcher matcher = pattern.matcher(page.getWikiText());
         List<String> left = new ArrayList<String>();
         List<String> right = new ArrayList<String>();
         left.add(page.getTitle().trim().toLowerCase());
         right.add(page.getTitle().trim().toLowerCase());

         if (matcher.find()) {

            Pattern wordpattern = Pattern.compile("\\|[NGDA][a-zA-Z\\s]+=.*?\\s(\\p{Upper}[\\p{L}]+)");
            Matcher wordmatcher = wordpattern.matcher(matcher.group());
            int end = 0;

            while (wordmatcher.find(end)) {
               String item = wordmatcher.group(1).toLowerCase();
               if (!left.contains(item)) {
                  left.add(item);
               }

               end = wordmatcher.end();
            }

         }

         callback.callback(left, right);
      }

   }

   @Override
   protected String getName() {
      return "Übersicht";
   }

}
