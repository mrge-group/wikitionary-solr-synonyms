package com.s24.wiki;
import java.util.List;

public interface PageParserCallback {

   void callback(List<String> left, List<String> right);
}
