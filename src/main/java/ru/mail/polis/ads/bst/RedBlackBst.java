package ru.mail.polis.ads.bst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RedBlackBst<Key extends Comparable<Key>, Value>
        implements Bst<Key, Value> {
    private Node root;
    private int size;
    private static final boolean BLACK = false;
    private static final boolean RED = true;

    private class Node {
        Key key;
        Value value;
        Node left;
        Node right;
        boolean color;

        public Node(Key key, Value value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    @Nullable
    @Override
    public Value get(@NotNull Key key) {
        return get(root, key);
    }

    private Value get(Node node, Key key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) < 0) {
            return get(node.left, key);
        }
        if (key.compareTo(node.key) > 0) {
            return get(node.right, key);
        }
        return node.value;
    }

    @Override
    public void put(@NotNull Key key, @NotNull Value value) {
        root = put(root, key, value);
        root.color = BLACK;
    }

    private Node put(Node node, Key key, Value value) {
        if (node == null) {
            size++;
            return new Node(key, value, RED);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = put(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        return fix(node);
    }


    private Node fix(Node node) {
        if (!isRed(node.left) && isRed(node.right)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColours(node);
        }
        return node;
    }

    private Node flipColours(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
        return node;
    }

    @Nullable
    @Override
    public Value remove(@NotNull Key key) {
        Value rez = get(root, key);
        if (rez == null) {
            return null;
        }
        root = remove(root, key);
        size--;
        return rez;
    }

    private Node remove(Node node, Key key) {
        if (node == null) {
            return null;
        }
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            if (node.left != null) {
                if (!isRed(node.left) && !isRed(node.left.left)) { // если 2 черных узла подряд
                    node = moveRedLeft(node);
                }
                node.left = remove(node.left, key);
            }
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
                node.right = remove(node.right, key);
            }
            else if (compare == 0 && node.right == null) {
                return null;
            } else {
                if (node.right != null && !isRed(node.right) && !isRed(node.right.left)) {
                    node = moveRedRight(node);
                }
                if (key.compareTo(node.key) == 0) {
                    Node min = findMin(node.right);
                    node.key = min.key;
                    node.value = min.value;
                    node.right = deleteMin(node.right);
                } else {
                    node.right = remove(node.right, key);
                }
            }
        }
        return fix(node);
    }

    private Node moveRedLeft(Node x) { // спускает красноту сначала вниз, затем поворотами
                                        // добивается что красный линк уходит из правой части
                                        // в левую(где нам и надо удалять)
        flipColors(x); // после получился временный 4-узел
        if (isRed(x.right.left)) {
            x.right = rotateRight(x.right);
            x = rotateLeft(x);
            flipColors(x);
        }
        return x;
    }

    private Node moveRedRight(Node x) {
        flipColors(x);
        if (isRed(x.left.left)) {
            x = rotateRight(x);
            flipColors(x);
        }
        return x;
    }

    private Node flipColors(Node x) {
        x.color = !x.color;
        x.left.color = !x.left.color;
        x.right.color = !x.right.color;
        return x;
    }

    @Nullable
    @Override
    public Key min() {
        Node min = findMin(root);
        return min == null ? null : min.key;
    }

    @Nullable
    @Override
    public Value minValue() {
        Node min = findMin(root);
        return min == null ? null : min.value;
    }

    @Nullable
    @Override
    public Key max() {
        Node max = findMax(root);
        return max == null ? null : max.key;
    }

    @Nullable
    @Override
    public Value maxValue() {
        Node max = findMax(root);
        return max == null ? null : max.value;
    }

    @Nullable
    @Override
    public Key floor(@NotNull Key key) { //максмальное, меньшее ключа (округляет вниз)
        Node floor = floor(root, key);
        if (floor == null) {
            return null;
        }
        return floor.key;
    }

    private Node floor(Node node, Key key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) == 0) {
            return node;
        }
        if (key.compareTo(node.key) < 0) {
            return floor(node.left, key);
        }
        Node temp = floor(node.right, key);
        return (temp == null || temp.key.compareTo(key) > 0) ? node : temp;
    }

    @Nullable
    @Override
    public Key ceil(@NotNull Key key) { //минимальное, большее ключа (округляет вверх)
        Node ceil = ceil(root, key);
        if (ceil == null) {
            return null;
        }
        return ceil.key;
    }

    private Node ceil(Node node, Key key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) == 0) {
            return node;
        }
        if (key.compareTo(node.key) > 0) {
            return ceil(node.right, key);
        }
        Node temp = ceil(node.left, key);
        return (temp == null || temp.key.compareTo(key) < 0) ? node : temp;
    }

    @Override
    public int size() {
        return size;
    }

    private Node findMin(Node node) {
        if (node == null) {
            return null;
        }
        return node.left != null ? findMin(node.left) : node;
    }

    private Node findMax(Node node) {
        if (node == null) {
            return null;
        }
        return node.right != null ? findMax(node.right) : node;
    }

    private boolean isRed(Node node) {
        return node != null && node.color;
    }

    private Node deleteMin(Node x) {
        if (x.left == null) {
            return null;
        }
        if (!isRed(x.left) && !isRed(x.left.left)) {
            x = moveRedLeft(x);
        }
        x.left = deleteMin(x.left);
        return fix(x);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        x.color = y.color;
        y.color = RED;
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        y.color = x.color;
        x.color = RED;
        return y;
    }
}

