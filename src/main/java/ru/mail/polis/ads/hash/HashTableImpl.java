package ru.mail.polis.ads.hash;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {
    private class Node {
        Key key;
        Value value;
        Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final double LOAD_FACTOR = 0.75;
    private static final int[] simpleNumbers = {223, 457, 907, 1811, 3643, 7309, 14653, 29231,
            58657, 117269, 220009, 440023, 880057};
    int size = 0;
    int indx = 0;
    int n = simpleNumbers[indx];

    LinkedList<Node>[] table = new LinkedList[n];

    public HashTableImpl() {
    }

    @Override
    public @Nullable Value get(@NotNull Key key) {
        int index = hashCode(key);
        LinkedList<Node> list = table[index];
        if (list == null) {
            return null;
        }
        for (Node node : list) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public void put(@NotNull Key key, @NotNull Value value) {
        resizeIfNeed();
        int index = hashCode(key);
        if (get(key) != null) {
            for (Node node : table[index]) {
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
            }
        } else {
            if (table[index] == null) {
                table[index] = new LinkedList<>();
            }
            table[index].add(new Node(key, value));
            size++;
        }
    }

    @Override
    public @Nullable Value remove(@NotNull Key key) {
        Value value = get(key);
        if (value != null) {
            int index = hashCode(key);
            if (table[index].size() == 1) {
                table[index] = null;
            } else {
                for (int i = 0; i < table[index].size(); i++) {
                    if (table[index].get(i).equals(key)) {
                        table[index].remove(i);
                        break;
                    }
                }
            }
            size--;
        }
        return value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void resizeIfNeed() {
        if (size() >= n * LOAD_FACTOR && indx + 1 < simpleNumbers.length) {
            indx++;
            n = simpleNumbers[indx];
            LinkedList<Node>[] temp = new LinkedList[n];
            for (LinkedList<Node> list : table) {
                if (list != null) {
                    for (Node node : list) {
                        int index = hashCode(node.key);
                        if (temp[index] == null) {
                            temp[index] = new LinkedList<>();
                        }
                        temp[index].add(node);
                    }
                }
            }
            table = temp;
        }
    }

    private int hashCode(Key key) {
        return Math.abs(key.hashCode() % n);
    }
}
