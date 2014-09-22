package cn.edu.tongji.sxjm.model;

public class SiteModel {
	private RequirementModel rm;
	private SiteModel front = null;
	private int distanceToFront;

	public SiteModel(RequirementModel rm, SiteModel front, int distanceToFront) {
		super();
		this.rm = rm;
		this.front = front;
		this.distanceToFront = distanceToFront;
	}

	// 判断Requirement中参数是否都为零
	public boolean isRequirementEmpty() {
		return rm.isRequirementEmpty();
	}

	public RequirementModel getRm() {
		return rm;
	}

	public void setRm(RequirementModel rm) {
		this.rm = rm;
	}

	public SiteModel getFront() {
		return front;
	}

	public void setFront(SiteModel front) {
		this.front = front;
	}

	public int getDistanceToFront() {
		return distanceToFront;
	}

	public void setDistanceToFront(int distanceToFront) {
		this.distanceToFront = distanceToFront;
	}

}
