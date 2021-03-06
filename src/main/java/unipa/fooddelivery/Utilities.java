package unipa.fooddelivery;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import unipa.fooddelivery.models.*;

public class Utilities
 {

    public static List<Dish> filterDishesByCategory(List<Dish> dishes, DishCategory category) {
        
        return dishes.stream()
                     .filter(x -> x.getCategory().equals(category))
                     .collect(Collectors.toList());
    }

    public static double getTotal(Hashtable<Dish, Integer> dishes, double deliveryFee) {
         
        return dishes.entrySet().stream().mapToDouble(x -> x.getKey().getPrice() * x.getValue()).sum();
    }

    public static Hashtable<Dish, Integer> getDishesFromIDs(Hashtable<Long, Integer> dishesIDs)
	{
		Hashtable<Dish, Integer> dishes = new Hashtable<>();
		var dishesFromDB = DataBase.getInstance().getDishes();
			
		dishesIDs.forEach((k, v) -> {
			var optionalDish = dishesFromDB.stream().filter(x -> x.getId() == k).findFirst();

			if(optionalDish.isPresent())
				dishes.put(optionalDish.get(), v);
		});

		return dishes;
	}

	public static List<Restaurant> getRestaurantsFromDishes(Map<Dish, Integer> dishes) {
		return dishes.entrySet()
					 .stream()
					 .map(x -> x.getKey().getRestaurant())
					 .filter(distinctByKey(Restaurant::getId))
					 .collect(Collectors.toList());
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}
}