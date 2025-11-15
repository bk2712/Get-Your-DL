package com.Get_Your_DL_public_portal.dto;

import com.Get_Your_DL_public_portal.entity.Document;
import com.Get_Your_DL_public_portal.entity.LicenseDetail;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDets {
    private LicenseDetail licenseDetail;
    private Document docs;
}
