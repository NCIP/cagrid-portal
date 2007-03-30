package org.cagrid.gridftp.authorization.plugin.gridgrouper;


import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;

import org.globus.wsrf.encoding.SerializationException;

public class GrouperConfig {

	//private GridFTPOperation _operation;
	private String _operation;
	
	private String _path;
	
	private MembershipExpression _expression;

	//public GrouperConfig(GridFTPOperation _operation, String _path,
	public GrouperConfig(String _operation, String _path,
			MembershipExpression _expression) {
		super();
		this._operation = _operation;
		this._path = _path;
		this._expression = _expression;
	}

	//public GridFTPOperation get_operation() {
	public String get_operation() {
		return _operation;
	}

	public String get_path() {
		return _path;
	}

	public MembershipExpression get_expression() {
		return _expression;
	}
	
	public String toString() {
		String toString = "";
		/*
		MembershipExpression[] expressions = _expression.getMembershipExpression();
		MembershipQuery [] queries = _expression.getMembershipQuery();
		for (MembershipQuery query : queries) {
			toString += "op: " + _operation + ", path: " + _path + ", exp: (" + query.getGroupIdentifier().getGroupName() + ", " + query.getGroupIdentifier().getGridGrouperURL() + ")";
		}
		*/
		try {
			toString += "op: " + _operation + ", path: " + _path + ", exp: " + GridGrouperConfigurationManager.membershipExpressionToString(_expression);
		} catch (SerializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toString;
	}

	/*
	static class ConfiguredAction {
		
		private String _action;
		
		public ConfiguredAction(String action) {
			//this is the string taken from the config file
			//assume that schema is accurate and reflects the GridFTPOperations
			//so there can't be any mismatch!
			_action = action;
		}
	}
	*/
}
