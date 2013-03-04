package com.clican.appletv.service;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import junit.framework.TestCase;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.MovieBox;
import com.googlecode.mp4parser.AbstractContainerBox;

/**
 * 223683537 1152209
 * 
 * @author weizha
 * 
 */
public class Mp4TestCase extends TestCase {

	public void testParseMp4FileTypeHeader() throws Exception {
		FileChannel fc = new FileInputStream("c:/file.mp4").getChannel();
		IsoFile isoFile = new IsoFile(fc);
		for (Box box : isoFile.getBoxes()) {
			System.out.println(box);
		}
		FileTypeBox ftb = isoFile.getBoxes(FileTypeBox.class).get(0);
		InputStream is = new FileInputStream("c:/file.mp4");
		DataInputStream in = new DataInputStream(is);
		int fileTypeHeaderSize = in.readInt();
		System.out.println("fileTypeHeaderSize="+fileTypeHeaderSize);
		in.skipBytes(fileTypeHeaderSize-4);
		int moovHeaderSize = in.readInt();
		System.out.println("moovHeaderSize="+moovHeaderSize);
		in.skipBytes(moovHeaderSize-4);
		int mdatHeaderSize = in.readInt();
		System.out.println("mdatHeaderSize="+mdatHeaderSize);
		in.skipBytes(mdatHeaderSize-4);
		int freeHeaderSize = in.readInt();
		System.out.println("freeHeaderSize="+freeHeaderSize);
		in.skipBytes(freeHeaderSize-4);
		
	}

	public void testParseMp4MoviceBoxHeader() throws Exception {
		FileChannel fc = new FileInputStream("c:/file.mp4").getChannel();
		IsoFile isoFile = new IsoFile(fc);
		for(Box box:isoFile.getBoxes()){
			if(box instanceof AbstractContainerBox){
				dumpBoxSize((AbstractContainerBox)box,"");
			}else{
				System.out.println(box.getType() + ",size=" + box.getSize());
			}
		}
		
	}

	private void dumpBoxSize(AbstractContainerBox parent,String offset) throws Exception {
		for (Box box : parent.getBoxes()) {
			System.out.println(offset+box.getType() + ",size=" + box.getSize());
			if (box instanceof AbstractContainerBox) {
				this.dumpBoxSize((AbstractContainerBox) box,offset+"\t");
			}
		}
	}

}
