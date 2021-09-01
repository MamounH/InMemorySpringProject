package com.atypon.springproject.entity;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Quote {

    private int id;

    @NotNull(message = "* Please provide book id number")
    private int bookId;

    @NotEmpty(message = "* quote cannot be empty")
    private String quote;

    private String bookName;

    @Override
    public String toString() {
        return id + "," + bookId + "," + quote +"\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote = (Quote) o;
        return id == quote.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
