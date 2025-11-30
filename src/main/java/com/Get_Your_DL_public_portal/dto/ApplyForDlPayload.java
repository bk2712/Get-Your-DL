package com.Get_Your_DL_public_portal.dto;

import com.Get_Your_DL_public_portal.entity.LicenseDetail;
import com.Get_Your_DL_public_portal.entity.UserDetail;
import lombok.*;

import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyForDlPayload {
    private LicenseDetail licenseDetail;
    private UserDetail userDetail;
    private Map<String, Integer> fileIds;
}
