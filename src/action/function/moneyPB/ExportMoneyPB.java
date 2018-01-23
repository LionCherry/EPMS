package action.function.moneyPB;

import java.io.*;

import action.*;
import obj.*;
import obj.annualTable.*;
import obj.annualTable.list.Leaf;
import obj.annualTable.list.List_Region_PracticeBaseRegionMoneyPB_MoneyPB;
import obj.annualTable.list.PracticeBaseWithRegionWithMoneyPB;

public class ExportMoneyPB extends Action{
	private static final long serialVersionUID = 3677055466118899859L;

	private action.Annual annual=new action.Annual();
	public action.Annual getAnnual(){return this.annual;}
	
	
	private List_Region_PracticeBaseRegionMoneyPB_MoneyPB list;
	
	public List_Region_PracticeBaseRegionMoneyPB_MoneyPB getPracticeBaseAndStudents(){return this.list;}
	

	static public final String SessionListKey=Export.SessionListKey; 
	
	public ExportMoneyPB(){
		super();
		this.list=Manager.loadSession(List_Region_PracticeBaseRegionMoneyPB_MoneyPB.class,SessionListKey);
	}

	@Override
	public String execute(){
		return this.jumpBackWithTips("该项目不可用!");
	}
	
	
	
	private String practiceBaseName;
		public void setPracticeBaseName(String a) {this.practiceBaseName=Field.s2S(a);}
		public String getPracticeBaseName() {return this.practiceBaseName;}
	

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
	protected String downloadByIO(SpecialIO io,int year,PracticeBaseWithRegionWithMoneyPB pbrm, OutputStream stream) throws IOException{
		return io.createPracticeBaseMoney(year,pbrm,stream);
	}
	public String download(){//下载模板
		System.out.println(">> ExportMoneyPB:download > practiceBaseName="+this.practiceBaseName);
		if(this.list==null)
			return this.jumpBackWithTips("该项目未初始化!");
		Leaf<PracticeBaseWithRegionWithMoneyPB, MoneyPB> pair=
				this.list.get(this.practiceBaseName);
		if(pair==null)
			return this.jumpBackWithTips("实习基地名称有误!");
		final Boolean status=false;
		if(status!=null && (status^pair.getT().getPracticeBase().getStatus()))
			return this.jumpBackWithTips("实习基地是非正式实习基地，不能提供经费明细！");
		System.out.println(">> ExportMoneyPB:download > create download file.");
		downloadOutputStream=new ByteArrayOutputStream();
		try{
			String fileName=this.downloadByIO((SpecialIO)Base.io(),
					this.getAnnual().getYear(),pair.getT(),downloadOutputStream);
			this.setDownloadFileName(fileName);//设置下载文件名称
			this.downloadOutputStream.flush();
		}catch(IOException e){
			downloadOutputStream=null;
			return this.jumpBackWithTips("服务器开小差去了，暂时无法下载！",e);
		}
		System.out.println(">> ExportMoneyPB:download <downloadAttachment");
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
