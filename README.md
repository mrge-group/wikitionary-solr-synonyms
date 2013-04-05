# wikitionary-solr-synonyms

## Usage

Example usage in `Main.java`.

	1. Implement a PageParser
	
	$ public class MyPageParser extends PageParser {
	$ 
	$ 	@Override
	$ 	public void parse(WikiPage page) {
	$ 		// TODO Auto-generated method stub
	$ 
	$ 	}
	$ 
	$ 	@Override
	$ 	protected String getName() {
	$ 		// TODO Auto-generated method stub
	$ 		return null;
	$ 	}
	$ 
	$ }
	
	2. Add PageParser to WikiParser and implement a callback
	
	$ WikiParser wp = new WikiParser();
	$ wp.addParser(new MyPageParser(new PageParserCallback() {
	$		
	$		@Override
	$		public void callback(List<String> left, List<String> right) {
	$			//do something
	$		}
	$	}));
	$
	$ // ...
	
	3. Parse!
	
	$ wp.parse("resources/dewiktionary-20121206-pages-meta-current.xml");
	
	
