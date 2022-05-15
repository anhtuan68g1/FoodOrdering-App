package vn.aptech.sem4prj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private String fullname;
    private Date Time;
    private Double revenue;
}
