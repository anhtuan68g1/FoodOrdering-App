package vn.aptech.sem4prj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String image;
    private Double price;
    private String category_name;
    private String description;
    private Date createdAt;
    private Date modifiedAt;
}
