package com.mafei.server;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mafei
 * @Created 6/27/2021 9:40 PM
 */
public class GameData {
    private static final Map<Integer, Integer> DATA = new HashMap<>();

    static {
        //ladders located locations on the board [start and end].
        DATA.put(1, 38);
        DATA.put(4, 14);
        DATA.put(8, 30);
        DATA.put(21, 42);
        DATA.put(28, 76);
        DATA.put(50, 67);
        DATA.put(71, 92);
        DATA.put(80, 99);
        //snakes located locations on the board [start and end].
        DATA.put(32, 10);
        DATA.put(36, 6);
        DATA.put(48, 26);
        DATA.put(62, 18);
        DATA.put(88, 24);
        DATA.put(95, 56);
        DATA.put(97, 78);

    }

    public static int getPosition(int position) {
        return DATA.getOrDefault(position, position);
    }
}
