package com.s24.wiki;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WikiApi {

   public Set<String> query(String page, String[] attributes) {

      SortedSet<String> result = new TreeSet<String>();

      result.add(nomalizeValue(page));

      try {

         URL url = new URL("http://de.wikipedia.org/w/api.php?action=parse&format=xml&page="
               + URLEncoder.encode(page, "UTF-8") + "&prop=links|" + StringUtils.join(attributes, '|'));

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(url.openStream());

         NodeList links = doc.getElementsByTagName("links");

         if (links.getLength() == 1 && links.item(0).getChildNodes().getLength() == 1) {
            result.addAll(query(links.item(0).getChildNodes().item(0).getTextContent(), attributes));
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

      return result;

   }

   private String nomalizeValue(String str) {
      return str.replace('_', ' ').replaceAll("\\(.*?\\)", "").trim();
   }

}
