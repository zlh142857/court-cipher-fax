package com.hx.util;/*
 *//*
 *@作者:张立恒
 *@时间:2019/9/26 9:36
 *@功能:
 */



public class TiffToPdf {
    //向上抛出异常,读取tif文件为空
   /* public static void main(String [] args) throws Exception{
        String file1="C:\\Users\\zlh\\Desktop\\test1.tif";
        //思路:传入参数为  tif文件数组    PDF文件存储路径   然后循环读取tif内容,每读取一页就加载到PDF一次
        String pdfFilePath=TiffToFileDir+"\\"+"test.pdf";
        // 创建存储PDF文件的文件夹
        File pdfFolder = new File(TiffToFileDir);
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
        }
        Document docment = null;// 文档对象
        try {
            // 定义输出位置并把文档对象装入输出对象中
            docment = new Document(PageSize.LETTER, 0, 0, 0, 0);// 创建一个文档对象
            PdfWriter pdfWriter = PdfWriter.getInstance(docment, new FileOutputStream(pdfFilePath));
            pdfWriter.setStrictImageSequence(true);

            int tifFilePageCount = 0;// tif文件页数
            for (int i = 0; i < 1; i++) {
                RandomAccessFileOrArray randomAccessFileOrArr = null;
                try {
                    File tifFile = new File( "C:\\Users\\zlh\\Desktop\\test1.tif" );
                    InputStream tifFileInputStream = null;
                    try {
                        tifFileInputStream = new FileInputStream(tifFile);
                        randomAccessFileOrArr = new RandomAccessFileOrArray(tifFileInputStream);
                        tifFilePageCount = TiffImage.getNumberOfPages(randomAccessFileOrArr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("读取tiff文件["
                                + (null == tifFile) != null?"文件为空":tifFile.getName() + "]页数失败");
                    } finally {
                        if(null != tifFileInputStream){
                            tifFileInputStream.close();
                        }
                    }

                    for (int pageNumber = 1; pageNumber <= tifFilePageCount; ++pageNumber) {
                        try {

                            // 将tif文件读取成图片
                            Image tifImg = TiffImage.getTiffImage(randomAccessFileOrArr, pageNumber);
                            if(null == tifImg){
                                throw new Exception("读取tif文件失败");
                            }

                            // 设置首页tif图片的大小
                            Rectangle firstpageSize = new Rectangle(tifImg.getWidth(), tifImg.getHeight());
                            docment.setPageSize(firstpageSize);
                            docment.open();// 打开文档对象

                            // 设置图片大小
                            Rectangle pageSize = new Rectangle(tifImg.getWidth(), tifImg.getHeight());
                            docment.setPageSize(pageSize);

                            tifImg.setAbsolutePosition(0, 0);// 设置图像位置
                            tifImg.setAlignment(Image.ALIGN_CENTER);// 设置图像的对齐方式(居中)。
                            docment.add(tifImg);// 加载tif图片
                            docment.newPage(); // 新建新的pdf页
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new Exception("tif文件[" + tifFile.getName() + "]载入pdf文件失败");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }finally {

                    // 关闭
                    if(null != randomAccessFileOrArr){
                        randomAccessFileOrArr.close();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭文档对象，释放资源
            if(null != docment){
                docment.close();
            }
        }
    }*/
}
