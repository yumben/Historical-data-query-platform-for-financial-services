package cn.com.infohold.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.bson.BSON;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

import cn.com.infohold.basic.util.json.BasicJsonUtil;

public class IntactCheckUtil {
	public static boolean check(Object obj) {
		List<Field> list = Arrays.asList(obj.getClass().getDeclaredFields());
		for (int i = 0; i < list.size(); i++) {
			Field field = list.get(i);
			if (field.isAnnotationPresent(TableField.class) || field.isAnnotationPresent(TableId.class)) {// 是否使用MyAnno注解
				for (Annotation anno : field.getDeclaredAnnotations()) {
					if (anno.annotationType().equals(TableId.class)) {// 找到TableId的注解
						System.out.println("pk:" + ((TableId) anno).value());
						System.out.println("field:" + ((TableId) anno).value());
					} else {
						System.out.println("field:" + ((TableField) anno).value());
					}
				}
			}
		}
		return true;
	}

	public static BSON initWrapper(Object obj) throws IOException {
		BSON bson=new BSON();
		String text = BasicJsonUtil.getInstance().toJsonString(obj);
		System.out.println(text);
		JSONObject jsonObject=JSONObject.parseObject(text);
		List<Field> list = Arrays.asList(obj.getClass().getDeclaredFields());
		for (int i = 0; i < list.size(); i++) {
			Field field = list.get(i);
			if (field.isAnnotationPresent(TableField.class) || field.isAnnotationPresent(TableId.class)) {// 是否使用MyAnno注解
				for (Annotation anno : field.getDeclaredAnnotations()) {
					if (anno.annotationType().equals(TableId.class)) {// 找到TableId的注解
						System.out.println(((TableId) anno).value() + "----->"+jsonObject.getString(field.getName()));
					} else {
						System.out.println(((TableField) anno).value() + "----->"+jsonObject.getString(field.getName()));
					}
				}
			}
		}
		return bson;
	}



}
