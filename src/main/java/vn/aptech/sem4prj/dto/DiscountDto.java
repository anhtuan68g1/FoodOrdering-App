package vn.aptech.sem4prj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    private int id;
    private String name;
    private Double value;
    private String start;
    private String end;
}
