# wikitionary-solr-synonyms

Parser for wiktionary files.

Execute `SolrSynomyms.java` for current implementation. Current implementation creates 2 files `stem.txt` (german stemming) and `subword.txt` (transitive subwords) for solr configuration.

Stemming:
```
...
pullover,pullovers,pullovern => pullover
...
```

Subwords:
```
...
pullover,pullovers,pullovern => pullover,pullovern,pullovers,strickpullover,sweatshirt,wollpullover
...
```


## Wiktionary download

Download `dewiktionary-20130321-pages-meta-current.xml` (or other language) from http://dumps.wikimedia.org/backup-index.html

## Usage

Example usage in `SolrSynomyms.java`.

1. Implement a PageParser

```java	
 public class MyPageParser extends PageParser {
 
 	@Override
 	public void parse(WikiPage page) {
 		// TODO Auto-generated method stub
 
 	}
 
 	@Override
 	protected String getName() {
 		// TODO Auto-generated method stub
 		return null;
 	}
 
 }
```	

2. Add PageParser to WikiParser and implement a callback
	
```java
 WikiParser wp = new WikiParser();
 wp.addParser(new MyPageParser(new PageParserCallback() {
		
		@Override
		public void callback(List<String> left, List<String> right) {
			//do something
		}
	}));
```	
	
3. Parse!

```java	
 wp.parse("resources/dewiktionary-20121206-pages-meta-current.xml");
```		
	
