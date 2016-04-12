package com.s24.wiki;

import edu.jhu.nlp.wikipedia.WikiPage;

public class EnglishWikiParser extends AbstractWikiParser {

   @Override
   protected boolean isNoun(WikiPage page) {
      return page.getWikiText().contains("{{en-noun") 
            && page.getTitle().trim().length() > 2
            && !page.getTitle().contains(":") && !page.getTitle().contains("-")
            && !page.getTitle().trim().contains(" ");
   }
   
}
