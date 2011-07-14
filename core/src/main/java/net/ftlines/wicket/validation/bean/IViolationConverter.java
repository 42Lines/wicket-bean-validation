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

import javax.validation.ConstraintViolation;

import org.apache.wicket.validation.ValidationError;

/**
 * Converts {@link ConstraintViolation}s into Wicket {@link ValidationError}s
 * 
 * @author igor
 * 
 */
public interface IViolationConverter
{
	/**
	 * Converts a {@link ConstraintViolation} into a {@link ValidationError}
	 * 
	 * @param <T>
	 * @param violation
	 * @return
	 */
	<T> ValidationError convert(ConstraintViolation<T> violation);
}
