package de.automatic.ui.colorconfiguraiton.entities;

public interface Sample {

	public double get(Channels c);
	
	public double getNormalized(Channels c);

	public double getC1();
	
	public double getC1Normalized();

	public void setC1(double r);

	public double getC2();
	
	public double getC2Normalized();

	public void setC2(double g);

	public double getC3();
	
	public double getC3Normalized();

	public void setC3(double b);

	public int getCount();

	public void setCount(int count);

}
