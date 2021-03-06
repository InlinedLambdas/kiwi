/*
 * MIT License
 *
 * Copyright (c) 2022 InlinedLambdas and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.inlambda.kiwi;

import lombok.experimental.UtilityClass;
import org.inlambda.kiwi.lazy.LazyFunction;
import org.inlambda.kiwi.lazy.LazySupplier;
import org.inlambda.kiwi.proxy.LazyProxy;
import org.inlambda.kiwi.reflection.AccessibleClass;
import org.inlambda.kiwi.tuple.Pair;
import org.inlambda.kiwi.tuple.Triple;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Proxy;
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.AvailableSince("0.1.0")
@UtilityClass
public class Kiwi {
    public static <T> Result<T, ? extends Throwable> fromAny(AnySupplier<T> supplier) {
        try {
            var result = supplier.get();
            return Result.ok(result);
        } catch (Throwable e) {
            return Result.err(e);
        }
    }

    public static Result<?, ? extends Throwable> runAny(AnyRunnable runnable) {
        try {
            runnable.run();
            return Result.ok();
        } catch (Throwable e) {
            return Result.err(e);
        }
    }

    public static <V> LazySupplier<V> byLazy(Supplier<V> supplier) {
        return LazySupplier.by(supplier);
    }

    public static <K, V> LazyFunction<K, V> byLazy(Function<K, V> function) {
        return LazyFunction.by(function);
    }

    @SuppressWarnings("unchecked")
    public static <T> T lazyProxy(Class<T> tClass, Supplier<T> tSupplier) {
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass}, new LazyProxy<>(byLazy(tSupplier)));
    }

    public static <T> T todo(String msg) {
        throw new UnsupportedOperationException("TODO: " + msg);
    }

    public static <T> T todo() {
        throw new UnsupportedOperationException("TODO");
    }

    public static <K, V> Pair<K, V> pairOf(K k, V v) {
        return new Pair<>(k, v);
    }

    public static <A, B, C> Triple<A, B, C> tripleOf(A a, B b, C c) {
        return new Triple<>(a, b, c);
    }

    public static <T> AccessibleClass<T> accessClass(Class<T> clazz) {
        return AccessibleClass.of(clazz);
    }

}
