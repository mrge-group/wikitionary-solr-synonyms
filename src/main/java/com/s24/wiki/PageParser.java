package com.s24.wiki;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.jhu.nlp.wikipedia.WikiPage;

public abstract class PageParser {

   PageParserCallback callback;

   public PageParser(PageParserCallback cb) {
      callback = cb;
   }

   abstract public void parse(WikiPage page);

   abstract protected String getName();

   protected boolean isValidPage(WikiPage page) {
      Pattern pattern = Pattern.compile("\\{\\{Überarbeiten\\|[^\\}]*" + getName());
      Matcher matcher = pattern.matcher(page.getWikiText());
      return !matcher.find();

   }

}
