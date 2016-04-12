package com.s24.wiki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.jhu.nlp.wikipedia.WikiPage;

public abstract class GermanPageParser extends PageParser {
   
   public GermanPageParser(PageParserCallback cb) {
      super(cb);
   }

   protected boolean isValidPage(WikiPage page) {
      Pattern pattern = Pattern.compile("\\{\\{Überarbeiten\\|[^\\}]*" + getName());
      Matcher matcher = pattern.matcher(page.getWikiText());
      return !matcher.find();
   }

}
