/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.utils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for collection operations.
 * Provides safe and convenient collection manipulations.
 * 
 * @author tai.buivan
 * @version 1.0
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // Private constructor to prevent instantiation
    }


    /**
     * Creates a mutable list from varargs
     * 
     * @param elements The elements
     * @param <T>      Element type
     * @return New ArrayList
     */
    @SafeVarargs
    public static <T> List<T> listOf(T... elements) {
        if (elements == null || elements.length == 0) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(elements));
    }

    /**
     * Gets first element of list safely
     * 
     * @param list The list
     * @param <T>  Element type
     * @return Optional containing first element
     */
    public static <T> Optional<T> first(List<T> list) {
        if (ObjectUtils.isEmpty(list))
            return Optional.empty();
        return Optional.ofNullable(list.get(0));
    }

    /**
     * Gets last element of list safely
     * 
     * @param list The list
     * @param <T>  Element type
     * @return Optional containing last element
     */
    public static <T> Optional<T> last(List<T> list) {
        if (ObjectUtils.isEmpty(list))
            return Optional.empty();
        return Optional.ofNullable(list.get(list.size() - 1));
    }

    /**
     * Gets element at index safely
     * 
     * @param list  The list
     * @param index The index
     * @param <T>   Element type
     * @return Optional containing element
     */
    public static <T> Optional<T> getAt(List<T> list, int index) {
        if (ObjectUtils.isEmpty(list) || index < 0 || index >= list.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(index));
    }

    /**
     * Safely sublist with bounds checking
     * 
     * @param list The list
     * @param from Start index (inclusive)
     * @param to   End index (exclusive)
     * @param <T>  Element type
     * @return Sublist
     */
    public static <T> List<T> subListSafe(List<T> list, int from, int to) {
        if (ObjectUtils.isEmpty(list))
            return new ArrayList<>();
        int safeFrom = Math.max(0, from);
        int safeTo = Math.min(list.size(), to);
        if (safeFrom >= safeTo)
            return new ArrayList<>();
        return new ArrayList<>(list.subList(safeFrom, safeTo));
    }

    /**
     * Partitions list into chunks of specified size
     * 
     * @param list The list to partition
     * @param size Chunk size
     * @param <T>  Element type
     * @return List of chunks
     */
    public static <T> List<List<T>> partition(List<T> list, int size) {
        if (ObjectUtils.isEmpty(list) || size <= 0)
            return new ArrayList<>();
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            result.add(subListSafe(list, i, i + size));
        }
        return result;
    }


    /**
     * Creates a mutable map from key-value pairs
     * 
     * @param pairs Key-value pairs (must be even number of arguments)
     * @param <K>   Key type
     * @param <V>   Value type
     * @return New HashMap
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapOf(Object... pairs) {
        if (pairs == null || pairs.length == 0) {
            return new HashMap<>();
        }
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Must provide even number of arguments");
        }
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put((K) pairs[i], (V) pairs[i + 1]);
        }
        return map;
    }

    /**
     * Gets value from map safely with default
     * 
     * @param map          The map
     * @param key          The key
     * @param defaultValue Default value
     * @param <K>          Key type
     * @param <V>          Value type
     * @return Value or default
     */
    public static <K, V> V getOrDefault(Map<K, V> map, K key, V defaultValue) {
        if (map == null || key == null)
            return defaultValue;
        return map.getOrDefault(key, defaultValue);
    }


    /**
     * Maps list elements to new type
     * 
     * @param list   Source list
     * @param mapper Mapping function
     * @param <T>    Source type
     * @param <R>    Result type
     * @return Mapped list
     */
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        if (ObjectUtils.isEmpty(list))
            return new ArrayList<>();
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * Filters list by predicate
     * 
     * @param list      Source list
     * @param predicate Filter condition
     * @param <T>       Element type
     * @return Filtered list
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        if (ObjectUtils.isEmpty(list))
            return new ArrayList<>();
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Converts list to map by key extractor
     * 
     * @param list         Source list
     * @param keyExtractor Function to extract key
     * @param <K>          Key type
     * @param <V>          Value type
     * @return Map of elements
     */
    public static <K, V> Map<K, V> toMap(List<V> list, Function<V, K> keyExtractor) {
        if (ObjectUtils.isEmpty(list))
            return new HashMap<>();
        return list.stream().collect(Collectors.toMap(keyExtractor, Function.identity(), (a, b) -> b));
    }

    /**
     * Groups list by classifier
     * 
     * @param list       Source list
     * @param classifier Grouping function
     * @param <K>        Key type
     * @param <V>        Value type
     * @return Grouped map
     */
    public static <K, V> Map<K, List<V>> groupBy(List<V> list, Function<V, K> classifier) {
        if (ObjectUtils.isEmpty(list))
            return new HashMap<>();
        return list.stream().collect(Collectors.groupingBy(classifier));
    }

    /**
     * Flattens nested lists
     * 
     * @param lists List of lists
     * @param <T>   Element type
     * @return Flattened list
     */
    public static <T> List<T> flatten(List<List<T>> lists) {
        if (ObjectUtils.isEmpty(lists))
            return new ArrayList<>();
        return lists.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Removes duplicates from list preserving order
     * 
     * @param list Source list
     * @param <T>  Element type
     * @return List with unique elements
     */
    public static <T> List<T> distinct(List<T> list) {
        if (ObjectUtils.isEmpty(list))
            return new ArrayList<>();
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Removes null elements from list
     * 
     * @param list Source list
     * @param <T>  Element type
     * @return List without nulls
     */
    public static <T> List<T> removeNulls(List<T> list) {
        if (ObjectUtils.isEmpty(list))
            return new ArrayList<>();
        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }


    /**
     * Finds intersection of two collections
     * 
     * @param c1  First collection
     * @param c2  Second collection
     * @param <T> Element type
     * @return Set of common elements
     */
    public static <T> Set<T> intersection(Collection<T> c1, Collection<T> c2) {
        if (ObjectUtils.isEmpty(c1) || ObjectUtils.isEmpty(c2))
            return new HashSet<>();
        Set<T> result = new HashSet<>(c1);
        result.retainAll(c2);
        return result;
    }

    /**
     * Finds union of two collections
     * 
     * @param c1  First collection
     * @param c2  Second collection
     * @param <T> Element type
     * @return Set of all elements
     */
    public static <T> Set<T> union(Collection<T> c1, Collection<T> c2) {
        Set<T> result = new HashSet<>();
        if (c1 != null)
            result.addAll(c1);
        if (c2 != null)
            result.addAll(c2);
        return result;
    }

    /**
     * Finds difference (c1 - c2)
     * 
     * @param c1  First collection
     * @param c2  Second collection
     * @param <T> Element type
     * @return Set of elements in c1 but not in c2
     */
    public static <T> Set<T> difference(Collection<T> c1, Collection<T> c2) {
        if (ObjectUtils.isEmpty(c1))
            return new HashSet<>();
        Set<T> result = new HashSet<>(c1);
        if (c2 != null)
            result.removeAll(c2);
        return result;
    }
}
