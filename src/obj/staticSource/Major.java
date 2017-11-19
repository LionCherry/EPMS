package obj.staticSource;


import java.sql.SQLException;

import obj.*;

@SQLTable("Major")
public class Major extends Base{
	
	@SQLField(value="专业名称",weight=1,isKey=true,notNull=true)
	private String name;
	@SQLField(value="所属学院",weight=10,isKey=true,notNull=true,source="School.name")
	private String school;

	public String getName(){return this.name;}
	public void setName(String a){this.name=Field.s2S(a);}
	public String getSchool(){return this.school;}
	public void setSchool(String a){this.school=Field.s2S(a);}
	
	@Override
	public String getDescription() {
		return this.getName();
	}
	
	public Major(String name) throws IllegalArgumentException, SQLException{
		super();
		this.setName(name);
		this.load();
	}
}