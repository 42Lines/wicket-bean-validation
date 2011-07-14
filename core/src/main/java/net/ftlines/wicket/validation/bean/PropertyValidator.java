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

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IModelAwareValidatable;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;

/**
 * Validates input based on constraints defined on a property or field of some object
 * 
 * @author igor
 * 
 * @param <T>
 *            type of value to validate
 */
public class PropertyValidator<T> implements INullAcceptingValidator<T>
{
	private final Serializable property;
	private final IGroups groups;

	/**
	 * Constructs a validator that will try to automatically resolve which property it is connected
	 * to, usually based on the {@link IModel} of the form component.
	 */
	public PropertyValidator()
	{
		property = null;
		groups = null;
	}

	/**
	 * Constructs a validator that will validate given the constraints of the specified
	 * {@code property}. The validator will attempt to resolve the provide {@code property} object
	 * to an {@link IProperty} using the configured {@link IPropertyResolver}s.
	 * 
	 * @param property
	 */
	public PropertyValidator(Serializable property)
	{
		this(property, null);
	}

	/**
	 * Constructs a validator that will validate constaints in the specified {@code groups} of the
	 * specified {@code property}
	 * 
	 * @param property
	 * @param groups
	 */
	public PropertyValidator(Serializable property, IGroups groups)
	{
		this.property = property;
		this.groups = groups;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void validate(IValidatable<T> validatable_)
	{
		IModelAwareValidatable<T> validatable = (IModelAwareValidatable<T>)validatable_;

		ValidationContext context = ValidationContext.get();


		IProperty property = null;

		if (this.property instanceof IProperty)
		{
			property = (IProperty)this.property;
		}

		// allow property resolver to resolve the property
		if (property == null && validatable.getModel() != null)
		{
			property = context.resolveProperty(validatable.getModel());
		}

		if (property == null)
		{
			throw new IllegalStateException("Could not resolve IProperty to validate and none were provided");
		}

		IGroups groups = this.groups != null ? this.groups : IGroups.NONE;
		Class type = property.getContainerType();

		Set<ConstraintViolation<Object>> violations = context.getValidator().validateValue(type, property.getName(),
			validatable.getValue(), groups.getGroups());

		for (ConstraintViolation<Object> violation : violations)
		{
			validatable.error(context.convert(violation));
		}
	}
}
