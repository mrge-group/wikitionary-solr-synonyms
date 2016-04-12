package com.s24.wiki;

import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import edu.jhu.nlp.wikipedia.WikiPage;

@RunWith(MockitoJUnitRunner.class)
public class EnglishGrammarPageParserTest {

   @Mock
   private PageParserCallback callback;
   
   private EnglishGrammarPageParser parser;
   
   @Before
   public void setUp() throws Exception {
      parser = new EnglishGrammarPageParser(callback);
   }
   
   @Test
   public void testWife() throws Exception {
      String wife = CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream("/wife.txt"), Charsets.UTF_8));
      WikiPage p = new WikiPage();
      p.setTitle("wife");
      p.setWikiText(wife);
      
      parser.parse(p);
   }

   @Test
   public void testTreaty() throws Exception {
      String wife = CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream("/treaty.txt"), Charsets.UTF_8));
      WikiPage p = new WikiPage();
      p.setTitle("treaty");
      p.setWikiText(wife);
      
      parser.parse(p);
   }

   @Test
   public void testApple() throws Exception {
      String wife = CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream("/apple.txt"), Charsets.UTF_8));
      WikiPage p = new WikiPage();
      p.setTitle("apple");
      p.setWikiText(wife);
      
      parser.parse(p);
   }

   @Test
   public void testBridge() throws Exception {
      String wife = CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream("/bridge.txt"), Charsets.UTF_8));
      WikiPage p = new WikiPage();
      p.setTitle("bridge");
      p.setWikiText(wife);
      
      parser.parse(p);
   }
   

}
