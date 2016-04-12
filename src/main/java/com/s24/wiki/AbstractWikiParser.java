package com.s24.wiki;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public abstract class AbstractWikiParser {

   List<PageParser> parser;

   public AbstractWikiParser() {
      parser = new ArrayList<PageParser>();
   }

   public void parse(String filename) {
      try {
         WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(filename);
         wxsp.setPageCallback(new PageCallbackHandler() {
            public void process(WikiPage page) {
               if (!page.isSpecialPage() && !page.isRedirect() && isNoun(page)) {
                  Iterator<PageParser> iter = parser.iterator();

                  while (iter.hasNext()) {
                     iter.next().parse(page);
                  }

               }

            }
         });

         wxsp.parse();

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void addParser(PageParser p) {
      parser.add(p);
   }

   protected abstract boolean isNoun(WikiPage page);

}
