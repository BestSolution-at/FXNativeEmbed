/**
 * Copyright (C) 2021 - BestSolution.at
 */
package at.bestsolution.fxembed.swing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Allows to load a native library after extracting it from fat jar
 */
public class NativeLibraryLoader {
	
	private NativeLibraryLoader() {
		
	}
	
	public static void load(String library, String path) throws IOException {
		load(FXEmbed.class, library, path);
	}
	
	public static void load(Class<?> requestor, String library, String path) throws IOException {
		String name = System.mapLibraryName(library);
		if (path == null) {
			path = "./";
		}
		String resourcePath = path + "/" + name;
		try (InputStream in = requestor.getResourceAsStream(resourcePath)) {
			Path extractPath = Paths.get(System.getProperty("java.io.tmpdir")).resolve(library + "/" + resourcePath);
			extract(in, extractPath);
			System.load(extractPath.toAbsolutePath().toString());
		} 
	}

	private static void extract(InputStream inStream, Path extractPath) throws IOException {
		Files.createDirectories(extractPath.getParent());
		if (!extractPath.toFile().exists()) {
			long bytes = Files.copy(inStream, extractPath);
			if (bytes == 0) {
				throw new IOException("Library file size is 0");
			}
		}
	}

}
