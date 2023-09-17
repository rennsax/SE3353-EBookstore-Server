package com.sjtu.rbj.bookstore.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnTransformer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Bojun Ren
 * @date 2023/04/08
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`book`")
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "BINARY(16) DEFAULT(UUID_TO_BIN(UUID()))", nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private String title;

    @Column(name = "pic_id", unique = true, columnDefinition = "CHAR(15)")
    private String picId;

    @Column(name = "`price`", columnDefinition = "DECIMAL(5, 2)")
    @ColumnTransformer(read = "100*price", write = "? / 100")
    /** Store in unit cent. */
    private Integer priceCent;

    private String author;

    private Integer stock;

    @Column(columnDefinition = "DATE")
    private Date date;

    @Column(unique = true, columnDefinition = "CHAR(13)")
    private String isbn;

    @Lob
    private String description;

    @PrePersist
    void prePersistInitialize() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (stock == null) {
            stock = 100;
        }
        if (priceCent == null) {
            priceCent = 100;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Book other = ((Book) obj);
        if (other.uuid == null) {
            return false;
        }
        return other.uuid.equals(this.uuid);
    }

    @Override
    public int hashCode() {
        if (uuid == null) {
            return 0;
        }
        return uuid.hashCode();
    }
}
