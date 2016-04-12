package com.s24.wiki;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.jhu.nlp.wikipedia.WikiPage;

public class GermanSubwordPageParser extends GermanPageParser {

   public GermanSubwordPageParser(PageParserCallback cb) {
      super(cb);
   }

   @Override
   public void parse(WikiPage page) {

      if (isValidPage(page)) {
         Pattern pattern = Pattern.compile("\\{\\{Unterbegriffe\\}\\}\n(:.*\n)+");
         Matcher matcher = pattern.matcher(page.getWikiText());
         List<String> right = new ArrayList<String>();
         final List<String> left = new ArrayList<String>();

         PageParser grammar = new GermanGrammarPageParser(new PageParserCallback() {

            @Override
            public void callback(List<String> l, List<String> r) {
               left.addAll(l);
            }
         });

         grammar.parse(page);
         right.addAll(left);

         if (matcher.find()) {

            Pattern wordpattern = Pattern.compile("\\[\\[(\\p{Upper}[\\p{L}]+)\\]\\]");
            Matcher wordmatcher = wordpattern.matcher(matcher.group());

            int end = 0;

            while (wordmatcher.find(end)) {
               String item = wordmatcher.group(1).toLowerCase();
               if (!right.contains(item)) {
                  right.add(item);
               }

               end = wordmatcher.end();
            }
         }

         callback.callback(left, right);
      }

   }

   @Override
   protected String getName() {
      return "Unterbegriffe";
   }

}
