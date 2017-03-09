/*
 * Copyright 2002-2016 XianYu Game Co. Ltd, The rashomon Project
 */

package tank.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;


/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/1/17
 * @Version: 1.0
 * @Description:
 */
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);

        objectMapper.setSerializationInclusion(Include.NON_NULL);// null 不参与转换
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式转换
        objectMapper.setDateFormat(dateFormat);
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("将对象序列化成JSON字符串时发生解析异常", e);
        }
    }

    /**
     * 将指定的JSON格式的字符串解析成指定类型的对象
     *
     * @param <T>  返回的对象类型
     * @param json 要解析成对象的JSON格式字符串
     * @param type 指定要解析成对象的类型
     * @return 返回解析对象
     */
    public static <T> T toBean(String json, Class<T> type) {
        if (json == null) {
            return null;
        }

        try {
            return objectMapper.readValue(json, type);
        } catch (JsonParseException e) {
            throw new RuntimeException("解析异常", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("字段映射异常", e);
        } catch (IOException e) {
            throw new RuntimeException("IO异常", e);
        }
    }

    public static <T> T toBean(String json, Class<T> type, T defaultValue) {
        T bean = toBean(json, type);
        return bean == null ? defaultValue : bean;
    }

    /**
     * 将指定的JSON格式的字符串解析成指定类型的对象
     *
     * @param <T>          返回的对象类型
     * @param json         要解析成对象的JSON格式字符串
     * @param valueTypeRef 指定要解析成对象的类型
     * @return 返回解析对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String json, TypeReference<?> valueTypeRef) {
        try {
            if (!StringUtils.isEmpty(json)) {
                return (T) objectMapper.readValue(json, valueTypeRef);
            }
            return null;
        } catch (JsonParseException e) {
            throw new RuntimeException("解析异常", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("字段映射异常", e);
        } catch (IOException e) {
            throw new RuntimeException("IO异常", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(String json, TypeReference<?> valueTypeRef, T defaultValue) {
        try {
            if (!StringUtils.isEmpty(json)) {
                return (T) objectMapper.readValue(json, valueTypeRef);
            }
            return defaultValue;
        } catch (JsonParseException e) {
            throw new RuntimeException("解析异常, Json:" + json + ", typeRef" + valueTypeRef + "异常信息：", e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("字段映射异常, Json:" + json + ", typeRef" + valueTypeRef + "异常信息：", e);
        } catch (IOException e) {
            throw new RuntimeException("IO异常, Json:" + json + ", typeRef" + valueTypeRef + "异常信息：", e);
        }
    }


}
