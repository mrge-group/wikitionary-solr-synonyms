package com.s24.wiki;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import edu.jhu.nlp.wikipedia.WikiPage;

public class GermanNounPageParser extends GermanPageParser {

   public GermanNounPageParser(PageParserCallback cb) {
      super(cb);
   }

   @Override
   public void parse(WikiPage page) {

      if (isValidPage(page)) {
         Pattern pattern = Pattern.compile("\\{\\{Wortart\\|Substantiv\\|Deutsch\\}\\}");
         Matcher matcher = pattern.matcher(page.getWikiText());
         
         if (matcher.find()) {
            callback.callback(Lists.newArrayList(page.getTitle()), null);
         }
      }

   }

   @Override
   protected String getName() {
      return "Nouns";
   }

}
