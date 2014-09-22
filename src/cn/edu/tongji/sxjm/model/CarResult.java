package cn.edu.tongji.sxjm.model;

import cn.edu.tongji.sxjm.process.AllocationCar;

public class CarResult {
	private int numCarCarry_1_1 = 0;
	private int numCarCarry_1_2 = 0;

	private int mileage = -1;

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public CarResult(int numCarCarry_1_1, int numCarCarry_1_2) {
		super();
		this.numCarCarry_1_1 = numCarCarry_1_1;
		this.numCarCarry_1_2 = numCarCarry_1_2;
	}

	public boolean isSatisfyOptimalCarNum(CarResult optimal, int planSelected) {
		boolean result = true;
		switch (planSelected) {
		case AllocationCar.typeCarCarry_1_1:
			if ((numCarCarry_1_1 + 1) > optimal.numCarCarry_1_1) {
				result = false;
			}
			break;
		case AllocationCar.typeCarCarry_1_2:
			if ((numCarCarry_1_2 + 1) > optimal.numCarCarry_1_2) {
				result = false;
			}
			break;
		}
		return result;
	}

	public int getNumCarCarry_1_1() {
		return numCarCarry_1_1;
	}

	public void setNumCarCarry_1_1(int numCarCarry_1_1) {
		this.numCarCarry_1_1 = numCarCarry_1_1;
	}

	public int getNumCarCarry_1_2() {
		return numCarCarry_1_2;
	}

	public void setNumCarCarry_1_2(int numCarCarry_1_2) {
		this.numCarCarry_1_2 = numCarCarry_1_2;
	}
}
