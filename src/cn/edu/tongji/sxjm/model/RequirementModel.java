package cn.edu.tongji.sxjm.model;

public class RequirementModel implements Comparable {
	private int x_I, x_II, x_III;

	public RequirementModel() {
		super();
	}

	public RequirementModel(int x_I, int x_II, int x_III) {
		super();
		this.x_I = x_I;
		this.x_II = x_II;
		this.x_III = x_III;
	}

	public RequirementModel(RequirementModel other) {
		super();
		this.x_I = other.x_I;
		this.x_II = other.x_II;
		this.x_III = other.x_III;
	}

	// 判断参数是否都为零
	public boolean isRequirementEmpty() {
		return (this.x_I == 0 && this.x_II == 0 && this.x_III == 0) ? true
				: false;
	}

	// RequirementModel参数分别相加
	public RequirementModel add(RequirementModel other) {
		return new RequirementModel(this.x_I + other.getX_I(), this.x_II
				+ other.getX_II(), this.x_III + other.getX_III());

	}

	public int getSum() {
		return (this.x_I + this.x_II + this.x_III);
	}

	// RequirementModel参数相减，两个RequirementModel互减，返回第一个
	public RequirementModel subBoth2(RequirementModel other) {
		RequirementModel one = new RequirementModel(this.x_I, this.x_II,
				this.x_III);
		one.subBoth(other);
		return one;
	}

	// RequirementModel参数相减，两个RequirementModel互减，返回第二个
	public RequirementModel subBoth(RequirementModel other) {
		if (this.x_I >= other.getX_I()) {
			this.x_I = this.x_I - other.getX_I();
			other.setX_I(0);
		} else {
			other.setX_I(other.getX_I() - this.x_I);
			this.x_I = 0;
		}
		if (this.x_II >= other.getX_II()) {
			this.x_II = this.x_II - other.getX_II();
			other.setX_II(0);
		} else {
			other.setX_II(other.getX_II() - this.x_II);
			this.x_II = 0;
		}
		if (this.x_III >= other.getX_III()) {
			this.x_III = this.x_III - other.getX_III();
			other.setX_III(0);
		} else {
			other.setX_III(other.getX_III() - this.x_III);
			this.x_III = 0;
		}
		return other;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		int result;
		RequirementModel other = (RequirementModel) o;
		if (this.x_I == other.getX_I() && this.x_II == other.getX_II()
				&& this.x_III == other.getX_III()) {
			result = 0;
		} else if (this.x_I < other.getX_I() || this.x_II < other.getX_II()
				|| this.x_III < other.getX_III()) {
			result = -1;
		} else {
			result = 1;
		}
		return result;
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
		RequirementModel other = (RequirementModel) obj;
		if (x_I != other.x_I)
			return false;
		if (x_II != other.x_II)
			return false;
		if (x_III != other.x_III)
			return false;
		return true;
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

}
