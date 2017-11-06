package action.jwc;

import java.sql.SQLException;

import obj.Search;

public class InnerPersonImport extends action.TableOperationAction{
	private static final long serialVersionUID = 8833385464572061925L;

	private action.Annual annual=new action.Annual();
	public action.Annual getAnnual(){return this.annual;}
	

	public InnerPersonImport() {
		super("InnerPerson");
	}

	@Override
	protected void setupSearchRestraint()
			throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, SQLException {
		this.getSearch().setRestraint(new Search.jwyRestraint(this.getSearch().getClassInfo(),
				null,
				this.getAnnual().getYear()));
	}

}
