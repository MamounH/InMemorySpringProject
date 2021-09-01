package com.atypon.springproject.entity;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book implements Serializable {


    private int ID;

    @NotEmpty(message = "* Please provide book name")
    private String name;

    @NotEmpty(message = "* Please provide book author name")
    private String author;

    @NotEmpty(message = "* Please provide book subject")
    private String subject;


    @NotEmpty(message = "* Please provide book publisher")
    private String publisher;

    @NotEmpty(message = "* Please provide book publication year")
    private String year;

    @Override
    public String toString() {
        return ID + "," + name + "," + subject + "," + author + ","
                + publisher + "," + year +"\n";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return ID == book.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}