package com.trendy.cbs.payload.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchSummaryDTO {

    private String branchCode;

    private String branchName;

}
