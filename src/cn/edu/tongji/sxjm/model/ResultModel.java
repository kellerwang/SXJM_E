package cn.edu.tongji.sxjm.model;

public class ResultModel {
	protected int mileage;
	protected double daur;

	public ResultModel() {
		super();
	}

	public ResultModel(int mileage, double daur) {
		super();
		this.mileage = mileage;
		this.daur = daur;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public double getDaur() {
		return daur;
	}

	public void setDaur(double daur) {
		this.daur = daur;
	}

}
