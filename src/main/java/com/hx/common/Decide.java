package com.hx.common;/*
 *//*
 *@作者:张立恒
 *@时间:2019/10/11 11:01
 *@功能:
 */

import com.hx.dao.ProgramSettingDao;
import com.hx.modle.Program_Setting;
import com.hx.modle.TempModel;
import com.hx.util.GetTimeToFileName;
import com.hx.util.PrintImage;
import com.hx.util.TiffToJPEG;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hx.common.StaticFinal.TEMPDIR;
import static com.hx.util.ColorReverse.writeJpg;

//根据switch case判断当前状态码
public class Decide {
    @Autowired
    private static ProgramSettingDao programSettingDao;
    private static Logger logger=Logger.getLogger(Decide.class);
    public static void decideCh(int flag, int ch){
        if(flag==2){
            Map<String,String> map=chState_2(ch);
            if(map.get( "message" ).equals( "成功" )){
                Program_Setting programSetting=programSettingDao.selectProgramSetting();
                String printService=programSetting.getPrintService();
                if(programSetting.getIsPrint()==0){
                    int tifLength=map.get( "tifPath" ).length();
                    int tifLengthBack=map.get( "tifPathBack" ).length();
                    if(tifLength>0){
                        Map<String,Object> fileMap=TiffToJPEG.readerTiff(map.get( "tifPath" ));
                        try {
                            List<String> pathList=(List<String>)fileMap.get("pathList");
                            List<File> newList=writeJpg(pathList);
                            PrintImage.printImageWhenReceive(newList,printService);
                            deleteFileList((List<File>)fileMap.get("fileList"),newList);
                        } catch (Exception e) {
                            logger.error( e.toString() );
                        }
                    }
                    if(tifLengthBack>0){
                        Map<String,Object> fileMap=TiffToJPEG.readerTiff(map.get( "tifPathBack" ));
                        try {
                            //将文件先转换颜色放进list集合再打印
                            List<String> pathList=(List<String>)fileMap.get("pathList");
                            List<File> newList=writeJpg(pathList);
                            PrintImage.printImageWhenReceive(newList,printService);
                            deleteFileList((List<File>)fileMap.get("fileList"),newList);
                        } catch (Exception e) {
                            logger.error( e.toString() );
                        }
                    }
                }
            }

        }
    }
    public static Map<String,String> chState_2(int ch) {
        Map<String,String> map=new HashMap<>(  );
        String message="成功";
        String tifPathBack="";
        String tifPath="";
        //摘机
        int pickup=Fax.INSTANCE.SsmPickup(ch);
        if(pickup==0){
            //获取发送方号码
            String callerId = Fax.INSTANCE.SsmGetCallerIdA( ch );
            int i = 8;
            //查找空闲软通道
            if (ch == 0) {
                i = 8;
            } else if (ch == 1) {
                i = 9;
            } else if (ch == 2) {
                i = 10;
            } else {
                i = 11;
            }
            //根据编号获取当前通道状态
            int code = Fax.INSTANCE.SsmGetChState( i );
            if (code == 0) {
                //空闲状态,建立连接
                int linkOk = Fax.INSTANCE.SsmTalkWith( ch, i );
                if (linkOk == 0) {
                    tifPath=TEMPDIR+"\\"+GetTimeToFileName.GetTimeToFileName()+".tif";
                    Fax.INSTANCE.SsmFaxStartReceive( i,tifPath);
                    TempModel tempModel=new TempModel();
                    tempModel= sendEnd( ch, i,tempModel );
                    if (tempModel.getEndFlag() == 0) {
                        int codeMode=tempModel.getCodeMode();
                        if(codeMode==0){
                            int flag=stopAndHungUp(ch,i);
                            if(flag==0){
                                //仅正文,存入数据库
                                logger.info( "接收成功" );
                            }
                        }else if(codeMode==1){
                            try {
                                Thread.sleep( 2000 );
                                //正文,查看是否有F1信号
                                int findVoc=Fax.INSTANCE.SsmGetVocFxFlag( i,1,true);
                                if(findVoc==0){
                                    //对方发送了一次
                                    tifPathBack=TEMPDIR+"\\"+GetTimeToFileName.GetTimeToFileName()+".tif";
                                    Fax.INSTANCE.SsmFaxStartReceive( i,tifPathBack);
                                    TempModel tempModel2=new TempModel();
                                    tempModel2= sendEnd( ch, i,tempModel2 );
                                    if (tempModel.getEndFlag() == 0) {
                                        int codeMode2=tempModel.getCodeMode();
                                        if(codeMode2==0){
                                            int flag=stopAndHungUp(ch,i);
                                            if(flag==0){
                                                //将回执文件存入数据库
                                                logger.info( "接收成功" );
                                            }
                                        }
                                    }
                                }else{
                                    int flag=stopAndHungUp(ch,i);
                                    if(flag==0){
                                        //仅正文,存入数据库
                                        logger.info( "接收成功" );
                                    }
                                }
                            } catch (InterruptedException e) {
                                message=e.toString();
                                logger.error( "Thread.sleep异常:"+e.toString() );
                            }
                        }
                    }
                } else {
                    message = Fax.INSTANCE.SsmGetLastErrMsgA();
                    logger.error( "建立连接失败:"+message );
                }
            }
        }
        map.put( "message",message );
        map.put( "tifPath",tifPath );
        map.put( "tifPathBack",tifPathBack );
        return map;
    }
    public static TempModel sendEnd(int ch, int i,TempModel tempModel){
        int end=Fax.INSTANCE.SsmGetChState(i);
        if(end==55){
            int codeMode=Fax.INSTANCE.SsmFaxGetCodecMode(ch,0);
            tempModel.setCodeMode( codeMode );
        }else if(end==0){
            tempModel.setEndFlag( end );
        }else{
            try {
                Thread.sleep( 1000 );
                tempModel=sendEnd(ch,i,tempModel);
            } catch (InterruptedException e) {
                logger.error( "Thread.sleep异常:"+e.toString() );
            }
        }
        return tempModel;
    }
    public static int stopAndHungUp(int ch, int i){
        int flag=-1;
        String errMsg="";
        int isStop=Fax.INSTANCE.SsmStopTalkWith(ch,i);
        if(isStop==0){
            int hangup=Fax.INSTANCE.SsmHangup(ch);
            if(hangup==0){
                flag=0;
            }else{
                errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
                logger.error("挂机失败:"+errMsg);
            }
        }else{
            errMsg=Fax.INSTANCE.SsmGetLastErrMsgA();
            logger.error("断开连接失败:"+errMsg);
        }
        return flag;
    }
    public static void deleteFileList(List<File> list,List<File> newList){
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                list.get(i).delete();
            }
        }
        if(newList.size()>0){
            for(int i=0;i<newList.size();i++){
                newList.get(i).delete();
            }
        }
    }


}
