package com.koldbyte.filesearch.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class SearchContents {
	private String location;
	private String key;
	private int maxLevel = 2;
	private String fileExtensions = ".txt|.java";
	private Boolean caseSensitive = true;
	public Integer totalSearched = 0;
	public Integer totalFiles = 0;
	public Integer totalDirs = 0;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String getFileExts() {
		return fileExtensions;
	}

	public void setFileExts(String fileExts) {
		this.fileExtensions = fileExts;
	}

	public SearchContents() {
		super();
	}

	public SearchContents(String location, String key, int maxLevel) {
		super();
		this.location = location;
		this.key = key;
		this.maxLevel = maxLevel;
	}

	public SearchContents(String location, String key, int maxLevel,
			String fileExtensions, Boolean caseSensitive) {
		super();
		this.location = location;
		this.key = key;
		this.maxLevel = maxLevel;
		this.fileExtensions = fileExtensions;
		this.caseSensitive = caseSensitive;
	}

	public Map<File, List<String>> search() {
		Map<File, List<String>> results = new HashMap<File, List<String>>();
		Queue<File> queue = new LinkedList<File>();

		File root = new File(this.location);

		// System.out.println("Starting search in root: " + root.getName());

		if (root.isDirectory()) {
			// search in directory
			queue.addAll(Arrays.asList(root.listFiles()));
			while (!queue.isEmpty()) {
				File f = queue.poll();
				if (f.isDirectory()) {
					// System.out.println("Starting search in sub: " +
					// f.getName());
					this.totalDirs++;
					queue.addAll(Arrays.asList(f.listFiles()));
				} else {
					// search in file
					// System.out.println("Starting search in file: " +
					// f.getName());
					
					List<String> result = searchFile(f);
					this.totalFiles++;
					if (!result.isEmpty()) {
						results.put(f, result);
					}
				}
			}
		} else {
			// search in file
			List<String> result = searchFile(root);
			if (!result.isEmpty())
				results.put(root, result);
		}
		return results;
	}

	private List<String> searchFile(File f) {
		List<String> results = new ArrayList<String>();

		if (!f.isFile())
			return results;

		String ext = getFileExtension(f);
		if (ext.matches(this.fileExtensions)) {
			this.totalSearched++;
			// System.out.println("Starting search in matched file: " +
			// f.getName()); 
			try (BufferedReader in = new BufferedReader(new FileReader(f))) {
				String line = null;
				while ((line = in.readLine()) != null) {
					if (caseSensitive) {
						if (line.contains(this.key)) {
							results.add(line);
						}
					} else {
						if (line.toLowerCase().contains(this.key.toLowerCase())) {
							results.add(line);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return results;
	}

	public String printResult() {
		Map<File, List<String>> searchResults = this.search();
		String result = "";

		Iterator<Entry<File, List<String>>> it = searchResults.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<File, List<String>> pair = it.next();
			if (pair.getValue().size() > 0) {
				result += "Found " + pair.getValue().size() + " instances of "
						+ key + " in File: " + pair.getKey().getAbsolutePath()
						+ System.getProperty("line.separator");
				for (String res : pair.getValue()) {
					result += res + System.getProperty("line.separator");
				}
				result += "\n";
			}
			// System.out.println(pair.getKey() + " = " + pair.getValue());
		}

		return result;
	}

	private String getFileExtension(File file) {
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return name.substring(lastIndexOf);
	}

	public void writeResult(File f) {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(f))) {
			String result = this.printResult();
			out.write(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
