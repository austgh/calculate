package com.ahzx.service;

import com.ahzx.dao.hshy.HyDaoImpl;
import com.ahzx.utils.CommUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * @author think
 * @date 2023年08月03日 21:05
 */
@Service
public class ExportDataService {
    private final Logger log = LoggerFactory.getLogger(ExportDataService.class);
    private final HyDaoImpl hyDao;

    public ExportDataService(HyDaoImpl hyDao) {
        this.hyDao = hyDao;
    }

    //@Scheduled(cron = "${exportCronExpr}")
    public void exportData() throws Exception {
        String date = CommUtils.getDate(0).replace("-", "");
        //文件路径
        String filePath = "./hxData/output/" + date + "/result.csv";
        File fileName = getFile(date, filePath);
        if (fileName == null) {
            return;
        }
        BufferedWriter bufferwriter;
        bufferwriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(fileName.toPath()), StandardCharsets.UTF_8));
        int count = hyDao.queryForInt("mapping/hshy", "common.queryResultCount", date);
        if (count < 100000) {
            final List<Map<String, Object>> resultList = hyDao.queryForList("mapping/hshy", "common.queryResultInfo",
                    date);
            for (Map<String, Object> stringObjectMap : resultList) {
                //ModelService.writeData(bufferwriter, stringObjectMap, log);
            }
            // 关闭流
            bufferwriter.newLine();
            bufferwriter.close();
            log.info("文件写入成功");
            String successFlag="./hxData/output/" + date + "/success.csv";
            File fileSuccess = new File(successFlag);
            if (fileSuccess.createNewFile()) {
                log.info("标志文件创建成功！");
            }
        }

    }

    private File getFile(String date, String filePath) throws IOException {
        log.info("当前日期为:{},文件名为:{}", date, filePath);
        File fileName = new File(filePath);
        if(fileName.exists()) {
            log.info("创建单个文件{}失败，目标文件已存在！", filePath);
            return null;
        }
        if (filePath.endsWith(File.separator)) {
            log.info("创建单个文件{}失败，目标文件不能为目录!", filePath);
            return null;
        }
        if(!fileName.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            log.info("目标文件所在目录不存在，准备创建它！");
            if(!fileName.getParentFile().mkdirs()) {
                log.info("创建目标文件所在目录失败！");
                return null;
            }
        }
        if (fileName.createNewFile()) {
            log.info("文件创建成功！");
        }
        return fileName;
    }
}
