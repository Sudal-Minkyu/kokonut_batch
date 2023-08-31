package com.app.batch.payment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : 결제정보 내역 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "payId")
@Data
@NoArgsConstructor
@Table(name="kn_payment")
public class Payment {

    @Id
    @Column(name = "pay_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;

    @Column(name = "pay_orderid")
    private String payOrderid;

    @Column(name = "pay_receiptid")
    private String payReceiptid;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "pay_amount")
    private Integer payAmount;

    @Column(name = "pay_cloud_amount")
    private Integer payCloudAmount;

    @Column(name = "pay_service_amount")
    private Integer payServiceAmount;

    @Column(name = "pay_email_amount")
    private Integer payEmailAmount;

    @Column(name = "pay_state")
    private String payState;

    @Column(name = "pay_method")
    private String payMethod;

    @Column(name = "pay_privacy_count")
    private Integer payPrivacyCount;

    @Column(name = "pay_billing_start_date")
    private LocalDate payBillingStartDate;

    @Column(name = "pay_billing_end_date")
    private LocalDate payBillingEndDate;

    @Column(name = "pay_reserve_id")
    private String payReserveId;

    @Column(name = "pay_reserve_execute_date")
    private LocalDateTime payReserveExecuteDate;

    @Column(name = "pay_reserve_started_date")
    private LocalDateTime payReserveStartedDate;

    @Column(name = "pay_reserve_finished_date")
    private LocalDateTime payReserveFinishedDate;

    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
