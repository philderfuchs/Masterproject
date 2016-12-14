package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Collections;


public class Histogram {

	private ArrayList<Pixel> pixelList;
	private HashSet<Integer> intValues;

	public Histogram() {
		pixelList = new ArrayList<Pixel>();
		intValues = new HashSet<Integer>();
	}

	public Histogram(ArrayList<Pixel> histogram) {
		this.pixelList = histogram;
		intValues = new HashSet<Integer>();
		for(Pixel p : histogram) {
			intValues.add(p.getRgb());
		}
	}
	
	public boolean contains(int rgb) {
		return intValues.contains(rgb);
	}
	
	public void sort(Channels c) {
		switch (c) {
		case R:
			Collections.sort(pixelList, new Comparator<Pixel>(){
		        public int compare(Pixel  p1, Pixel  p2) {
		        	if (p1.getR() > p2.getR()) {
		        		return 1;
		        	} else if(p1.getR() == p2.getR()) {
		        		return 0;
		        	} else {
		        		return -1;
		        	}
		        }
			});
			break;
		case G:
			Collections.sort(pixelList, new Comparator<Pixel>(){
		        public int compare(Pixel  p1, Pixel  p2) {
		        	if (p1.getG() > p2.getG()) {
		        		return 1;
		        	} else if(p1.getG() == p2.getG()) {
		        		return 0;
		        	} else {
		        		return -1;
		        	}
		        }
			});
			break;
		case B:
			Collections.sort(pixelList, new Comparator<Pixel>(){
		        public int compare(Pixel  p1, Pixel  p2) {
		        	if (p1.getB() > p2.getB()) {
		        		return 1;
		        	} else if(p1.getB() == p2.getB()) {
		        		return 0;
		        	} else {
		        		return -1;
		        	}
		        }
			});
			break;
		default:
			break;
		}
	}
	
	public void add(Pixel p) {
		this.pixelList.add(p);
		intValues.add(p.getRgb());
	}
	
	public int getLength() {
		return pixelList.size();
	}
	
	public int getCountOfPixels() {
		int count = 0;
		for (Pixel p : pixelList) {
			count+=p.getCount();
		}
		return count;
	}
	
	public Pixel get(int index){
		return pixelList.get(index);
	}

	public ArrayList<Pixel> getPixelList() {
		return pixelList;
	}

	public void setHistogram(ArrayList<Pixel> histogram) {
		this.pixelList = histogram;
		intValues = new HashSet<Integer>();
		for(Pixel p : histogram) {
			intValues.add(p.getRgb());
		}
	}
	
	public void clear(){
		this.pixelList.clear();
		this.intValues.clear();
	}

}
