## text-statistics

Command line application that will display text-statistics for a given set of texts

### requirements

- JDK: Java 8 (min)

### build

- unix: `./gradlew build`
- windows: `gradlew.bat build`

### run

Running the application, traditional way:

- `java -jar build/libs/text-statistics.jar https://www.gutenberg.org/cache/epub/66409/pg66409.txt`

Running the application, SpringBoot (bootRun) gradle task:

- unix: `./gradlew bootRun --args=https://www.gutenberg.org/cache/epub/66409/pg66409.txt`
- windows: `gradlew.bat bootRun --args=https://www.gutenberg.org/cache/epub/66409/pg66409.txt`

Output example:
```
========================================================================================
Resource: https://www.gutenberg.org/cache/epub/66409/pg66409.txt
Number of words:           65769
Number of lines:            7819
----------------------------------------------------------------------------------------
10 longest words: [indistinguishable, objectionableness, microlepidoptera, incomprehensible, 
misunderstanding, unenforceability, congratulations, undemonstrative, unconsciousness, phosphorescence]
----------------------------------------------------------------------------------------
20 most frequent words: [he (1779), was (1494), that (1444), his (1110), had (761), with (713), my (582), 
you (578), for (562), me (562), said (557), but (551), mr (459), this (454), him (443), all (426), 
not (420), there (369), were (330), have (324)]
```

### config

All configuration properties are in `application.properties` file

### notes

- Some explanations can be found as comments in source code (ex.: the reason to have `process.skip.words`)
- Parameters can be absolute file paths or URLs (http, ftp, etc.)
- Summary will print only if there are 2 or more valid resources as parameters
- Supported input format: plain text, to support others such as PDF, HTML, Json, XML, etc., additional libraries need to be added (PDFBox, Apache POI, JSoup, etc.)
