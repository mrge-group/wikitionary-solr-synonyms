package com.s24.wiki;

import edu.jhu.nlp.wikipedia.WikiPage;

public class GermanWikiParser extends AbstractWikiParser {

   @Override
   protected boolean isNoun(WikiPage page) {
      return page.getWikiText().contains("{{Wortart|Substantiv|Deutsch}}") && page.getTitle().trim().length() > 3
            && !page.getTitle().contains(":") && !page.getTitle().contains("-")
            && !page.getTitle().trim().contains(" ");
   }
   
}
