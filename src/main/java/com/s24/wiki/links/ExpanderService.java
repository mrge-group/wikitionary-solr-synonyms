package com.s24.wiki.links;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpanderService {
   private Logger log = LoggerFactory.getLogger(ExpanderService.class);
   
   ExpanderDao expander;
   
   List<String> queries;
   
   public void init(File in) {
      try {
         queries = FileUtils.readLines(in);
      } catch (IOException e) {
         log.error("", e);
      }
   }
   
   public Map<String, Set<String>> expandKeywords() {
      return expander.expand(queries);
   }
}
