package com.jnk.test.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class FtpUtil {
	@Autowired
	JdbcTemplate jdbcTemplate;
	//private static String FTP_BASE = PropertiesUtil.getProperty("FTP_BASE"); // ftp根目录
	/**
	 * FTP上传文件
	 * 
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public static FTPClient getFtpClient(String ip, int port, String username, String password) {
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(ip, port);// 连接FTP服务器
			ftp.login(username, password);// 登录
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.out.println("login ftp error");
			}
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory("FtpUpload/");
//			System.err.println(ftp.printWorkingDirectory());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftp;
	}

	public static boolean uploadFile(FTPClient ftp, String path, String filename, InputStream is) {
		boolean success = false;
		try {
			System.out.println("path "+ftp.printWorkingDirectory());

			boolean isRightDir = ftp.printWorkingDirectory().contains(path);
			if(!isRightDir){
			boolean flag = ftp.changeWorkingDirectory(path);
			if (!flag) {
				createD(ftp, path);
			}
			}
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);// 二进制文件
			success = ftp.storeFile(filename, is);
//			System.err.println("upload "+ftp.printWorkingDirectory()+"/"+filename+" "+success);
			
		} catch (Exception e) {
			System.out.println("no ftp server...");
			e.printStackTrace();
		} finally {
			try {
				if(is!=null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public static boolean createD(FTPClient ftp, String path) throws IOException {
		String temp[] = path.split("/");
		for (int i = 0; i < temp.length; i++) {
//			ftp.printWorkingDirectory();
			ftp.makeDirectory(temp[i]);
			ftp.changeWorkingDirectory((ftp.printWorkingDirectory().endsWith("/")?ftp.printWorkingDirectory():ftp.printWorkingDirectory()+"/")+temp[i]);
		}
		return true;

	}
	public static void closeFtp(FTPClient  ftp) {
		try {
			ftp.changeWorkingDirectory("FtpUpload/");
			ftp.logout();
			ftp.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//链接url下载图片
	public static String downloadvideo(String urlList,String filename) {
		URL url = null;
		int imageNumber = 0;
		String imageName=null;
		try {
			url = new URL(urlList);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());

			imageName =  "F:/"+filename;

			FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[2048];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			byte[] context=output.toByteArray();
			fileOutputStream.write(output.toByteArray());
			System.out.println("下载中....");
			dataInputStream.close();
			fileOutputStream.close();
			System.out.println("下载成功....");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageName;
	}



	//链接url下载图片
	private static String downloadPicture(String urlList,String filename) {
		URL url = null;
		int imageNumber = 0;
		String imageName=null;
		try {
			url = new URL(urlList);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());

			imageName =  "F:/project/"+filename;

			FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			byte[] context=output.toByteArray();
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageName;
	}
	final  static String folderName = "admin/images/project_info_s/2019-04-06/";//项目图片 白皮书
//	static String folderNameteam = "admin/images/project_team_info/" + DateUtil.ForDate(new Date(), DateUtil.YYYY_MM_DD) + "/";//项目人物图片
//	static String folderNamepimc = "admin/images/project_pimc_info/" + DateUtil.ForDate(new Date(), DateUtil.YYYY_MM_DD) + "/";//项目投资机构图片

	static FTPClient ftp=getFtpClient("103.229.116.92",21, "ftpcaijian", "2oKDhW5aYELM");
	public static String  uploadfiles(String filename,String folderName) throws Exception {
		File file = new File(filename);
		InputStream is = new FileInputStream(file);
		String imgname=file.getName();
		FtpUtil.uploadFile(ftp,folderName, imgname, is);// ftp
		is.close();
		return  "http://admin.bee360.io/FtpUpload/"+folderName+imgname;
	}

	@Test
	public void putHotNodePic() throws Exception{
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from project_info where id =15413");
		for(Map map:list){
			String id=map.get("id").toString();//项目id
			Object pic_urls=map.get("pic_url");//图片链接
			System.out.println(id);

			String pic_urlrs=null;
			if(isObjectNotEmpty(pic_urls)){
				String pic_url=pic_urls.toString();
				String pic_url_str=pic_url.split("/")[pic_url.split("/").length-1];
				String filename = downloadPicture(pic_url, id + "." + pic_url_str.split("\\.")[pic_url_str.split("\\.").length - 1]);
				if(filename!=null){
					String imgpath=uploadfiles(filename,folderName);
					pic_urlrs=imgpath;
					System.out.println(pic_urlrs);

				}
			}

			String white_paperrs=null;
			Object white_papers=map.get("white_paper");//白皮书链接
			if(isObjectNotEmpty(white_papers)){
				String white_paper=white_papers.toString();
				String white_paper_str=white_paper.split("/")[white_paper.split("/").length-1];
				String filename = downloadPicture(white_paper,id+"."+white_paper_str.split("\\.")[white_paper_str.split("\\.").length-1]);
				if(filename!=null){
					String imgpath=uploadfiles(filename,folderName);
					white_paperrs=imgpath;
					System.out.println(white_paperrs);

				}

			}

			JSONArray teamArr=null;
//			Object teams=map.get("team");//队伍成员图片链接json
//			if(isObjectNotEmpty(teams)){
//				JSONArray teamjson= JSONArray.fromObject(teams.toString());
//				for(int i=0;i<teamjson.size();i++) {
//					Object profile_pic=teamjson.getJSONObject(i).get("profile_pic");//人物链接
//					Object user_id=teamjson.getJSONObject(i).get("id").toString();//人物id
//
//					if(isObjectNotEmpty(profile_pic)&&!profile_pic.toString().equals("null")){
//						String profile_pics=profile_pic.toString();
//						String profile_picss=profile_pics.split("/")[profile_pics.split("/").length-1];
//						String filename = downloadPicture(profile_pics,id+user_id+"."+profile_picss.split("\\.")[profile_picss.split("\\.").length-1]);
//						if(filename!=null){
//							String imgpath=uploadfiles(filename,folderNameteam);
//							teamjson.getJSONObject(i).put("profile_pic",imgpath);
//						}
//					}
//
//				}
//				System.out.println(teamjson);
//				teamArr=teamjson;
//			}
//			System.out.println(teamArr);

			JSONArray pimcArr=null;
//            Object pimc=map.get("pimc");//投资机构成员图片链接json
//			if(isObjectNotEmpty(pimc)){
//				JSONArray pimcjson= JSONArray.fromObject(pimc.toString());
//				for(int i=0;i<pimcjson.size();i++) {
//					Object logo_url=pimcjson.getJSONObject(i).get("logo_url");//投资机构成员链接
//					Object user_id=pimcjson.getJSONObject(i).get("id").toString();//投资机构成员id
//					if(isObjectNotEmpty(logo_url)&&!logo_url.toString().equals("null")){
//						String logo_urls=logo_url.toString();
//						String logo_urlss=logo_urls.split("/")[logo_urls.split("/").length-1];
//						String filename = downloadPicture(logo_urls,user_id+"."+logo_urlss.split("\\.")[logo_urlss.split("\\.").length-1]);
//						if(filename!=null){
//							String imgpath=uploadfiles(filename,folderNamepimc);
//							pimcjson.getJSONObject(i).put("logo_url",imgpath);
//						}
//
//					}
//
//				}
//				pimcArr=pimcjson;
//			}
//			System.out.println(pimcArr);


			String updatesql="update project_info set id ='"+id+"' , ";
			if(pic_urlrs!=null){
				updatesql+=" pic_url='"+pic_urlrs+"' , ";
			}
			if(white_paperrs!=null){
				updatesql+=" white_paper='"+white_paperrs+"' , ";
			}
			if(teamArr!=null){
				updatesql+=" team='"+teamArr.toString().replace("'","\\'").replace("\\n","\\\\n")+"' , ";
			}
			if(pimcArr!=null){
				updatesql+=" pimc='"+pimcArr.toString().replace("'","\\'").replace("\\n","\\\\n")+"' , ";
			}
			updatesql+=" id='"+id+"'  where id='"+id+"'";
			System.out.println(updatesql);
			jdbcTemplate.execute(updatesql);

			System.out.println("-------------------------------------");

		}
	}
	  /**
      * 判断Object对象为空或空字符串
      * @param obj
      * @return
      */
public static Boolean isObjectNotEmpty(Object obj) {
String str = ObjectUtils.toString(obj, "");
return StringUtils.isNotBlank(str);
}

}
