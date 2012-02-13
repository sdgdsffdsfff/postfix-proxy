package postfixproxy.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

/**
 * 自动构建RowMapper对象
 * 
 * @author hongze.chi@gmail.com
 * 
 */
public final class RowMapperBuilder {

	private RowMapperBuilder() {

	}

	@Target({ ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface EnumField {
		String value(); // 映射方法名

		Class<?> argType() default int.class; // 映射方法接受参数类型
	}

	public static <T> RowMapper<T> buildRowMapper(final Class<T> clazz,
			final String props) {
		RowMapper<T> rowMapper = new RowMapper<T>() {

			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				try {
					String[] propList = getPropList(props);
					T obj = clazz.newInstance();
					for (String prop : propList) {
						String fieldName = getFiledName(prop);
						Field field = clazz.getDeclaredField(fieldName);
						fillField(prop, field, rs, obj);
					}
					return obj;
				} catch (Exception e) {
					throw new RuntimeException("Ops!", e);
				}
			}

		};

		return rowMapper;
	}

	private static void fillField(String prop, Field field, ResultSet rs,
			Object obj) throws Exception {
		field.setAccessible(true);
		Class<?> fieldClazz = field.getType();
		Object value = null;
		// 有一种东西叫枚举
		if (fieldClazz.isEnum()) {
			EnumField enumFieldAnn = field.getAnnotation(EnumField.class);
			if (enumFieldAnn != null) {
				String mapMtdName = enumFieldAnn.value();
				Class<?> argType = enumFieldAnn.argType();
				Method mtd = fieldClazz.getDeclaredMethod(mapMtdName, argType);
				mtd.setAccessible(true);
				if (mtd != null) {
					value = mtd.invoke(fieldClazz,
							getObjValueByField(argType, rs, prop));
				}
			} else {
				throw new RuntimeException(
						"Must target the enum with EnumField annotation!");
			}
		} else {
			value = getObjValueByField(fieldClazz, rs, prop);
		}

		field.set(obj, value);
	}

	private static Object getObjValueByField(Class<?> fieldType, ResultSet rs,
			String prop) throws Exception {
		if (fieldType == byte.class || fieldType == Byte.class) {
			return rs.getByte(prop);
		} else if (fieldType == short.class || fieldType == Short.class) {
			return rs.getShort(prop);
		} else if (fieldType == int.class || fieldType == Integer.class) {
			return rs.getInt(prop);
		} else if (fieldType == long.class || fieldType == Long.class) {
			return rs.getLong(prop);
		} else if (fieldType == float.class || fieldType == Float.class) {
			return rs.getFloat(prop);
		} else if (fieldType == double.class || fieldType == Double.class) {
			return rs.getDouble(prop);
		} else if (fieldType == boolean.class || fieldType == Boolean.class) {
			return rs.getBoolean(prop);
		} else if (fieldType == String.class) {
			return rs.getString(prop);
		} else if (fieldType == Date.class) {
			Timestamp tsp = rs.getTimestamp(prop);
			if (tsp != null) {
				return new Date(tsp.getTime());
			}
			return null;
		} else {
			return rs.getObject(prop);
		}
	}
	
	private static String[] getPropList(String props) {
        if (StringUtils.isEmpty(props)) {
            throw new RuntimeException("参数列表不能为空!");
        }
        String[] propList = props.split(",");
        if (!ArrayUtils.isEmpty(propList)) {
            for (int i = 0; i < propList.length; i++) {
                String prop = propList[i];
                prop = prop.trim();
                if (prop.matches("^`\\w+`$")) {
                    prop = prop.substring(1, prop.length() - 1);
                }
                propList[i] = prop;
            }
            return propList;
        }
        throw new RuntimeException("参数列表为空!");
    }
	
	private static String getFiledName(String prop) {
        StringBuilder filedNameBuilder = new StringBuilder();
        for (int i = 0; i < prop.length(); i++) {
            char c = prop.charAt(i);
            if (c == '_') {
                char nextC = prop.charAt(++i);
                nextC = (char) (nextC - 32);
                filedNameBuilder.append(nextC);
                continue;
            }
            filedNameBuilder.append(c);
        }
        return filedNameBuilder.toString();
    }
}
