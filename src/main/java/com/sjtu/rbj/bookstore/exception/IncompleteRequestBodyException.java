package com.sjtu.rbj.bookstore.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Bojun Ren
 * @data 2023/06/16
 */
public class IncompleteRequestBodyException extends NestedRuntimeException {
	/**
	 * Create a new IncompleteRequestBodyException.
	 * @param msg the detail message
	 */
    public IncompleteRequestBodyException(String msg) {
        super(msg);
    }
}
