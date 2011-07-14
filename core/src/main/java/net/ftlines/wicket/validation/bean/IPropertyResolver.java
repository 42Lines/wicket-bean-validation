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

/**
 * Resolves {@link IProperty} from an {@link Object}
 * 
 * {@link ValidationConfiguration} allows registration of custom {@link IPropertyResolver} to allow
 * applications to resolve properties from application-specific model objects.
 * 
 * @see ValidationConfiguration#setPropertyResolver(IPropertyResolver)
 * 
 * @author igor
 * 
 */
public interface IPropertyResolver
{
	/**
	 * Attempts to resolve {@link IProperty} from an object
	 * 
	 * @param object
	 * @return IProperty instance or {@code null}
	 */
	IProperty resolve(Object object);
}
