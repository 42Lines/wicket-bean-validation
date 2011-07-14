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

/**
 * Represents validation groups
 * 
 * @author igor
 */
public interface IGroups extends Serializable
{
	/**
	 * Gets validation groups
	 * 
	 * @return array of groups or {@code null} if none
	 */
	Class<?>[] getGroups();

	/**
	 * An empty {@link IGroups} implementation
	 */
	public static final IGroups NONE = new IGroups()
	{
		private final Class<?>[] EMPTY = new Class[0];

		@Override
		public Class<?>[] getGroups()
		{
			return EMPTY;
		}

	};
}
