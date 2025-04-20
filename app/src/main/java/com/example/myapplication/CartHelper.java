package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartHelper {
    private static final String PREF_NAME = "my_cart";
    private static final String CART_KEY = "cart_items";
    private static final String CHECKOUT_CART_KEY = "checkout_items";

    public static void addToCart(Context context, Product product) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String json = prefs.getString(CART_KEY, null);
        List<Product> cartList;

        if (json != null) {
            Type type = new TypeToken<List<Product>>() {}.getType();
            cartList = gson.fromJson(json, type);
        } else {
            cartList = new ArrayList<>();
        }

        // Add product to cart
        cartList.add(product);
        prefs.edit().putString(CART_KEY, gson.toJson(cartList)).apply();
    }

    public static List<Product> getCartItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CART_KEY, null);
        if (json != null) {
            Type type = new TypeToken<List<Product>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public static void clearCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(CART_KEY).apply();
    }

    // Save selected items for checkout
    public static void saveSelectedItemsForCheckout(Context context, List<Product> selectedProducts) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(selectedProducts);
        prefs.edit().putString(CHECKOUT_CART_KEY, json).apply();
    }

    // Get items selected for checkout
    public static List<Product> getCheckoutItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CHECKOUT_CART_KEY, null);
        if (json != null) {
            Type type = new TypeToken<List<Product>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    // Clear checkout items
    public static void clearCheckoutItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(CHECKOUT_CART_KEY).apply();
    }

    // Remove checked out items from the main cart
    public static void removeCheckedOutItems(Context context, List<Product> checkedOutProducts) {
        List<Product> currentCart = getCartItems(context);
        List<Product> updatedCart = new ArrayList<>();

        // Create a map to count how many of each product we've checked out
        Map<String, Integer> checkedOutCounts = new HashMap<>();
        for (Product product : checkedOutProducts) {
            String key = product.getName();
            checkedOutCounts.put(key, checkedOutCounts.getOrDefault(key, 0) + 1);
        }

        // Keep only products that haven't been checked out
        for (Product product : currentCart) {
            String key = product.getName();
            int count = checkedOutCounts.getOrDefault(key, 0);

            if (count > 0) {
                // We're checking out this product, so decrease the count
                checkedOutCounts.put(key, count - 1);
            } else {
                // Not checking out this product, keep it in the cart
                updatedCart.add(product);
            }
        }

        // Save the updated cart
        saveCart(context, updatedCart);
    }

    // Save cart items directly
    public static void saveCart(Context context, List<Product> cartItems) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        prefs.edit().putString(CART_KEY, json).apply();
    }

    // Get cart item counts for summary display
    public static Map<String, Integer> getCartItemCounts(Context context) {
        List<Product> items = getCartItems(context);
        Map<String, Integer> itemCounts = new HashMap<>();

        for (Product product : items) {
            String name = product.getName();
            itemCounts.put(name, itemCounts.getOrDefault(name, 0) + 1);
        }

        return itemCounts;
    }

    // Get checkout item counts for summary display
    public static Map<String, Integer> getCheckoutItemCounts(Context context) {
        List<Product> items = getCheckoutItems(context);
        Map<String, Integer> itemCounts = new HashMap<>();

        for (Product product : items) {
            String name = product.getName();
            itemCounts.put(name, itemCounts.getOrDefault(name, 0) + 1);
        }

        return itemCounts;
    }
}