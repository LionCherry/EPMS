package action.function;

import java.sql.*;
import java.util.*;

import action.*;
import obj.*;
import obj.annualTable.*;
import obj.staticObject.PracticeBase;
import obj.staticSource.Major;
import token.Role;

/**
 * 安排学生到实习基地
 */
public class StudentArrangeIntoPracticeBase extends Action{
	private static final long serialVersionUID = 5998268336475528662L;

	private action.Annual annual=new action.Annual();
	public action.Annual getAnnual(){return this.annual;}
	
	private String majorName;
	private boolean[] checkBox;
	private ListOfPracticeBaseAndStudentsAndPlan practiceBaseAndStudents;
	private String practiceBaseName;
	
	public void setMajorName(String a){this.majorName=Field.s2S(a);}
	public String getMajorName(){return majorName;}
	public void setCheckBox(boolean[] a){this.checkBox=a;}
	public boolean[] getCheckBox(){return this.checkBox;}
	public ListOfPracticeBaseAndStudentsAndPlan getPracticeBaseAndStudents(){return this.practiceBaseAndStudents;}
	public String getPracticeBaseName(){return this.practiceBaseName;}
	public void setPracticeBaseName(String a){this.practiceBaseName=a;}
	
	//记忆化部件
	public Student getStudent(){return new Student();}
	private List<PracticeBase> practiceBases;
		public List<PracticeBase> getPracticeBases(){
			if(this.practiceBases!=null) return this.practiceBases;
			if(this.practiceBaseAndStudents==null) return null;
			this.practiceBases=new ArrayList<PracticeBase>();
			for(ListOfPracticeBaseAndStudentsAndPlan.RegionPair rp:this.getPracticeBaseAndStudents().getList())
				for(ListOfPracticeBaseAndStudentsAndPlan.RegionPair.PracticeBasePair p:rp.getList())
					if(p.getPracticeBase()!=null)
						this.practiceBases.add(p.getPracticeBase());
			return this.practiceBases;
		}
	private List<Major> majors;
		public List<Major> getMajors(){
			if(this.majors!=null) return this.majors;
			Role role=Role.getRole(Manager.getUser());
			if(role==null) return null;
			try{
				if(role==Role.jwc)
					return this.majors=Base.list(Major.class);
				else
					return this.majors=Base.list(Major.class,
							new Restraint(Field.getField(Major.class,"school"),Manager.getUser().getSchool()));
			}catch(SQLException | IllegalArgumentException | InstantiationException e) {
				e.printStackTrace();
			}return this.majors=null;
		}
	

	static public final String SessionListKey="StudentArrangeIntoPracticeBase_list"; 
	static public final String SessionMajorNameKey="StudentArrangeIntoPracticeBase_majorName";
	
	public StudentArrangeIntoPracticeBase(){
		super();
		this.practiceBaseAndStudents=Manager.loadSession(ListOfPracticeBaseAndStudentsAndPlan.class,SessionListKey);
		this.majorName=Manager.loadSession(String.class,SessionMajorNameKey);
		if(this.majorName==null && this.getMajors()!=null && !this.getMajors().isEmpty())//默认值
			this.setMajorName(this.getMajors().get(0).getName());
		this.setupCheckBox();
	}

	private void setupCheckBox(){
		this.checkBox=null;
		if(this.practiceBaseAndStudents!=null){
			int len=this.practiceBaseAndStudents.getUndistributedStudents().size();
			for(ListOfPracticeBaseAndStudentsAndPlan.RegionPair rp:this.practiceBaseAndStudents.getList())
				for(ListOfPracticeBaseAndStudentsAndPlan.RegionPair.PracticeBasePair p:rp.getList())
					len=Math.max(len,p.getSize());
			this.checkBox=new boolean[len];
		}
	}

	
	/**
	 * 用于显示
	 */
	public String display(){
		this.practiceBaseName=null;
		System.out.println(">> RegionArrangement:display > year="+this.getAnnual().getYear()+",majorName="+majorName);
		this.practiceBaseAndStudents=null;
		Major major=null;
		try{
			major=new Major(this.majorName);
		}catch(IllegalArgumentException | SQLException e){
			return this.returnWithTips(NONE,"专业("+this.majorName+")不存在！",e);
		}
		try {
			this.practiceBaseAndStudents=new ListOfPracticeBaseAndStudentsAndPlan(
					this.getAnnual().getYear(),major,/*minPlanNumber*/1);
		} catch (IllegalArgumentException | InstantiationException | SQLException e) {
			return this.returnWithTips(NONE,"数据库开小差去了！",e);
		}
		if(this.getPracticeBases()==null)
			return this.returnWithTips(NONE,"读取实习基地列表失败!");
		if(this.getMajors()==null)
			return this.returnWithTips(NONE,"读取实习专业列表失败!");
		Manager.saveSession(SessionListKey,this.practiceBaseAndStudents);
		Manager.saveSession(SessionMajorNameKey,this.majorName);
		this.setupCheckBox();
		System.out.println(">> RegionArrangement:display <NONE");
		return NONE;
	}
	
	/**
	 * 用于创建实习大区
	 */
	@Override
	public String execute(){
		if(this.practiceBaseAndStudents==null)
			return display();
		System.out.println(">> RegionArrangement:execute > practiceBaseName= "+this.practiceBaseName);
		if(this.practiceBaseName==null || this.practiceBaseName.isEmpty())
			return this.jumpBackWithTips("请选择一个实习基地！");
		ListOfPracticeBaseAndStudentsAndPlan.RegionPair.PracticeBasePair pair=
				this.practiceBaseAndStudents.get(this.practiceBaseName);
		if(pair==null || pair.getPlan()==null)
			return this.jumpBackWithTips("基地("+this.practiceBaseName+")没有("+this.majorName+")专业派遣计划！");
		//RegionArrangement:execute
		boolean flag=false;
		for(boolean s:checkBox) flag|=s;
		if(!flag)
			return this.jumpBackWithTips("请至少选择一个实习生分配到实习基地！");
		List<Student> tmp=new ArrayList<Student>();
		StringBuilder sb=new StringBuilder();
		StringBuilder error=new StringBuilder();
		for(int i=0;i<this.practiceBaseAndStudents.getUndistributedStudents().size();i++){
			if(checkBox[i]){
				//选中了
				Student stu=this.practiceBaseAndStudents.getUndistributedStudents().get(i);
				if(stu==null||stu.getName()==null)
					continue;
				try{
					if(pair.getPracticeBase().check(stu) &&
							pair.getPlan().check(stu,pair.getStudents().size()+tmp.size())){
						stu.setPracticeBase(this.practiceBaseName);
						stu.update();
					}
				}catch(SQLException | IllegalArgumentException e){
					System.err.println(e.toString());
					if(error.length()>0) error.append('\n');
					error.append(stu.getName()+"("+e.getMessage()+")");
					continue;
				}
				tmp.add(stu);
				if(sb.length()>0) sb.append(',');
				sb.append(stu.getName());
			}
		}
		for(Student s:tmp) {
			pair.getStudents().add(s);
			this.practiceBaseAndStudents.getUndistributedStudents().remove(s);
		}
		Manager.removeSession(SessionListKey);
		return this.jumpToMethodWithTips("display",
				(sb.length()>0?(sb.toString()+" 已经分配到基地("+this.practiceBaseName+")！"):"")+
				(error.length()>0?("\n\n错误信息：\n"+error.toString()):""));
	}
	
	/**
	 * 用于从大区移除基地
	 */
	public String delete(){
		if(this.practiceBaseAndStudents==null)
			return display();
		System.out.println(">> RegionArrangement:delete > practiceBaseName= "+this.practiceBaseName);
		if(this.practiceBaseName==null || this.practiceBaseName.isEmpty())
			return this.jumpBackWithTips("未选中实习生！");
		boolean flag=false;
		for(boolean s:checkBox) flag|=s;
		if(!flag)
			return this.jumpBackWithTips("请至少选择一个实习生来移除！");
		ListOfPracticeBaseAndStudentsAndPlan.RegionPair.PracticeBasePair pair=
				this.practiceBaseAndStudents.get(this.practiceBaseName);
		if(pair==null)
			return this.jumpBackWithTips("选中了一个不存在的实习基地("+this.practiceBaseName+")！");
		List<Student> tmp=new ArrayList<Student>();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<pair.getStudents().size();i++){
			if(checkBox[i]){
				//选中了
				Student stu=pair.getStudents().get(i);
				if(stu==null||stu.getName()==null)
					continue;
				try{
					stu.setPracticeBase(null);
					stu.update();
				}catch(SQLException | IllegalArgumentException e){
					e.printStackTrace();
					continue;
				}
				tmp.add(stu);
				if(sb.length()>0) sb.append(',');
				sb.append(stu.getName());
			}
		}
		for(Student s:tmp)
			pair.getStudents().remove(s);
		Manager.removeSession(SessionListKey);
		return this.jumpToMethodWithTips("display",
				(sb.length()>0?(sb.toString()+" 已经从实习基地("+this.practiceBaseName+")移出！"):"error"));
	}
	
	
}
