package com.koldbyte.filesearch.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunnableSearchSingleFile implements Runnable {

	// input
	private String fileExtensions = ".txt|.java";
	private Boolean caseSensitive = true;
	private String key = "";
	private File file = null;

	// output
	private List<String> results = null;

	// status
	public Boolean isFinished = false;

	@Override
	public void run() {
		results = searchFile(file);
		isFinished = true;
	}

	private List<String> searchFile(File f) {
		List<String> results = new ArrayList<String>();

		if (f == null || !f.isFile())
			return results;

		// this.totalSearched++;
		System.out.println("Starting search in matched file: " + f.getName());
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

		return results;
	}


	public String getFileExtensions() {
		return fileExtensions;
	}

	public void setFileExtensions(String fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public Boolean getCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(Boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<String> getResults() {
		return results;
	}

	public void setResults(List<String> results) {
		this.results = results;
	}

	public RunnableSearchSingleFile() {
		super();
	}

	public RunnableSearchSingleFile(Boolean caseSensitive, String key, File file) {
		super();
		this.caseSensitive = caseSensitive;
		this.key = key;
		this.file = file;
	}

}
