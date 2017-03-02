package com.ajaysinghdewari.devd.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ajay on 25-02-2017.
 */

public class Songs {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<SongItem> ITEMS = new ArrayList<SongItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SongItem> ITEM_MAP = new HashMap<String, SongItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Songs.SongItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static SongItem createDummyItem(int position) {
        return new SongItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class SongItem {
        public final String id;
        public final String content;
        public final String details;

        public SongItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
