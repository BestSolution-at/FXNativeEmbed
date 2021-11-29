/**
 * Copyright (C) 2021 - BestSolution.at
 */
package at.bestsolution.fxembed.swing;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class VersionInfoUtil {

	private static Properties properties;

	private VersionInfoUtil() {

	}

	private static Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			try (InputStream in = VersionInfoUtil.class.getResourceAsStream("version-info.properties")) { //$NON-NLS-1$
				properties.load(in);
			} catch (Exception e) {
				System.err.println("could not load version-info. Looks like the jar is corrupted!");
			}
		}
		return properties;
	}

	/**
	 * @return the version
	 */
	public static String getVersion() {
		return Objects.toString(getProperties().get("version"), "-");
	}

	/**
	 * @return the build timestamp
	 */
	public static String getBuildTimestamp() {
		return Objects.toString(getProperties().get("build.timestamp"), "-");
	}

}
