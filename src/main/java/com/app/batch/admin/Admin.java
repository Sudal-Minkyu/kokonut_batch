package com.app.batch.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.app.batch.admin.enums.AuthorityRole;
import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(of = "adminId")
@Table(name="kn_admin")
public class Admin {

    @Id
    @Column(name = "admin_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "master_id")
    private Long masterId;

    @Column(name = "kn_user_type")
    private Integer knUserType;

    @Column(name = "kn_reg_type")
    private Integer knRegType;

    @Column(name = "kn_email")
    private String knEmail;

    @Column(name = "kn_password")
    private String knPassword;

    @Column(name = "kn_pwd_change_date")
    private LocalDateTime knPwdChangeDate;

    @Column(name = "kn_pwd_error_count")
    private Integer knPwdErrorCount = 0;

    @Column(name = "kn_name")
    private String knName;

    @Column(name = "kn_phone_number")
    private String knPhoneNumber;

    @Column(name = "kn_department")
    private String knDepartment;

    @Column(name = "kn_state")
    private Integer knState = 1;

    @Column(name = "kn_dormant_date")
    private LocalDateTime knDormantDate;

    @Column(name = "kn_expected_delete_date")
    private LocalDateTime knExpectedDeleteDate;

    @Column(name = "kn_reason")
    private String knReason;

    @Column(name = "kn_ip_addr")
    private String knIpAddr;

    @Column(name = "kn_approval_state")
    private Integer knApprovalState = 1;

    @Column(name = "kn_approval_date")
    private LocalDateTime knApprovalDate;

    @Column(name = "kn_approval_return_reason")
    private String knApprovalReturnReason;

    @Column(name = "kn_approval_name")
    private String knApprovalName;

    @Column(name = "kn_withdrawal_reason_type")
    private Integer knWithdrawalReasonType;

    @Column(name = "kn_withdrawal_reason")
    private String knWithdrawalReason;

    @Column(name = "kn_withdrawal_date")
    private LocalDateTime knWithdrawalDate;

    @Column(name = "kn_last_login_date")
    private LocalDateTime knLastLoginDate;

    @Column(name = "kn_is_email_auth")
    private String knIsEmailAuth;

    @Column(name = "kn_email_auth_Code")
    private String knEmailAuthCode;

    @Column(name = "kn_email_auth_number")
    private String knEmailAuthNumber;

    @Column(name = "kn_pwd_auth_number")
    private String knPwdAuthNumber;

    @Column(name = "kn_auth_start_date")
    private LocalDateTime knAuthStartDate;

    @Column(name = "kn_auth_end_date")
    private LocalDateTime knAuthEndDate;

    @Column(name = "kn_otp_key")
    private String knOtpKey;

    @Column(name = "kn_is_login_auth")
    private String knIsLoginAuth = "N";

    @Enumerated(EnumType.STRING)
    @Column(name="kn_role_code")
    private AuthorityRole knRoleCode;

    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @Column(name = "modify_id")
    private Long modify_id;

    @Column(name = "modify_email")
    private String modify_email;

    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
