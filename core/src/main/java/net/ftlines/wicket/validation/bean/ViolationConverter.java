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
import javax.validation.metadata.ConstraintDescriptor;

import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.ValidationError;

/**
 * A default implementation of {@link IViolationConverter}
 * 
 * TODO more docs about the actual conversion process
 * 
 * @author igor
 * 
 */
public class ViolationConverter implements IViolationConverter
{
	@Override
	public <T> ValidationError convert(ConstraintViolation<T> violation)
	{
		ConstraintDescriptor<?> desc = violation.getConstraintDescriptor();

		ValidationError error = new ValidationError();
		error.setMessage(violation.getMessage());
		addMessageKey(error, desc, "message");

		// TODO figure out how to handle composite constraints, see desc.isReportAsSingleViolation()

		for (String key : desc.getAttributes().keySet())
		{
			error.setVariable(key, desc.getAttributes().get(key));
		}
		return error;
	}


	protected void addMessageKey(ValidationError error, ConstraintDescriptor<?> desc, String attr)
	{
		final Object val = desc.getAttributes().get(attr);
		if (val != null)
		{
			String str = val.toString();
			if (!Strings.isEmpty(str) && str.startsWith("{") && str.endsWith("}"))
			{
				error.addMessageKey(str.substring(1, str.length() - 1));
			}
		}
	}

}
