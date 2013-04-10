package com.s24.wiki.links;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class WikipediaExpanderDao implements ExpanderDao {
   private Logger log = LoggerFactory.getLogger(WikipediaExpanderDao.class);
   
   public Set<String> expand(String keyword) {
      SortedSet<String> result = new TreeSet<String>();
      result.add(nomalizeValue(keyword));

      try {
         List<String> attributes = Lists.newArrayList("links", "categories");
         
         URL url = new URL("http://de.wikipedia.org/w/api.php?action=parse&format=xml&page="
               + URLEncoder.encode(keyword, "UTF-8") + "&prop=" + StringUtils.join(attributes , '|'));

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(url.openStream());

         NodeList links = doc.getElementsByTagName("links");

         if (links.getLength() == 1 && links.item(0).getChildNodes().getLength() == 1) {
            result.addAll(expand(links.item(0).getChildNodes().item(0).getTextContent()));
         }

         for (String attribute : attributes) {
            NodeList parent = doc.getElementsByTagName(attribute);

            if (parent.getLength() == 1) {
               NodeList children = parent.item(0).getChildNodes();

               for (int i = 0; i < children.getLength(); i++) {
                  Node node = children.item(i);
                  result.add(nomalizeValue(node.getTextContent()));
               }
            }
         }
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      }
      return filterResult(result);
   }

   private String nomalizeValue(String str) {
      return str.replace('_', ' ').replaceAll("\\(.*?\\)", "").trim();
   }
   
   private Set<String> filterResult(final Set<String> result) {
      return Sets.filter(result, new Predicate<String>() {
         @Override
         public boolean apply(String input) {
            return !input.contains(":");
         }
      });
   }

   private boolean isDefinition(Set<String> result) {
      return result.contains("Wikipedia:Begriffsklärung");
   }

   @Override
   public Map<String, Set<String>> expand(List<String> queries) {
      Map<String, Set<String>> terms = Maps.newHashMap();
      for (String query : queries) {
         Set<String> result = expand(query);
         // found a page && is not a definition page
         if (result.size() > 1 && !isDefinition(result)) {
            result = filterResult(result);
            terms.put(query, result);
            log.info(query + " -> " + StringUtils.join(result, "|"));
         }
      }
      return terms;
   }
}
