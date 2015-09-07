/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Robert
 */
public class ResultSetMapper<T> {

    public List<T> mapResultSetToPojo(ResultSet rs, Class outputClazz) {
        if (rs == null) {
            return null;
        }

        List<T> outputList = null;
        try {
            if (outputClazz.isAnnotationPresent(Entity.class)) {
                ResultSetMetaData rsmd = rs.getMetaData();
                Field[] fields = outputClazz.getDeclaredFields();
                while (rs.next()) {
                    T bean = (T) outputClazz.newInstance();
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        String columnName = rsmd.getColumnName(i + 1);
                        Object columValue = rs.getObject(i + 1);
                        for (Field field : fields) {
                            if (field.isAnnotationPresent(Column.class)) {
                                Column column = field.getAnnotation(Column.class);
                                if (column.name().equalsIgnoreCase(columnName) && columValue != null) {
                                    BeanUtils.setProperty(bean, field.getName(), columValue);
                                    break;
                                }
                            }
                        }
                    }
                    if (outputList == null) {
                        outputList = new ArrayList<>();
                    }
                    outputList.add(bean);
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(ResultSetMapper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputList;
    }
}
