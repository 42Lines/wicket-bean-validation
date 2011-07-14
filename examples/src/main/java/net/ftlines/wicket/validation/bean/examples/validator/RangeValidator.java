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

package net.ftlines.wicket.validation.bean.examples.validator;

import java.beans.PropertyDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeValidator implements ConstraintValidator<Range, Object>
{

	private String lhsField, rhsField;

	@Override
	public void initialize(Range annot)
	{
		lhsField = annot.lhsField();
		rhsField = annot.rhsField();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context)
	{

		try
		{
			Comparable lhs = (Comparable)new PropertyDescriptor(lhsField, value.getClass()).getReadMethod().invoke(
				value);
			Comparable rhs = (Comparable)new PropertyDescriptor(rhsField, value.getClass()).getReadMethod().invoke(
				value);

			if (lhs == null || rhs == null)
			{
				return true;
			}
			else
			{
				boolean error = lhs.compareTo(rhs) > 0;
				return !error;
			}

		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
