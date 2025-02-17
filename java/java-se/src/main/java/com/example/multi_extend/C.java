package com.example.multi_extend;

import java.util.Objects;

public class C implements A, B{

    private A a;
    @Override
    public void show() {
        A.super.show();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        C c = (C) o;
        return Objects.equals(a, c.a);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(a);
    }
}
