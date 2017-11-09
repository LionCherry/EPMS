package obj.staticSource;

import obj.*;

@SQLTable("School")
public class School extends Base{
	
	@SQLField(value="学院名称",weight=1,isKey=true,notNull=true)
	private String name;
	
	public String getName(){return this.name;}
	public void setName(String name){this.name=name==null||name.isEmpty()?null:name;}
	
	public School(String name){
		super();
		this.setName(name);
	}
	
	
	@Override
	public String getDescription() {
		return this.name;
	}
	
	
}