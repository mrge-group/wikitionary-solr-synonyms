package com.s24.wiki.links;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExpanderDao {
   Set<String> expand(String keyword);

   Map<String, Set<String>> expand(List<String> queries);
}
