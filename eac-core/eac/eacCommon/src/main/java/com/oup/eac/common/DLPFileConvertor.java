package com.oup.eac.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.oup.eac.common.utils.lang.EACStringUtils;

public class DLPFileConvertor {

	private static final String INPUT_DIR = "C:\\Users\\harlandd\\Desktop\\tokens\\in";
	private static final String OUTPUT_DIR = "C:\\Users\\harlandd\\Desktop\\tokens\\out";
	
	public static void main(String[] args) throws IOException {
		File inDir = new File(INPUT_DIR);
		for(String fileName : inDir.list()){
			System.out.println("Processing file :" + fileName);
			BufferedReader in = new BufferedReader(new FileReader(INPUT_DIR + "\\" + fileName));
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(OUTPUT_DIR + "\\" + fileName)));
			String line = in.readLine();
			int count = 0;
			while(line != null) {
				if(count > 0) {
					out.write("\r\n");
				}
				out.write(EACStringUtils.format(line, 4, '-'));
				count++;
				line = in.readLine();
			}
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(in);
		}
	}
}
