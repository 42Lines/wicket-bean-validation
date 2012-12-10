/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.ftlines.wicket.validation.bean;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.interpolator.MapVariableInterpolator;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidator;

/**
 * Bean validation aware form. This form can perform such tasks as:
 * <ul>
 * <li>Validate its model object</li>
 * <li>Add {@link PropertyValidator}s to its child form components</li>
 * </ul>
 * These features are controlled via setters on this class, all features are enabled by default.
 * 
 * @author igor
 * 
 * @param <T>
 */
public class ValidationForm<T> extends Form<T>
{
	private static final short FLAG_VALIDATE_MODEL_OBJECT = 0x01;
	private static final short FLAG_AUTOADD_VALIDATORS = 0x02;

	private short flags = FLAG_VALIDATE_MODEL_OBJECT | FLAG_AUTOADD_VALIDATORS;
	private IGroups groups;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 */
	public ValidationForm(String id, IModel<T> model)
	{
		super(id, model);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public ValidationForm(String id)
	{
		super(id);
	}

	public ValidationForm<T> setGroups(IGroups groups)
	{
		this.groups = groups;
		return this;
	}

	private void setFormFlag(short flag, boolean value)
	{
		if (value)
		{
			flags |= flag;
		}
		else
		{
			flags &= ~flag;
		}
	}

	private boolean getFormFlag(short flag)
	{
		return (flags & flag) != 0;
	}

	/**
	 * Checks whether or not model object will be validated. Validating the model object can catch
	 * errors which property validators attached to individual components may not, such as
	 * class-level constraints.
	 * 
	 * @return {@code true} iff model object will be validated
	 */
	public final boolean getValidateModelObject()
	{
		return getFormFlag(FLAG_VALIDATE_MODEL_OBJECT);
	}

	/**
	 * Sets whether or not model object will be validated, see {@link #getValidateModelObject()}
	 * 
	 * @param value
	 * @return {@code this}
	 */
	public final ValidationForm<T> setValidateModelObject(boolean value)
	{
		setFormFlag(FLAG_VALIDATE_MODEL_OBJECT, value);
		return this;
	}

	/**
	 * Checks whether or not property validators will be added to child form components. If set to
	 * {@code true} {@link PropertyValidator} instances will be added to all child form components
	 * for which an {@link IProperty} can be resolved using one of the registered
	 * {@link IPropertyResolver}s. The addition of these validators happens during the
	 * {@link #onBeforeRender()} stage.
	 * 
	 * @return {@code true} iff property validators should be added
	 */
	public final boolean getAutoAddPropertyValidators()
	{
		return getFormFlag(FLAG_AUTOADD_VALIDATORS);
	}

	/**
	 * Sets whether or not property validators should be added to child form components, see
	 * {@link #getAutoAddPropertyValidators()}
	 * 
	 * @param value
	 * @return {@code this}
	 */
	public ValidationForm<T> setAutoAddPropertyValidators(boolean value)
	{
		setFormFlag(FLAG_AUTOADD_VALIDATORS, value);
		return this;
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		if (!getAutoAddPropertyValidators())
		{
			return;
		}

		addPropertyValidators();
	}

	protected void addPropertyValidators()
	{
		final ValidationContext validation = ValidationContext.get();

		visitChildren(FormComponent.class, new IVisitor<FormComponent<?>, Void>()
		{
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void component(FormComponent<?> component, IVisit<Void> visit)
			{
				if (!hasPropertyValidator(component))
				{
					IProperty property = validation.resolveProperty(component.getModel());
					if (property != null)
					{
						component.add(new PropertyValidator(null, groups));
					}
				}
			}
		});
	}

	private boolean hasPropertyValidator(FormComponent<?> fc)
	{
		for (IValidator<?> validator : fc.getValidators())
		{
			if (validator instanceof PropertyValidator)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onValidateModelObjects()
	{
		super.onValidateModelObjects();
		if (getFormFlag(FLAG_VALIDATE_MODEL_OBJECT) && getModel() != null)
		{
			validateFormModelObject();
		}
	}

	protected void validateFormModelObject()
	{
		final Object bean = getModelObject();

		// validate the bean

		IGroups groups = this.groups != null ? this.groups : IGroups.NONE;

		ValidationContext context = ValidationContext.get();
		Set<ConstraintViolation<Object>> violations = context.getValidator().validate(bean, groups.getGroups());

		for (ConstraintViolation<Object> violation : violations)
		{
			error(context.convert(violation).getErrorMessage(new FormErrorSource(this)));
		}
	}

	private static class FormErrorSource implements IErrorMessageSource
	{
		private final Form<?> form;

		private FormErrorSource(Form<?> form)
		{
			this.form = form;
		}

		@Override
		public String getMessage(String key, Map<String, Object> vars)
		{
			String s = form.getString(key);
			return new MapVariableInterpolator(s, vars, Application.get()
				.getResourceSettings()
				.getThrowExceptionOnMissingResource()).toString();
		}
	}

}
