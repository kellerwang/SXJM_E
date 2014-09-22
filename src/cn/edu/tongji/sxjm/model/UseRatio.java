package cn.edu.tongji.sxjm.model;

import java.util.ArrayList;
import java.util.List;

public class UseRatio implements Comparable {

	private int x_I, x_II, x_III;
	private double userRatio;
	private int numCarCarrier;
	private int num = 1;
	private RequirementModel layer1 = new RequirementModel(0, 0, 0);
	private RequirementModel layer2 = new RequirementModel(0, 0, 0);;
	private RequirementModel layer3 = new RequirementModel(0, 0, 0);;
	private RequirementModel layer4 = new RequirementModel(0, 0, 0);;

	private String end = "";
	List<String> pass = new ArrayList<String>();

	public void setPass(List<String> pass) {
		this.pass = pass;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public List<String> getPass() {
		return pass;
	}

	public UseRatio(int x_I, int x_II, int x_III, double userRatio,
			int numCarCarrier, RequirementModel layer1) {
		super();
		this.x_I = x_I;
		this.x_II = x_II;
		this.x_III = x_III;
		this.userRatio = userRatio;
		this.numCarCarrier = numCarCarrier;
		this.layer1 = layer1;
	}

	public RequirementModel getLayer1() {
		return layer1;
	}

	public void setLayer1(RequirementModel layer1) {
		this.layer1 = layer1;
	}

	public RequirementModel getLayer2() {
		return layer2;
	}

	public void setLayer2(RequirementModel layer2) {
		this.layer2 = layer2;
	}

	public RequirementModel getLayer3() {
		return layer3;
	}

	public void setLayer3(RequirementModel layer3) {
		this.layer3 = layer3;
	}

	public RequirementModel getLayer4() {
		return layer4;
	}

	public void setLayer4(RequirementModel layer4) {
		this.layer4 = layer4;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public UseRatio(int x_I, int x_II, int x_III, double userRatio,
			int numCarCarrier) {
		super();
		this.x_I = x_I;
		this.x_II = x_II;
		this.x_III = x_III;
		this.userRatio = userRatio;
		this.numCarCarrier = numCarCarrier;
	}

	public UseRatio(int x_I, int x_II, int x_III, int numCarCarrier) {
		super();
		this.x_I = x_I;
		this.x_II = x_II;
		this.x_III = x_III;
		this.numCarCarrier = numCarCarrier;
	}

	public UseRatio(int x_I, int x_II, int x_III) {
		super();
		this.x_I = x_I;
		this.x_II = x_II;
		this.x_III = x_III;
	}

	public UseRatio(RequirementModel other, int numCarCarrier) {
		super();
		this.x_I = other.getX_I();
		this.x_II = other.getX_II();
		this.x_III = other.getX_III();
		this.numCarCarrier = numCarCarrier;
	}

	public UseRatio(RequirementModel other) {
		super();
		this.x_I = other.getX_I();
		this.x_II = other.getX_II();
		this.x_III = other.getX_III();
	}

	// 返回RequirementModel类型参数
	public RequirementModel getRm() {
		return new RequirementModel(this.x_I, this.x_II, this.x_III);
	}

	// 判断是否分配完成
	public boolean isFinished() {
		return (this.x_I == 0 && this.x_II == 0 && this.x_III == 0) ? true
				: false;
	}

	// 判断是否能转载（进考虑下层或者不考虑III型车）
	public boolean isBestHoldPlan(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UseRatio other = (UseRatio) obj;
		if (numCarCarrier != other.numCarCarrier)
			return false;
		if (x_I <= other.x_I && x_II <= other.x_II && x_III <= other.x_III) {
			return true;
		} else {
			return false;
		}
	}

	// 判断是否能装载
	public boolean isBestHoldPlan2(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UseRatio other = (UseRatio) obj;
		if (numCarCarrier != other.numCarCarrier)
			return false;
		if (x_I <= other.x_I && x_II <= other.x_II && x_III == other.x_III) {
			return true;
		} else {
			return false;
		}
	}

	// 判断是否能转载(默认车型相同)
	public boolean isBestHoldPlan3(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UseRatio other = (UseRatio) obj;
		if (x_I <= other.x_I && x_II <= other.x_II && x_III <= other.x_III) {
			return true;
		} else {
			return false;
		}
	}

	// 判断是否正好完全转载(默认车型相同)
	public boolean isWholeHoldPlan(UseRatio other) {
		if (other == null)
			return false;
		if (x_I == other.x_I && x_II == other.x_II && x_III == other.x_III) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x_I;
		result = prime * result + x_II;
		result = prime * result + x_III;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UseRatio other = (UseRatio) obj;
		if (x_I != other.x_I)
			return false;
		if (x_II != other.x_II)
			return false;
		if (x_III != other.x_III)
			return false;
		return true;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return this.getUserRatio() == ((UseRatio) arg0).getUserRatio() ? 0
				: (this.getUserRatio() > ((UseRatio) arg0).getUserRatio() ? -1
						: 1);
	}

	public int getNumCarCarrier() {
		return numCarCarrier;
	}

	public void setNumCarCarrier(int numCarCarrier) {
		this.numCarCarrier = numCarCarrier;
	}

	public int getX_I() {
		return x_I;
	}

	public void setX_I(int x_I) {
		this.x_I = x_I;
	}

	public int getX_II() {
		return x_II;
	}

	public void setX_II(int x_II) {
		this.x_II = x_II;
	}

	public int getX_III() {
		return x_III;
	}

	public void setX_III(int x_III) {
		this.x_III = x_III;
	}

	public double getUserRatio() {
		return userRatio;
	}

	public void setUserRatio(double userRatio) {
		this.userRatio = userRatio;
	}

}
