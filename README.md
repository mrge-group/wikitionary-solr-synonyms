# wikitionary-solr-synonyms

Parser for wiktionary files that creates 2 files `stem.txt` (german stemming) and `subword.txt` 
(transitive subwords) usable for solr synonym configuration.

Stemming:
```
pullover,pullovers,pullovern => pullover
```

Subwords:
```
pullover,pullovers,pullovern => pullover,pullovern,pullovers,strickpullover,sweatshirt,wollpullover
```

Nouns:
```
insert into lists (type, entry, modified_by, modified_at, active) values ('noun-wiktionary-de', 'Aachen','SomeUserName',now(),true);
```

## Wiktionary download

Download `dewiktionary-<DATE>-pages-meta-current.xml` (or other language) from http://dumps.wikimedia.org/backup-index.html

## Usage

    $ mvn clean package
    $ java -jar target/wikitionary-solr-synonyms-0.0.1-SNAPSHOT-jar-with-dependencies.jar ./dewiktionary-20150315-pages-meta-current.xml 

## License

This project is licensed under the [Apache License, Version 2](http://www.apache.org/licenses/LICENSE-2.0.html).

