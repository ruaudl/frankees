/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.frankees.maven.mojo.converter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.lang.model.element.Element;
import org.frankees.builder.BuilderDescription;
import org.frankees.builder.PropertyDescription;
import org.frankees.builder.TypeDescription;

/**
 *
 * @author Guillaume
 */
public class DescriptionConverter {

    public BuilderDescription convert(Class<?> clazz) {
        TypeDescription objectTypeDescription = new TypeDescription(
                clazz.getPackage().getName(),
                clazz.getSimpleName());
        TypeDescription builderTypeDescription = new TypeDescription(
                clazz.getPackage().getName(),
                clazz.getSimpleName() + "Builder");
        Set<PropertyDescription> properties = new HashSet<PropertyDescription>();
        final BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if (propertyDescriptor.getWriteMethod() != null) {
                    final String name = propertyDescriptor.getName();
                    final Class<?> propertyType = propertyDescriptor.getPropertyType();
                    final Package packagePropertyType = propertyType.getPackage();
                    PropertyDescription property = new PropertyDescription(name, new TypeDescription(packagePropertyType.getName(), propertyType.getSimpleName()));
                    properties.add(property);
                }
            }
            return new BuilderDescription(builderTypeDescription,
                    objectTypeDescription, properties);
        } catch (IntrospectionException ex) {
            Logger.getLogger(DescriptionConverter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
