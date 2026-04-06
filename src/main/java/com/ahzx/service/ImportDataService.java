package com.ahzx.service;

import com.ahzx.dao.hshy.HyDaoImpl;
import com.ahzx.utils.CommUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author think
 * @date 2023年08月03日 20:35
 */
@Service
public class ImportDataService {
	//定义常量
	public static int COUNT=100;
	private final Logger log= LoggerFactory.getLogger(ImportDataService.class);
	private final HyDaoImpl hyDao;

	public ImportDataService(HyDaoImpl hyDao) {
		this.hyDao = hyDao;
	}
	@Scheduled(cron = "${importCronExpr}")
	public void importData() throws IOException {
		long startTime = System.currentTimeMillis();
		BufferedReader br = null;
		List<Map<String, Object>> requstList = new ArrayList<>();
		String date= CommUtils.getDate(0).replace("-","");
		String filePath="./hxData/input/"+date+"/entname.csv";
		//log.info("当前日期为:{},文件名为:{}", date,filePath);
		//判断文件是否存在
		File inputfile = new File(filePath);// 线上审批
		if(!inputfile.exists()){
			//log.info("当前日期为:{}下面的文件不存在,文件目录为:{}", date,filePath);
			return;
		}
		String successFlag = "./hxData/input/" + date + "/over.csv";
		File fileSuccess = new File(successFlag);
		if(fileSuccess.exists()){
			return;
		}
		int total=0;
		try {
			InputStreamReader fReader = new InputStreamReader(Files.newInputStream(inputfile.toPath()),
					StandardCharsets.UTF_8);
			br = new BufferedReader(fReader);
			String readLine;

			while ((readLine = br.readLine()) != null) {
				total++;
				log.info("第{}行数据为:{}", total, readLine);
				String[] params = readLine.split(";");
				if (CommUtils.isEmptyStr(params[2]) || CommUtils.isEmptyStr(params[1])
						|| CommUtils.isEmptyStr(params[0])) {
					log.info("[水号:{}、企业名:{}或者同一社会信用代码:{}]为空",params[0],params[1],params[2]);
					continue;
				}
				Map<String, Object> map = new HashMap<>();
				map.put("serialno", params[0]);
				map.put("nsrmc",  params[1]);
				map.put("nsrsbh", params[2]);
				map.put("ifkjedai",  params[3]);
				map.put("hytype", params[4]);
				map.put("hytypecode",  params[5]);
				map.put("xedoldcust", params[6]);
				map.put("inputtime", CommUtils.getDate());
				map.put("batchdate", date);
				String params8 = params[8];
				//log.info("params[8]值:{}", params8);
				map.put("xed_type","Y");
				if(!CommUtils.isEmptyStr(params8)){
					//log.info("表达式:{}", params8.equals("1")?"Y":"N");
					map.put("xed_type",params8.equals("1")?"Y":"N");
				}
				//map.put("xed_type", params[8]);
				String params9 = params[9];
				map.put("kced_type","N");
				if(!CommUtils.isEmptyStr(params9)){
					//log.info("表达式:{}", params9.equals("1")?"Y":"N");
					map.put("kced_type",params9.equals("1")?"Y":"N");
				}
				//hyDao.insert("hshy", "common.insertData", map);
				requstList.add(map);
				if(requstList.size()>=100){
					saveDate(requstList, date);
				}
			}
			if(!requstList.isEmpty()){
				saveDate(requstList, date);
			}
			//String successFlag = "./hxData/input/" + date + "/over.csv";
			//File fileSuccess = new File(successFlag);
			if (fileSuccess.createNewFile()) {
				log.info("over.csv文件创建成功!");
			}else{
				log.info("文件已经创建过!");
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		long endTime = System.currentTimeMillis();
		int time = (int) ((endTime - startTime) / 1000);
		log.info("{}条数据导入共耗时{}秒",total,time);
	}

	private void saveDate(List<Map<String, Object>> requstList, String date) {
		final HashMap<Object, Object> map1 = new HashMap<>();
		map1.put("batchdate", date);
		map1.put("list", requstList);
		hyDao.insert("hshy", "common.insertData1", map1);
		requstList.clear();
	}
}
