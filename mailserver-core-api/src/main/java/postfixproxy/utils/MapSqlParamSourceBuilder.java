package postfixproxy.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public final class MapSqlParamSourceBuilder {

	private static final MapSqlParamSourceBuilder _instance = new MapSqlParamSourceBuilder();

	public static MapSqlParamSourceBuilder getInstance() {
		return _instance;
	}

	private final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

	private MapSqlParamSourceBuilder() {

	}

	public MapSqlParamSourceBuilder add(String key, Object value) {
		this.mapSqlParameterSource.addValue(key, value);
		return this;
	}

	public MapSqlParameterSource build() {
		return this.mapSqlParameterSource;
	}

	public MapSqlParameterSource build(Object... objs) {
		try {
			for (Object obj : objs) {
				Class<?> clazz = obj.getClass();
				Field[] fields = clazz.getDeclaredFields();
				if (!ArrayUtils.isEmpty(fields)) {
					for (Field f : fields) {
						String propName = this.getPropName(f.getName());
						Object value = f.get(obj);
						this.mapSqlParameterSource.addValue(propName, value);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return this.mapSqlParameterSource;
	}

	private String getPropName(String fieldName) {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < fieldName.length(); i++) {
			char c = fieldName.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				c = (char) (c + 32);
				if (i != 0) {
					sb.append("_").append(c);
				} else {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
