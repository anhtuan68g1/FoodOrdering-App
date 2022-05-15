package vn.aptech.sem4prj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="admin_roles")
public class AdminRole implements Serializable {
    @Id
    @Column(name="id")
    private int id;

    //mo ta tham chieu ve bang AppUser - bang quan he 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="admin_id", referencedColumnName = "id")
    private Administrator adminId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", referencedColumnName = "id")
    private Role roleId;
}
