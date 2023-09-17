package com.sjtu.rbj.bookstore.entity;

/**
 * @author Bojun Ren
 */
public enum OrderState {
    /**
     * An order that is pending, typically denotes the cart.
     */
    PENDING,

    /**
     * An order that is already complete.
     */
    COMPLETE,

    /**
     * An order that is submitted, but yet not complete.
     */
    TRANSPORTING;
}
