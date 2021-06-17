package net.victorbetoni.tasker.utils;

public class Pair<T,U> {
    private T first;
    private U second;

    public static <T,U> Pair<T,U> of(T first, U second) {
        return new Pair<>(first,second);
    }

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}
