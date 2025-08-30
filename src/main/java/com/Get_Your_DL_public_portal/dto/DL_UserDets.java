package com.Get_Your_DL_public_portal.dto;

import com.Get_Your_DL_public_portal.entity.LicenseDetail;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DL_UserDets {
    private LicenseDetail licenseDetail;
    private UserDetail userDetail;
}
