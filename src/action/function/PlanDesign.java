package action.function;

import action.Manager;
import obj.*;

public class PlanDesign extends action.TableOperationAction{
	private static final long serialVersionUID = 8833385464572061925L;

	private action.Annual annual=new action.Annual();
	public action.Annual getAnnual(){return this.annual;}
	
	
	public PlanDesign(){
		super();
	}
	
	@Override
	protected Search createSearch() throws Exception {
		JoinParam param=new JoinParam(obj.annualTable.Plan.class);
		obj.staticObject.InnerPerson user=Manager.getUser();
		token.Role role=token.Role.getRoleByInnerPerson(user);
		return new Search(param,new obj.restraint.YearAndSchoolAndMajorRestraint(param,3,
				this.getAnnual().getYear(),
				role==token.Role.jwc ? null : new obj.staticSource.School(user.getSchool())
						));
	}
	
}
