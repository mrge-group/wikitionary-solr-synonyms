import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;



public class Main {

	private static final String stem = "stem.txt";	
	
	private static final String subword = "subword.txt";	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {


		WikiParser wp = new WikiParser();
		
		final File stemout = new File(stem);
		final File subwordout = new File(subword);

		final Map<String,Pair<List<String>,List<String>>> subwortmap = new TreeMap<String,Pair<List<String>,List<String>>>();
		final Map<String,String> stemmap = new TreeMap<String,String>();
		
		wp.addParser(new SubwordPageParser(new PageParserCallback() {
			
			@Override
			public void callback(List<String> left, List<String> right) {
				
				if (left.size() < right.size())
				{
					subwortmap.put(left.get(0), Pair.of(left, right));
				}
			}
		}));
		
		wp.addParser(new GrammarPageParser(new PageParserCallback() {
			
			@Override
			public void callback(List<String> left, List<String> right) {
				
				if (left.size() > right.size())
				{
					stemmap.put(StringUtils.join(left, ","), StringUtils.join(right, ","));
				}
			}
		}));
		
		wp.parse("resources/dewiktionary-20121206-pages-meta-current.xml");
		
		try {
			
			SortedSet<String> keys = new TreeSet<String>(subwortmap.keySet());
			
			subwordout.createNewFile();
			
			for (String key : keys) { 
				Pair<List<String>,List<String>> pair = subwortmap.get(key);
				List<String> right = pair.getRight();
				
				SortedSet<String> newright = new TreeSet<String>(right);
				
				Iterator<String> iterator = right.iterator();
				while (iterator.hasNext()) {
					// is subword a generic word ?
					String subword = iterator.next();
					
					if (keys.contains(subword))
					{
						Pair<List<String>,List<String>> subpair = subwortmap.get(subword);
						newright.addAll(subpair.getRight());
					}
				}
				
				FileUtils.writeStringToFile(subwordout, StringUtils.join(pair.getLeft(), ",") + " => " + StringUtils.join(newright, ",") + "\n", "utf-8", true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
			SortedSet<String> keys = new TreeSet<String>(stemmap.keySet());
			
			stemout.createNewFile();
			
			for (String key : keys) { 
				FileUtils.writeStringToFile(stemout, key + " => " + stemmap.get(key) + "\n", "utf-8", true);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
