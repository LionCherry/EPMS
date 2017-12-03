package action;

import java.io.IOException;
import java.io.OutputStream;

import obj.annualTable.ListOfPracticeBaseAndStudents;
import obj.staticObject.PracticeBase;

public interface SpecialExcelIO {
	
	
	
	/**
	 * 实习生名单
	 * @return 文件名称
	 */
	public abstract String createStudentList(
			int year,
			PracticeBase pb,
			String majorName,
			OutputStream stream)
					throws IOException;
	
	/**
	 * 布局规划
	 */
	public abstract String createPlanDesign(
			int year,
			ListOfPracticeBaseAndStudents list,
			OutputStream stream)
					throws IOException;
	
	/**
	 * 指导教师列表
	 */
	public abstract String createTeacherList(
			int year,
			OutputStream stream)
					throws IOException;

	/**
	 * 督导任务表
	 */
	public abstract String createSuperviseList(
			int year,
			OutputStream stream)
					throws IOException;
	
	
	
	
}
