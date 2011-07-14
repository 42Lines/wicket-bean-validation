/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ftlines.wicket.validation.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.wicket.model.IPropertyReflectionAwareModel;

/**
 * {@link IProperty} implementation based on a {@link Field} or a getter or setter a {@link Method}
 * 
 * @author igor
 * 
 */
public class ReflectableProperty implements IProperty
{
	private String name;
	private ClassReference<?> containerType;


	/**
	 * Constructs a property based on a field
	 * 
	 * @param field
	 */
	public ReflectableProperty(Field field)
	{
		init(field);
	}

	private void init(Field field)
	{
		name = field.getName();
		containerType = ClassReference.of(field.getDeclaringClass());
	}

	/**
	 * Constructs a property based on a getter or a setter method
	 * 
	 * @param field
	 */
	public ReflectableProperty(Method method)
	{
		init(method);
	}

	private void init(Method method)
	{
		if (method.getName().startsWith("get") && method.getName().length() > 3)
		{
			name = method.getName();
			name = name.substring(3, 4).toLowerCase() + (name.length() > 4 ? name.substring(4) : "");
			containerType = ClassReference.of(method.getDeclaringClass());
		}
		else if (method.getName().startsWith("set") && method.getName().length() > 3 &&
			method.getParameterTypes().length == 1)
		{
			name = method.getName();
			name = name.substring(3, 4).toLowerCase() + (name.length() > 4 ? name.substring(4) : "");
			containerType = ClassReference.of(method.getDeclaringClass());
		}
		else
		{
			throw new IllegalArgumentException("Method: " + method + " is neither getter nor setter");
		}
	}

	/**
	 * Creates a property based on a {@link IPropertyReflectionAwareModel} model
	 * 
	 * @param model
	 */
	public ReflectableProperty(IPropertyReflectionAwareModel model)
	{
		if (model.getPropertyGetter() != null)
		{
			init(model.getPropertyGetter());
		}
		else if (model.getPropertySetter() != null)
		{
			init(model.getPropertySetter());
		}
		else
		{
			init(model.getPropertyField());
		}
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Class<?> getContainerType()
	{
		return containerType.get();
	}

}
