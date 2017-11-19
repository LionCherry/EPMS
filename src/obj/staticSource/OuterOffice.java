package obj.staticSource;

import obj.*;

@SQLTable("OuterOffice")
public class OuterOffice extends Base{
	
	@SQLField(value="校外人员类别",weight=1,isKey=true,notNull=true)
	private String name;
	
	public String getName(){return this.name;}
	public void setName(String a){this.name=Field.s2S(a);}
	

	@Override
	public String getDescription() {
		return this.name;
	}
}