package com.company.project.core;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.company.project.core.model.ExceptionResult;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public abstract class AbstractService<T> implements Service<T> {


    @Autowired
    protected Mapper<T> mapper;

    private Class<T> modelClass;    // 当前泛型真实类型的Class

    public Mapper<T> getMapper() {
        return mapper;
    }


    public AbstractService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    @Transactional
    public void save(T model) {
        mapper.insertSelective(model);
    }

    @Override
    public void save(List<T> models) {
        mapper.insertList(models);
    }

    @Override
    public void deleteById(String id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(String ids) {
        mapper.deleteByIds(ids);
    }

    @Override
    public void update(T model) {
        mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public T findById(String id) {
        return mapper.selectByPrimaryKey(id);
    }

	@Override
	public T findBy(String fieldName, Object value) throws Exception {
		try {
			T model = modelClass.newInstance();
			Field field = modelClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(model, value);
			return mapper.selectOne(model);
		} catch (ReflectiveOperationException e) {
			throw new ExceptionResult(e.getMessage(), e);
		}
	}

    @Override
    public List<T> findByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    @Override
    public List<T> findByCondition(Condition condition) {
        return mapper.selectByCondition(condition);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public T findByKey(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    @Override
    @Transactional
    public int batchDelete(String ids, String property, Class<T> clazz) {
        List<String> list = Arrays.asList(ids.split(","));
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, list);
        return this.mapper.deleteByExample(example);
    }

    @Override
    @Transactional
    public int delete(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }

    @Override
    @Transactional
    public int updateNotNull(T model) {
        return mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public List<T> findByExample(Object example) {
        return mapper.selectByExample(example);
    }


   /* @Override
    @Transactional
    public void batchSave(List<T> list) {
        int size = list.size();
        int unitNum = 199;
        int startIndex = 0;
        int endIndex = 0;
        while (size > 0) {
            if (size > unitNum) {
                endIndex = startIndex + unitNum;
                // logger.info(" start insert :" + startIndex + " to " + endIndex);
            } else {
                endIndex = startIndex + size;
                // logger.info(" start insert :" + startIndex + " to " + endIndex);
                // logger.info("insertBatch success : " + list.size() + " data inserted ;");
            }
            List<T> insertData = list.subList(startIndex, endIndex);
            save(insertData);
            size = size - unitNum;
            startIndex = endIndex;
        }
    }
*/


}
