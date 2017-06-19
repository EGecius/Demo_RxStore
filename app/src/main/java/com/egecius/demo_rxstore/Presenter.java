package com.egecius.demo_rxstore;


import java.io.File;
import java.util.Arrays;
import java.util.List;

import au.com.gridstone.rxstore.Converter;
import au.com.gridstone.rxstore.ListStore;
import au.com.gridstone.rxstore.RxStore;
import au.com.gridstone.rxstore.ValueStore;
import au.com.gridstone.rxstore.converters.MoshiConverter;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class Presenter {

	private final Converter converter = new MoshiConverter();
	private final ValueStore<Person> personStore = RxStore.value(new File("person"), converter, Person.class);
	private final ListStore<Person> peopleStore = RxStore.list(new File("people"), converter, Person.class);


	public Presenter() {
		observeValues();
	}

	private void observeValues() {
		// We can subscribe for updates every time our personStore changes.
		personStore.observe()
				.filter(new Predicate<ValueStore.ValueUpdate<Person>>() {
					@Override
					public boolean test(@NonNull final ValueStore.ValueUpdate<Person> update)
							throws Exception {
						return !update.empty;
					}
				}) // We might only want non-empty updates.
				.map(new Function<ValueStore.ValueUpdate<Person>, Object>() {
					@Override
					public Object apply(@NonNull final ValueStore.ValueUpdate<Person> update)
							throws Exception {
						return update.value;
					}
				}) // If updates are non-empty we can get a non-null Person.
				.subscribe(new Consumer<Object>() {
					@Override
					public void accept(@NonNull final Object person) throws Exception {
						// Here we receive a new person every time the store updates.
					}
				});

		// We can do the same for our ListStore of many people.
		peopleStore.observe().subscribe(new Consumer<List<Person>>() {
			@Override
			public void accept(@NonNull final List<Person> people) throws Exception {
				// Now we'll be informed every time our list of people updates.
			}
		});
	}

	public void onAddClicked(final String name) {
		addPerson(name);
	}

	private void addPerson(final String name) {
		// Make some model objects to persist.
		Person person = new Person(name, 30);


		// Here we'll write one person to disk and observe the operation.
		personStore.observePut(person).subscribe(new Consumer<Person>() {
			@Override
			public void accept(@NonNull final Person person) throws Exception {
				// In the onSuccess callback we now have a persisted model object to work with.
			}
		});

		// We can add one person at a time to our ListStore if we like.
		peopleStore.add(person, Schedulers.trampoline());
	}

	public void addPeople() {
		List<Person> manyPeople = Arrays.asList(
				new Person("Trevor Tester", 24),
				new Person("Edward Example", 61),
				new Person("Daphne Demonstration", 9)
		);

		// Write many people to a ListStore. Rather than observe the operation this time we'll
		// just fire
		// and forget. We specify the Scheduler as trampoline to keep it simple and synchronous.
		peopleStore.put(manyPeople, Schedulers.trampoline());
	}

	public void clearValues() {
		// If we don't need our data persisted anymore we can clear the files from disk.
		personStore.clear(Schedulers.trampoline());
		peopleStore.clear(Schedulers.trampoline());
	}

	public void remove() {
		// Let's remove Edward from our ListStore and observe the operation.
		peopleStore.observeRemove(new ListStore.PredicateFunc<Person>() {
			@Override
			public boolean test(@NonNull final Person person) {
				return person.name.contains("Edward");
			}
		}).subscribe(new Consumer<List<Person>>() {
			@Override
			public void accept(@NonNull final List<Person> people) throws Exception {
				// Now we have a list of people without Edward.
			}
		});
	}
}
