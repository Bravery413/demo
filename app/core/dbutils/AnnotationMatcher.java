package core.dbutils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationMatcher implements Matcher {
	
	private Map<String, String> fieldMappings = new HashMap<String, String>();

	public <T> AnnotationMatcher(Class<T> clazz) {
	    Class c = clazz;
	    while (c != Object.class) {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    String columnName = column.name().toLowerCase();
                    String fieldName = field.getName();
                    fieldMappings.put(columnName, fieldName);
                }
            }
            c = c.getSuperclass();
        }
	}

	@Override
	public boolean match(String columnName, String propertyName) {
		if (columnName == null) {
			return false;
		}

		String mapProperty = fieldMappings.get(columnName.toLowerCase());
		if (mapProperty == null) {
			return false;
		}
		
		if (mapProperty.equals(propertyName)) {
			return true;
		}

		return false;
	}
}
