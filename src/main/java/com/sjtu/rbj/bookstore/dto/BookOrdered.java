package com.sjtu.rbj.bookstore.dto;

import java.util.UUID;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Bojun Ren
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookOrdered {
    @Nullable
    private UUID uuid;
    @Nullable
    private Integer quantity;

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("uuid='");
        buffer.append(getUuid());
        buffer.append("'");
        buffer.append(", quantity='");
        buffer.append(getQuantity());
        buffer.append("'");
        return buffer.toString();
    }

}
