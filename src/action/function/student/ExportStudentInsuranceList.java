package action.function.student;

import java.io.*;

import com.opensymphony.xwork2.ActionSupport;

import action.*;
import obj.*;
import obj.annualTable.*;

/**
 * 导出保险单
 */
public class ExportStudentInsuranceList extends ActionSupport{
	private static final long serialVersionUID = 3677055466118899859L;

	private action.Annual annual=new action.Annual();
	public action.Annual getAnnual(){return this.annual;}
	
	private ListOfPracticeBaseAndStudents practiceBaseAndStudents;
	
	public ListOfPracticeBaseAndStudents getPracticeBaseAndStudents(){return this.practiceBaseAndStudents;}
	

	static public final String SessionListKey=Export.SessionListKey; 
	
	public ExportStudentInsuranceList(){
		super();
		this.practiceBaseAndStudents=Manager.loadSession(ListOfPracticeBaseAndStudents.class,SessionListKey);
	}

	private String jumpURL=Export.ActionName;
		public String getJumpURL() {return this.jumpURL;}
		public void setJumpURL(String a) {this.jumpURL=a;}

	@Override
	public String execute(){
		return Manager.tips("该项目不可用!","jump");
	}
	

	/*
	 * 下载模板
	 */
	private String downloadFileName;
		public void setDownloadFileName(String a){
			this.downloadFileName=a;
			try{this.downloadFileName=new String(a.getBytes("gb2312"), "iso8859-1");
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
				this.downloadFileName=a;
			}//*/
		}
		public String getDownloadFileName(){return this.downloadFileName;}
	private ByteArrayOutputStream downloadOutputStream=null;
	protected String downloadByIO(SpecialIO io,int year,ListOfPracticeBaseAndStudents list,OutputStream stream) throws IOException{
		return io.createStudentInsuranceList(year,list,stream);
	}
	public String download(){//下载模板
		System.out.println(">> ExportStudentInsuranceList:download >");
		if(this.practiceBaseAndStudents==null)
			return Manager.tips("该项目未初始化!","jump");
		System.out.println(">> ExportStudentInsuranceList:download > create download file.");
		this.downloadOutputStream=new ByteArrayOutputStream();
		try{
			String fileName=this.downloadByIO((SpecialIO)Base.io(),
					this.getAnnual().getYear(),practiceBaseAndStudents,downloadOutputStream);
			this.setDownloadFileName(fileName);//设置下载文件名称
			this.downloadOutputStream.flush();
		}catch(IOException e){
			this.downloadOutputStream=null;
			return Manager.tips("服务器开小差去了，暂时无法下载！",e,"jump");
		}
		System.out.println(">> ExportStudentInsuranceList:download <downloadAttachment");
		return "downloadAttachment";
	}
	public InputStream getDownloadAttachment(){//实际上获取的输出流，使用getter获取的downloadAttachment
		ByteArrayInputStream in=IOHelper.ByteArrayOutStream2InputStream(this.downloadOutputStream);
		try {
			this.downloadOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	/*	try {
			ServletActionContext.getResponse().setHeader("Content-Disposition","attachment;downloadFileName="+java.net.URLEncoder.encode(this.downloadFileName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//*/
		return in;
	}
	


}