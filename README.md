This project contains an integration of JSR303 bean validation into
Wicket.

How to Use
----------

First we must configure the validation for our Wicket application:

	public class MyApplication extends WebApplication {
		@Override
		protected void init()
		{
			super.init();

			// create a configuration
			ValidationConfiguration conf = new ValidationConfiguration();
			
			// tweak the configuration
			
			// bind the configuration to the application
			conf.configure(this);
		}
	}		

Once the application is configured we can begin validating fields:

	TextField field=new TextField(...);
	field.add(new PropertyValidator(new SimpleProperty(Person.class, "name")));

Or if we are using a `PropertyModel`, or a `CompoundPropertyModel`, or any
model that implements `IPropertyReflectionAwareModel` there is no need to
tell the validator what property it is bound to, it can figure it out
itself:

	TextField field=new TextField(...);
	field.add(new PropertyValidator());

To validate class-level constraints we must use `ValidationForm` and give
it the bean as its model:

	Form form = new ValidationForm("form", model);
	TextField field=new TextField(...);
	field.add(new PropertyValidator(new SimpleProperty(Person.class, "name")));
	form.add(field);

Once the form is submitted its model object will be validated.

`ValidationForm` can also add `PropertyValidators` to its `FormComponents`
automatically so we dont have to, but once again we need to use one of
the supported models:

	IModel model = CompoundPropertyModel.of(bean);
	Form form = new ValidationForm("form", model);
	form.add(new TextField("name"));

In the above example the "name" `TextField` will have a `PropertyValidator`
added to it automatically.
