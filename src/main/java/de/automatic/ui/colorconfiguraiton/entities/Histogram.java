package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Collections;


public class Histogram {

	private ArrayList<RgbPixel> pixelList;

	public Histogram() {
		pixelList = new ArrayList<RgbPixel>();
	}

	public Histogram(ArrayList<RgbPixel> histogram) {
		this.pixelList = histogram;
	}
	
	public void sort(Channel c) {
		switch (c) {
		case R:
			Collections.sort(pixelList, new Comparator<RgbPixel>(){
		        public int compare(RgbPixel  p1, RgbPixel  p2) {
		        	if (p1.getC1() > p2.getC1()) {
		        		return 1;
		        	} else if(p1.getC1() == p2.getC1()) {
		        		return 0;
		        	} else {
		        		return -1;
		        	}
		        }
			});
			break;
		case G:
			Collections.sort(pixelList, new Comparator<RgbPixel>(){
		        public int compare(RgbPixel  p1, RgbPixel  p2) {
		        	if (p1.getC2() > p2.getC2()) {
		        		return 1;
		        	} else if(p1.getC2() == p2.getC2()) {
		        		return 0;
		        	} else {
		        		return -1;
		        	}
		        }
			});
			break;
		case B:
			Collections.sort(pixelList, new Comparator<RgbPixel>(){
		        public int compare(RgbPixel  p1, RgbPixel  p2) {
		        	if (p1.getC3() > p2.getC3()) {
		        		return 1;
		        	} else if(p1.getC3() == p2.getC3()) {
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
	
	public void add(RgbPixel p) {
		this.pixelList.add(p);
	}
	
	public int getLength() {
		return pixelList.size();
	}
	
	public int getCountOfPixels() {
		int count = 0;
		for (RgbPixel p : pixelList) {
			count+=p.getCount();
		}
		return count;
	}
	
	public RgbPixel get(int index){
		return pixelList.get(index);
	}

	public ArrayList<RgbPixel> getPixelList() {
		return pixelList;
	}

	public void setHistogram(ArrayList<RgbPixel> histogram) {
		this.pixelList = histogram;

	}
	
	public void clear(){
		this.pixelList.clear();
	}

}
