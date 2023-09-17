package com.sjtu.rbj.bookstore.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicate a controller's method can only be invoked by the administrators.
 *
 * @author Bojun Ren
 */
@Target(ElementType.METHOD)
public @interface Administer {
}
