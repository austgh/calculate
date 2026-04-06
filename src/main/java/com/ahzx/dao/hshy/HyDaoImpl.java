package com.ahzx.dao.hshy;

import com.ahzx.dao.BaseDao;
import com.ahzx.utils.CommUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
*
 * @date 2026/4/5 20:47
 * 数据源DAO操作层
 */
@Repository("hyDao")
public class HyDaoImpl implements BaseDao {

    private Logger logger = LoggerFactory.getLogger(HyDaoImpl.class);

    @Autowired
    SqlSessionTemplate hshySqlSessionTemplate;

    @Override
    public List<Map<String, Object>> queryForList(String type, String sqlId, Object reqData) {
        List<Map<String, Object>> list = null;
        try {
            list = hshySqlSessionTemplate.selectList(sqlId, reqData);
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 查询异常！", e);
        }
        return list;
    }

    @Override
    public boolean queryForBool(String type, String sqlId, Object reqData) {
        try {
            int result = hshySqlSessionTemplate.selectOne(sqlId, reqData);
            return result > 0 ? true : false ;
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 查询异常！", e);
            return false;
        }
    }

    @Override
    public int queryForInt(String type, String sqlId, Object reqData) {
        int result = 0;
        try {
            Object object = hshySqlSessionTemplate.selectOne(sqlId, reqData);
            result = object == null?0:(int)object;
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 查询异常！", e);
        }
        return result;
    }

    @Override
    public String queryForString(String type, String sqlId, Object reqData) {
        List<String> result = null;
        try {
            result = hshySqlSessionTemplate.selectList(sqlId, reqData);
            if (CommUtils.isEmptyList(result)) {
                return "";
            }
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 查询异常！", e);
            return "";
        }
        return  result.get(0);
        
        
    }

    @Override
    public Map<String, Object> queryForMap(String type, String sqlId, Object reqData) {
        List<Map<String, Object>> list = null;
        try {
            list = hshySqlSessionTemplate.selectList(sqlId, reqData);
            if (CommUtils.isEmptyList(list)) {
                return  new HashMap<String,Object>();
            }
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 查询异常！", e);
            return new HashMap<String,Object>();
        }
        return list.get(0);
    }

    @Override
    public boolean insert(String type, String sqlId, Object reqData) {
        boolean result = false;
        try {
            result = hshySqlSessionTemplate.insert(sqlId, reqData) < 0 ? false : true;
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 插入异常！", e);
            result = false;
        }
        return result;
    }

    @Override
    public boolean delete(String type, String sqlId, Object reqData) {
        boolean result = false;
        try {
            result = hshySqlSessionTemplate.delete(sqlId, reqData) < 0 ? false : true;
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 删除异常！", e);
            result = false;
        }
        return result;
    }

    @Override
    public boolean update(String type, String sqlId, Object reqData) {
        boolean result = false;
        try {
            result = hshySqlSessionTemplate.update(sqlId, reqData) < 0 ? false : true;
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 更新异常！", e);
            result = false;
        }
        return result;
    }
    @Override
    public double queryForDouble(String type, String sqlId, Object reqData) {
        double result = 0;
        try {
            Object  object = hshySqlSessionTemplate.selectOne(sqlId, reqData);
            result = object == null?0d:(double)object;
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 查询异常！", e);
            result = 0;
        }
        return result;
    }

    @Override
    public List<String> queryForStrList(String type, String sqlId, Object reqData) {
        List<String> list = null;
        try {
            list = hshySqlSessionTemplate.selectList(sqlId, reqData);
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 查询异常！", e);
        }
        return list;
    }

    @Override
    public String querySerialNo(String type, String sqlId) {
        String serialNo="";
        try {
            serialNo = hshySqlSessionTemplate.selectOne(sqlId);
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 查询异常！", e);
        }
        return serialNo;
    }

    @Override
    public Object queryForObject(String type, String sqlId, Object reqData) {
        Object obj = null;
        try {
            obj = hshySqlSessionTemplate.selectOne(sqlId, reqData);
        } catch (Exception e) {
            logger.error("数据源：" + type + " 执行语句：" + sqlId + " 查询异常！", e);
        }
        return obj;
    }
}
