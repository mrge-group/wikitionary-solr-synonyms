package com.s24.wiki;
import edu.jhu.nlp.wikipedia.WikiPage;

public abstract class PageParser {

   PageParserCallback callback;

   public PageParser(PageParserCallback cb) {
      callback = cb;
   }

   abstract public void parse(WikiPage page);

   abstract protected String getName();

   protected boolean isValidPage(WikiPage page) {
      return true;
   }

}
