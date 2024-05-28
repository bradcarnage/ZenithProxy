package com.zenith.mc.food;

import com.zenith.util.Registry;

public final class FoodRegistry {
    public static final Registry<FoodData> REGISTRY = new Registry<>(41);

    public static final FoodData APPLE = register(new FoodData(799, "apple", 64, 4.0f, 19.2f, true));

    public static final FoodData MUSHROOM_STEW = register(new FoodData(849, "mushroom_stew", 1, 6.0f, 86.4f, true));

    public static final FoodData BREAD = register(new FoodData(855, "bread", 64, 5.0f, 60.0f, true));

    public static final FoodData PORKCHOP = register(new FoodData(881, "porkchop", 64, 3.0f, 10.8f, true));

    public static final FoodData COOKED_PORKCHOP = register(new FoodData(882, "cooked_porkchop", 64, 8.0f, 204.8f, true));

    public static final FoodData GOLDEN_APPLE = register(new FoodData(884, "golden_apple", 64, 4.0f, 76.8f, true));

    public static final FoodData ENCHANTED_GOLDEN_APPLE = register(new FoodData(885, "enchanted_golden_apple", 64, 4.0f, 76.8f, true));

    public static final FoodData COD = register(new FoodData(935, "cod", 64, 2.0f, 1.6f, true));

    public static final FoodData SALMON = register(new FoodData(936, "salmon", 64, 2.0f, 1.6f, true));

    public static final FoodData TROPICAL_FISH = register(new FoodData(937, "tropical_fish", 64, 1.0f, 0.4f, true));

    public static final FoodData PUFFERFISH = register(new FoodData(938, "pufferfish", 64, 1.0f, 0.4f, false));

    public static final FoodData COOKED_COD = register(new FoodData(939, "cooked_cod", 64, 5.0f, 60.0f, true));

    public static final FoodData COOKED_SALMON = register(new FoodData(940, "cooked_salmon", 64, 6.0f, 115.200005f, true));

    public static final FoodData COOKIE = register(new FoodData(980, "cookie", 64, 2.0f, 1.6f, true));

    public static final FoodData MELON_SLICE = register(new FoodData(984, "melon_slice", 64, 2.0f, 4.8f, true));

    public static final FoodData DRIED_KELP = register(new FoodData(985, "dried_kelp", 64, 1.0f, 1.2f, true));

    public static final FoodData BEEF = register(new FoodData(988, "beef", 64, 3.0f, 10.8f, true));

    public static final FoodData COOKED_BEEF = register(new FoodData(989, "cooked_beef", 64, 8.0f, 204.8f, true));

    public static final FoodData CHICKEN = register(new FoodData(990, "chicken", 64, 2.0f, 4.8f, false));

    public static final FoodData COOKED_CHICKEN = register(new FoodData(991, "cooked_chicken", 64, 6.0f, 86.4f, true));

    public static final FoodData ROTTEN_FLESH = register(new FoodData(992, "rotten_flesh", 64, 4.0f, 6.4f, false));

    public static final FoodData SPIDER_EYE = register(new FoodData(1000, "spider_eye", 64, 2.0f, 12.8f, false));

    public static final FoodData CARROT = register(new FoodData(1097, "carrot", 64, 3.0f, 21.6f, true));

    public static final FoodData POTATO = register(new FoodData(1098, "potato", 64, 1.0f, 1.2f, true));

    public static final FoodData BAKED_POTATO = register(new FoodData(1099, "baked_potato", 64, 5.0f, 60.0f, true));

    public static final FoodData POISONOUS_POTATO = register(new FoodData(1100, "poisonous_potato", 64, 2.0f, 4.8f, false));

    public static final FoodData GOLDEN_CARROT = register(new FoodData(1102, "golden_carrot", 64, 6.0f, 172.8f, true));

    public static final FoodData PUMPKIN_PIE = register(new FoodData(1111, "pumpkin_pie", 64, 8.0f, 76.8f, true));

    public static final FoodData RABBIT = register(new FoodData(1118, "rabbit", 64, 3.0f, 10.8f, true));

    public static final FoodData COOKED_RABBIT = register(new FoodData(1119, "cooked_rabbit", 64, 5.0f, 60.0f, true));

    public static final FoodData RABBIT_STEW = register(new FoodData(1120, "rabbit_stew", 1, 10.0f, 240.0f, true));

    public static final FoodData MUTTON = register(new FoodData(1131, "mutton", 64, 2.0f, 4.8f, true));

    public static final FoodData COOKED_MUTTON = register(new FoodData(1132, "cooked_mutton", 64, 6.0f, 115.200005f, true));

    public static final FoodData CHORUS_FRUIT = register(new FoodData(1150, "chorus_fruit", 64, 4.0f, 19.2f, false));

    public static final FoodData BEETROOT = register(new FoodData(1154, "beetroot", 64, 1.0f, 2.4f, true));

    public static final FoodData BEETROOT_SOUP = register(new FoodData(1156, "beetroot_soup", 1, 6.0f, 86.4f, true));

    public static final FoodData SUSPICIOUS_STEW = register(new FoodData(1190, "suspicious_stew", 1, 6.0f, 86.4f, true));

    public static final FoodData SWEET_BERRIES = register(new FoodData(1213, "sweet_berries", 64, 2.0f, 1.6f, true));

    public static final FoodData GLOW_BERRIES = register(new FoodData(1214, "glow_berries", 64, 2.0f, 1.6f, true));

    public static final FoodData HONEY_BOTTLE = register(new FoodData(1221, "honey_bottle", 16, 6.0f, 14.400001f, true));

    public static final FoodData OMINOUS_BOTTLE = register(new FoodData(1328, "ominous_bottle", 64, 1.0f, 0.4f, true));

    private static FoodData register(FoodData value) {
        REGISTRY.put(value.id(), value);
        return value;
    }
}