package rs.raf.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private String password;
    @Nullable
    private boolean canReadUsers;
    @Nullable
    private boolean canCreateUsers;
    @Nullable
    private boolean canUpdateUsers;
    @Nullable
    private boolean canDeleteUsers;

    @Nullable
    private boolean canSearchVacuum;
    @Nullable
    private boolean canStartVacuum;
    @Nullable
    private boolean canStopVacuum;
    @Nullable
    private boolean canDischargeVacuum;
    @Nullable
    private boolean canAddVacuum;
    @Nullable
    private boolean canRemoveVacuum;
}
