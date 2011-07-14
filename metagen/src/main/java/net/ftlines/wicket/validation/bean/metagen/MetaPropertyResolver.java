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

package net.ftlines.wicket.validation.bean.metagen;

import net.ftlines.metagen.Property;
import net.ftlines.wicket.validation.bean.IProperty;
import net.ftlines.wicket.validation.bean.IPropertyResolver;

/**
 * {@link IPropertyResolver} that works with MetaGen properties
 * 
 * @author igor
 */
public class MetaPropertyResolver implements IPropertyResolver {

	@Override
	public IProperty resolve(Object object) {
		if (object instanceof Property) {
			return new MetaProperty((Property<?, ?>) object);
		}
		return null;
	}

}
