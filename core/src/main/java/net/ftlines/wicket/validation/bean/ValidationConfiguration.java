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

import javax.validation.Configuration;

import org.apache.wicket.Application;

/**
 * Configures and builds the {@link ValidationContext}
 * 
 * @author igor
 * 
 */
public class ValidationConfiguration
{

	private Configuration<?> config;
	private IViolationConverter violationConverter = new ViolationConverter();
	private List<IPropertyResolver> propertyResolvers = new ArrayList<IPropertyResolver>();

	/**
	 * Constructor that will use a default {@link Configuration}
	 */
	public ValidationConfiguration()
	{
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param config
	 */
	public ValidationConfiguration(Configuration<?> config)
	{
		this.config = config;
		addPropertyResolver(new ModelPropertyResolver());
	}

	/**
	 * Sets violation converter
	 * 
	 * @param converter
	 */
	public ValidationConfiguration setViolationConverter(IViolationConverter converter)
	{
		if (converter == null)
		{
			throw new IllegalArgumentException("converter cannot be null");
		}
		this.violationConverter = converter;
		return this;
	}

	/**
	 * Gets violation converter
	 * 
	 * @return converter instance
	 */
	public IViolationConverter getViolationConverter()
	{
		return violationConverter;
	}

	/**
	 * Adds a property resolver
	 * 
	 * @return
	 */
	public ValidationConfiguration addPropertyResolver(IPropertyResolver resolver)
	{
		propertyResolvers.add(resolver);
		return this;
	}

	/**
	 * Builds the {@link ValidationContext} and binds it to the {@code application}
	 * 
	 * @param application
	 */
	public void configure(Application application)
	{
		if (application == null)
		{
			throw new IllegalArgumentException("application cannot be null");
		}

		if (config == null)
		{
			config = javax.validation.Validation.byDefaultProvider().configure();
		}

		ValidationContext context = new ValidationContext(config.buildValidatorFactory(), violationConverter,
			propertyResolvers);
		context.bind(application);
	}

}
