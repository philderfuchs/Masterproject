package de.automatic.ui.colorconfiguraiton.entities;

public class GetCountOfLastOrderChildrenVisitor implements Visitor {

	private int count;

	public GetCountOfLastOrderChildrenVisitor() {
		super();
		this.count = 0;
	}

	public GetCountOfLastOrderChildrenVisitor(int count) {
		super();
		this.count = count;
	}

	@Override
	public void visit(HierarchicalHsiSample sample) {
		if (sample.getChildren().size() == 0) {
			count++;
		}
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
