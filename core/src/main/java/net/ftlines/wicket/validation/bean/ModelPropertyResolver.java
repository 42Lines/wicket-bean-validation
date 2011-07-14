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

import org.apache.wicket.model.IPropertyReflectionAwareModel;

/**
 * A {@link IPropertyResolver} that knows about common Wicket models that can provide
 * {@link IProperty} information
 * 
 * @see IPropertyReflectionAwareModel
 * 
 * @author igor
 */
public class ModelPropertyResolver implements IPropertyResolver
{

	@Override
	public IProperty resolve(Object object)
	{
		if (object instanceof IPropertyReflectionAwareModel)
		{
			return new ReflectableProperty((IPropertyReflectionAwareModel)object);
		}
		return null;
	}

}
