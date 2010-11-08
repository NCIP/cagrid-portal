package gov.nih.nci.cagrid.portal.portlet.map.ajax;

public class ClassCounter {
	
	public ClassCounter(){}
	private String className;
	private String caption;
	private int count;
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
