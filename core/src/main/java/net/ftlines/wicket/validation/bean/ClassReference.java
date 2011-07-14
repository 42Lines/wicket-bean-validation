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
import java.lang.ref.WeakReference;

import org.apache.wicket.Application;

/**
 * A serialization-safe reference to a {@link Class}
 * 
 * @author igor
 * 
 * @param <T>
 *            type of class
 */
public class ClassReference<T> implements Serializable
{
	private transient WeakReference<Class<? extends T>> cache;
	private final String name;

	/**
	 * Constructor
	 * 
	 * @param clazz
	 */
	public ClassReference(Class<? extends T> clazz)
	{
		name = clazz.getName();
		cache(clazz);
	}

	/**
	 * Gets the {@link Class} stored in this reference
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends T> get()
	{
		Class<? extends T> clazz = cache != null ? cache.get() : null;
		if (clazz == null)
		{
			try
			{
				clazz = (Class<? extends T>)Application.get()
					.getApplicationSettings()
					.getClassResolver()
					.resolveClass(name);
			}
			catch (ClassNotFoundException e)
			{
				throw new RuntimeException("Could not resolve class: " + name, e);
			}
			cache(clazz);
		}
		return clazz;
	}

	private void cache(Class<? extends T> clazz)
	{
		cache = new WeakReference<Class<? extends T>>(clazz);
	}

	/**
	 * Diamond operator factory
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> ClassReference<T> of(Class<T> clazz)
	{
		return new ClassReference<T>(clazz);
	}
}
