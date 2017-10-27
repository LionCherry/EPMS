package obj.staticSource;

import java.sql.SQLException;

import obj.*;

@SQLTable("City")
public class City extends ListableBase{
	
	@SQLField(isKey=true)
	private String name;
	@SQLField(source="Province")
	private String province;

	public String getName(){return this.name;}
	public void setName(String name){this.name=name==null||name.isEmpty()?null:name;}
	public String getProvince(){return this.province;}
	public void setProvince(String province){this.province=province==null||province.isEmpty()?null:province;}
	
	public City() throws SQLException {
		super();
	}
}