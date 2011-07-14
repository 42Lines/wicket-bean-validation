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

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.validation.ValidationError;

/**
 * The context for validation within a Wicket application
 * 
 * @author igor
 * 
 */
public class ValidationContext
{
	private static final MetaDataKey<ValidationContext> METAKEY = new MetaDataKey<ValidationContext>()
	{
	};

	private final ValidatorFactory validatorFactory;
	private final IViolationConverter errorFactory;
	private final List<IPropertyResolver> propertyResolvers = new ArrayList<IPropertyResolver>();

	ValidationContext(ValidatorFactory validatorFactory, IViolationConverter errorFactory,
		List<IPropertyResolver> propertyResolvers)
	{
		this.validatorFactory = validatorFactory;
		this.errorFactory = errorFactory;
		this.propertyResolvers.addAll(propertyResolvers);
	}

	/**
	 * Gets a {@link Validator} instance
	 * 
	 * @return
	 */
	public Validator getValidator()
	{
		return validatorFactory.usingContext()
			.messageInterpolator(new SessionLocaleInterpolator(validatorFactory.getMessageInterpolator()))
			.getValidator();
	}

	/**
	 * Converts a {@link ConstraintViolation} into a {@link ValidationError} using the
	 * {@link IViolationConverter} specified in the {@link ValidationContext}
	 * 
	 * @param violation
	 * @return
	 */
	public ValidationError convert(ConstraintViolation<?> violation)
	{
		return errorFactory.convert(violation);
	}

	public IProperty resolveProperty(Object object)
	{
		for (IPropertyResolver resolver : propertyResolvers)
		{
			IProperty property = resolver.resolve(object);
			if (property != null)
			{
				return property;
			}
		}
		return null;
	}

	void bind(Application application)
	{
		application.setMetaData(METAKEY, this);
	}

	/**
	 * Gets the {@link ValidationContext} associated with the specified {@code application}
	 * 
	 * @param application
	 * @return
	 */
	public static ValidationContext get(Application application)
	{
		ValidationContext validation = application.getMetaData(METAKEY);
		if (validation == null)
		{
			throw new IllegalStateException("No ValidationConfiguration instance bound to Application");
		}
		return validation;
	}

	/**
	 * Gets the {@link ValidationContext} associated with the thread's current {@link Application}
	 * instance
	 * 
	 * @return
	 */
	public static ValidationContext get()
	{
		return get(Application.get());
	}

}
