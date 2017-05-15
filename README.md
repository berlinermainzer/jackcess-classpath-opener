# jackcess-classpath-opener
Custom implementation of the JackcessOpenerInterface which can load files from classpath.

Tries to load files from classpath (e.g. provided in a jar file) if given filename is prefixed with classpath. Enable the custom opener by adding the jackcessOpener property to the Ucanaccess url:
  jdbc:ucanaccess://classpath:SOME_FILENAME;jackcessOpener=de.felten.classpathopener.ClasspathAwareMdbOpener
