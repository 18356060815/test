package com.jnk.test.util;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
/**
 * 类说明 sftp工具类
 */
public class SFTPUtil {
    private transient Logger log = LoggerFactory.getLogger(this.getClass());

    private ChannelSftp sftp;

    private Session session;
    /** SFTP 登录用户名*/
    private String username;
    /** SFTP 登录密码*/
    private String password;
    /** 私钥 */
    private String privateKey;
    /** SFTP 服务器地址IP地址*/
    private String host;
    /** SFTP 端口*/
    private int port;


    /**
     * 构造基于密码认证的sftp对象
     */
    public SFTPUtil(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     */
    public SFTPUtil(String username, String host, int port, String privateKey) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
    }

    public SFTPUtil(){}


    /**
     * 连接sftp服务器
     */
    public void login(){
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                jsch.addIdentity(privateKey);// 设置私钥
            }

            session = jsch.getSession(username, host, port);

            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout(){
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }


    /**
     * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
     * @param basePath  服务器的基础路径
     * @param directory  上传到该目录
     * @param sftpFileName  sftp端文件名
     * @param input   输入流
     */
    public void upload(String basePath,String directory, String sftpFileName, InputStream input) throws SftpException{
        try {
            sftp.cd(basePath);
            sftp.cd(directory);
        } catch (SftpException e) {
            //目录不存在，则创建文件夹
            String [] dirs=directory.split("/");
            String tempPath=basePath;
            for(String dir:dirs){
                if(null== dir || "".equals(dir)) continue;
                tempPath+="/"+dir;
                try{
                    sftp.cd(tempPath);
                }catch(SftpException ex){
                    sftp.mkdir(tempPath);
                    sftp.cd(tempPath);
                }
            }
        }
        sftp.put(input, sftpFileName);  //上传文件
    }


    /**
     * 下载文件。
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException{
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
    }

    /**
     * 下载文件
     * @param directory 下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException{
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);

        byte[] fileData = IOUtils.toByteArray(is);

        return fileData;
    }


    /**
     * 删除文件
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    public void delete(String directory, String deleteFile) throws SftpException{
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }


    /**
     * 列出目录下的文件
     * @param directory 要列出的目录
     * @param directory
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

//    public static FTPClient getFtpClient(String ip, int port, String username, String password) {
//        FTPClient ftp = new FTPClient();
//        try {
//            ftp.connect(ip, port);// 连接FTP服务器
//            ftp.login(username, password);// 登录
//            int reply = ftp.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftp.disconnect();
//                System.out.println("login ftp error");
//            }
//            ftp.enterLocalPassiveMode();
//            ftp.changeWorkingDirectory(FTP_BASE);
////			System.err.println(ftp.printWorkingDirectory());
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ftp;
//    }

//    #ftp
//            FTP_IP=103.229.116.92
//    FTP_PORT=21
//    FTP_USER=ftpcaijian
//            FTP_PASSWORD=2oKDhW5aYELM
//            FTP_BASE=FtpUpload/
//
//#image server
//    IMG_SERVER=http://admin.bee360.io/FtpUpload/

    //上传文件测试
   static String folderName = "admin/images/project_info_s/" + DateUtil.ForDate(new Date(), DateUtil.YYYY_MM_DD) + "/";
//    String fullPath = UploadFileInName(file, fileName, folderName);

    public static void main(String[] args) throws SftpException, IOException {
        // new SFTPUtil("root", "jnk123bee360", "103.229.116.104", 22); //新服务器
        // new SFTPUtil("root", "Yinweidong8650", "47.99.189.110", 22); //免费阿里服务器
        SFTPUtil sftp = new SFTPUtil("ftpcaijian", "2oKDhW5aYELM", "103.229.116.92", 21);
        sftp.login();

        //sftp.download("/usr/es-head-master/_site","app.js","D:\\centos\\1.txt");//下载

        File file = new File("C:\\1.jpg");
        InputStream is = new FileInputStream(file);
        sftp.upload("FtpUpload/",folderName, "1.jpg", is);
        sftp.logout();
        System.out.println("http://admin.bee360.io/FtpUpload/"+folderName+"1.jpg");
    }
}
