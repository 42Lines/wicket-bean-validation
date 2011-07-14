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

package net.ftlines.wicket.validation.bean.examples.basic;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import net.ftlines.wicket.validation.bean.examples.validator.Range;

import org.hibernate.validator.constraints.NotEmpty;

@Range(lhsField = "minSize", rhsField = "maxSize", message="{FileSearch.range.size.error}")
public class FileSearch implements Serializable
{
	@NotEmpty
	@Size(max = 10)
	private String filename;

	@Min(0)
	private Integer minSize;

	@Min(0)
	private Integer maxSize;

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public Integer getMinSize()
	{
		return minSize;
	}

	public void setMinSize(Integer minSize)
	{
		this.minSize = minSize;
	}

	public Integer getMaxSize()
	{
		return maxSize;
	}

	public void setMaxSize(Integer maxSize)
	{
		this.maxSize = maxSize;
	}


}
