package action.jwc;

import java.sql.*;

/**
 * 导入免费师范生数据
 */
public class mfsfsxxsj extends action.login.AnnualAction{
	private static final long serialVersionUID = 5998268336475528662L;
	
	public mfsfsxxsj() throws SQLException, NoSuchFieldException, SecurityException{
		super();
	}
	
	
	private String jumpURL="login.action";
		public String getJumpURL(){return jumpURL;}

	@Override
	public String execute(){
		if(!executive)
			return display();
		return display();
	}
	
	@Override
	public String display(){
		this.jumpURL="jwc_function_TableOperation_display.action?tableName=Student&year="+this.getYear();
		return "jump";
	}
	
	
	
}
