package org.dev.publicapiserverbungee.policies;

import java.util.function.Supplier;

public interface TokenSupplier extends Supplier {
    @Override
    String get();
}
