package de.felten.classpathopener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.util.MemFileChannel;

import net.ucanaccess.jdbc.DefaultJackcessOpener;
import net.ucanaccess.jdbc.JackcessOpenerInterface;

/**
 * Custom implementation of the {@link JackcessOpenerInterface} which can load files from classpath. Tries to load from
 * classpath if given filename is prefixed with <code>classpath:</code>. Ucanacces db url must contain property
 * <code>jackcessOpener</code> to make use of this class: <pre>jdbc:ucanaccess://SOME_FILENAME;jackcessOpener=de.felten.classpathopener.ClasspathAwareMdbOpener</pre>
 */
public class ClasspathAwareMdbOpener implements JackcessOpenerInterface {

	private Logger logger = LoggerFactory.getLogger(ClasspathAwareMdbOpener.class);

	private final static String CP_PREFIX = "classpath:";

	@Override
	public Database open(File fl, String pwd) throws IOException {
		if (fl == null) {
			throw new IOException("File must not be null.");
		}

		if (fl.getPath().startsWith(CP_PREFIX)) {
			String path = fl.getPath().substring(CP_PREFIX.length());
			logger.debug("Loading file '{}' from classpath.", path);

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream in = classLoader.getResourceAsStream(path);
			MemFileChannel ch = MemFileChannel.newChannel(in);
			DatabaseBuilder dbBuilder = new DatabaseBuilder();
			dbBuilder.setChannel(ch);

			return dbBuilder.open();
		} else {
			logger.debug("Loading file '{}' from filesystem.", fl);
			DefaultJackcessOpener fileOpener = new DefaultJackcessOpener();
			return fileOpener.open(fl, pwd);
		}
	}
}
