package project.HFMzip;

public  class  representation{
	public int index;
	public int count;
	public representation(int index, int count) {
		this.index = index;
		this.count = count;
	}
	@Override
	public String toString() {
		return "ind "+index+" | cot "+count;
	}
	
}