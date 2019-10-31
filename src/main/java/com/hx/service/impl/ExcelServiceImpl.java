package com.hx.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hx.dao.ExcelMapper;
import com.hx.modle.Mail;
import com.hx.service.ExcelService;
import com.hx.util.ExcelHelper;
import org.springframework.stereotype.Service;




@Service("ExcelService")
public class ExcelServiceImpl implements ExcelService {

	@Resource
	private ExcelMapper excelMapper;

	@Override
	public String InputExcel(InputStream is, String originalFilename) {

		ArrayList<ArrayList<String>> list;
//		if (originalFilename.endsWith(".xls")) {
//			list = Mail_List.readExcel2003(is);
//		} else {
//			list = Mail_List.readExcel2007(is);
//		}
		//此处建议直接提供模板,就不需要再去兼容2003和2007,模板是多少就是多少
		String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		list = ExcelHelper.readExcel(is, fileType);//从第二行开始读取,第一行index为0是表头部分  这种excel转集合的方式不妥当

		for (int i=0,j=list.size();i<j;i++){
			ArrayList<String> row = list.get(i);
			//此处做处理,针对空行问题要移除
			if (row.get(0) == null || row.get(0).trim() == "") continue;	//如果首列ID为空,则跳过
			Map<String,Object> ginsengMap = new HashMap<String,Object>();
			ginsengMap.put("linkname", row.get(2).toString());
	        ginsengMap.put("linknumber", row.get(0));
	        ginsengMap.put("typeid", row.get(1));
	        excelMapper.InputExcel(ginsengMap);
		}
		return "Import Success";
	}

	@Override
	public List<Mail> OutputExcel() {
		return excelMapper.getAll();
	}

	@Override
	public List<Mail> getAll() {
		return excelMapper.getAll();
	}

	public Integer deleteMany(int[] id_arr) {
		// TODO 自动生成的方法存根
		return excelMapper.deleteMany(id_arr);
	}

	@Override
	public List<Mail> getById(String id) {
		return excelMapper.getById(id);
	}


}
