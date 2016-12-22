package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Collections;


public class Histogram {

	private ArrayList<RgbSample> pixelList;

	public Histogram() {
		pixelList = new ArrayList<RgbSample>();
	}

	public Histogram(ArrayList<RgbSample> histogram) {
		this.pixelList = histogram;
	}
	
	public void sort(RgbChannels c) {
		switch (c) {
		case R:
			Collections.sort(pixelList, new Comparator<RgbSample>(){
		        public int compare(RgbSample  p1, RgbSample  p2) {
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
			Collections.sort(pixelList, new Comparator<RgbSample>(){
		        public int compare(RgbSample  p1, RgbSample  p2) {
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
			Collections.sort(pixelList, new Comparator<RgbSample>(){
		        public int compare(RgbSample  p1, RgbSample  p2) {
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
	
	public void add(RgbSample p) {
		this.pixelList.add(p);
	}
	
	public int getLength() {
		return pixelList.size();
	}
	
	public int getCountOfPixels() {
		int count = 0;
		for (RgbSample p : pixelList) {
			count+=p.getCount();
		}
		return count;
	}
	
	public RgbSample get(int index){
		return pixelList.get(index);
	}

	public ArrayList<RgbSample> getPixelList() {
		return pixelList;
	}

	public void setHistogram(ArrayList<RgbSample> histogram) {
		this.pixelList = histogram;

	}
	
	public void clear(){
		this.pixelList.clear();
	}

}
