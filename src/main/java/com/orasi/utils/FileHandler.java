package com.orasi.utils;

import java.io.File;
import java.io.IOException;

public class FileHandler {

	File file = null;
	
	/**
	 * Checks to see if a file exists
	 * Uses the file system path as a parameter
	 * @param path
	 * @return boolean
	 * @date 10/6/2016
	 */
	public boolean checkToSeeIfFileExists(String path){
		file = new File(path);
		return file.isFile();	
	}
	
	/**
	 * Creates a new file on the local system
	 * Uses the file system path as a parameter
	 * Will only create the file if doesn't exist already
	 * @param path
	 * @date 10/6/2016
	 */
	public void createNewFile(String path){
		file = new File(path);
		if (!file.isFile()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				TestReporter.log(e.getStackTrace().toString());
				throw new RuntimeException("Could not create file");
			}
		}
		
	}
	
	/**
	 * Deletes a file on the local system
	 * Uses the file system path as a parameter
	 * @param path
	 * @date 10/6/2016
	 */
	public void deleteFile(String path){
		file = new File(path);
		file.delete();
	}
}
