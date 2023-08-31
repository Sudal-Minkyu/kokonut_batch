package com.app.batch.payment;

import com.app.batch.awskmshistory.AwsKmsHistoryService;
import com.app.batch.company.companypayment.CompanyPayment;
import com.app.batch.company.companypayment.CompanyPaymentRepository;
import com.app.batch.company.companypayment.dtos.CompanyPaymentListDto;
import com.app.batch.company.companypayment.dtos.CompanyPaymentReservationListDto;
import com.app.batch.company.companypayment.dtos.CompanyPaymentSearchDto;
import com.app.batch.company.companypaymentinfo.CompanyPaymentInfo;
import com.app.batch.company.companypaymentinfo.CompanyPaymentInfoRepository;
import com.app.batch.email.EmailService;
import com.app.batch.kokonutuser.DynamicUserRepositoryCustom;
import com.app.batch.payment.dtos.PaymentReservationResultDto;
import com.app.batch.payment.dtos.PaymentReservationSearchDto;
import com.app.batch.payment.paymenterror.PaymentError;
import com.app.batch.payment.paymenterror.PaymentErrorRepository;
import com.app.batch.payment.paymentprivacycount.PaymentPrivacyCount;
import com.app.batch.payment.paymentprivacycount.PaymentPrivacyCountRepository;
import com.app.batch.payment.paymentprivacycount.dtos.PaymentPrivacyCountMonthAverageDto;
import com.app.batch.utils.BootPayService;
import com.app.batch.utils.CommonUtil;
import com.app.batch.utils.KeyGenerateService;
import com.app.batch.utils.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : 구독관리 관련 Service
 */
@Slf4j
@Service
public class PaymentService {

	private final MailSender mailSender;
	private final BootPayService bootPayService;
	private final KeyGenerateService keyGenerateService;
	private final EmailService emailService;
	private final AwsKmsHistoryService awsKmsHistoryService;

	private final PaymentRepository paymentRepository;
	private final PaymentErrorRepository paymentErrorRepository;
	private final CompanyPaymentRepository companyPaymentRepository;
	private final CompanyPaymentInfoRepository companyPaymentInfoRepository;
	private final PaymentPrivacyCountRepository paymentPrivacyCountRepository;

	private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

	@Autowired
	public PaymentService(MailSender mailSender, BootPayService bootPayService, KeyGenerateService keyGenerateService,
						  EmailService emailService, AwsKmsHistoryService awsKmsHistoryService,
						  PaymentRepository paymentRepository,
						  PaymentErrorRepository paymentErrorRepository, CompanyPaymentRepository companyPaymentRepository,
						  CompanyPaymentInfoRepository companyPaymentInfoRepository,
						  PaymentPrivacyCountRepository paymentPrivacyCountRepository, DynamicUserRepositoryCustom dynamicUserRepositoryCustom) {
		this.mailSender = mailSender;
		this.bootPayService = bootPayService;
		this.keyGenerateService = keyGenerateService;
		this.emailService = emailService;
		this.awsKmsHistoryService = awsKmsHistoryService;
		this.paymentRepository = paymentRepository;
		this.paymentErrorRepository = paymentErrorRepository;
		this.companyPaymentRepository = companyPaymentRepository;
		this.companyPaymentInfoRepository = companyPaymentInfoRepository;
		this.paymentPrivacyCountRepository = paymentPrivacyCountRepository;
		this.dynamicUserRepositoryCustom = dynamicUserRepositoryCustom;
	}

	// 매일 새벽 5시 시작
	// 일일 개인정보 수 저장
	@Transactional
	public void dayPrivacyAdd(LocalDate localDate) {
		log.info("dayPrivacyAdd 호출");

		List<CompanyPaymentListDto> companyPaymentListDtos = companyPaymentRepository.findByPaymentList(localDate);
//		log.info("companyPaymentListDtos : "+companyPaymentListDtos);

		List<PaymentPrivacyCount> paymentPrivacyCountList = new ArrayList<>();
		PaymentPrivacyCount paymentPrivacyCount;

		for(CompanyPaymentListDto companyPaymentListDto : companyPaymentListDtos) {

			paymentPrivacyCount = new PaymentPrivacyCount();

			String ctName = companyPaymentListDto.getCtName();

			paymentPrivacyCount.setCpCode(companyPaymentListDto.getCpCode());
			paymentPrivacyCount.setCtName(companyPaymentListDto.getCtName());

			int privacyCount = dynamicUserRepositoryCustom.privacyListTotal("SELECT COUNT(*) FROM "+ ctName);
//			log.info(ctName+"의 개인정보 수 : "+privacyCount);

			paymentPrivacyCount.setPpcCount(privacyCount);
			paymentPrivacyCount.setPpcDate(localDate);

			paymentPrivacyCountList.add(paymentPrivacyCount);
		}

		paymentPrivacyCountRepository.saveAll(paymentPrivacyCountList);
	}

	// 매달 첫날(1일) 새벽 12시 시작
	// 결제 예약걸기
	@Transactional
	public void kokonutPay(LocalDate localDate) throws Exception {
		log.info("kokonutPay 호출");

		// 결제예약할 리스트 불러오기 (조건 : 현재 날짜로부터 일일 개인정보 수의 값이 하나이상 존재하는지,
		// 익일 5일 오후 12시 결제예약 걸기
		LocalDate yesterday = localDate.minusDays(1);  // 어제의 날짜를 구합니다.
		String yyyymm = yesterday.format(DateTimeFormatter.ofPattern("yyyyMM")); // 어제날짜의 yyyymm을 가져온다.
//		log.info("yesterday : "+yesterday);
//		log.info("yyyymm : "+yyyymm);

		LocalDate firstDayOfLastMonth = yesterday.withDayOfMonth(1);  // 어제가 속한 월의 첫 날을 구합니다.
		LocalDate lastDayOfLastMonth = yesterday.withDayOfMonth(yesterday.lengthOfMonth()); // 어제가 속한 월의 마지막 날을 구합니다.

//		log.info("firstDayOfLastMonth : "+firstDayOfLastMonth);
//		log.info("lastDayOfLastMonth : "+lastDayOfLastMonth);

		// 결제할 날짜
		LocalDateTime payDayTime = LocalDateTime.now().plusDays(4).withHour(12).withMinute(0).withSecond(0).withNano(0);
//		log.info("payDayTime : "+payDayTime);

		List<CompanyPaymentReservationListDto> companyPaymentReservationListDtos = companyPaymentRepository.findByPaymentReservationList(localDate);
//		log.info("companyPaymentReservationListDtos : "+companyPaymentReservationListDtos);

		List<Payment> paymentList = new ArrayList<>();

		// 구독해지한 기업 빌링키 삭제처리
		List<CompanyPayment> companyPaymentList = new ArrayList<>();
		List<CompanyPaymentInfo> companyPaymentInfoList = new ArrayList<>();

		Payment payment;
		for(CompanyPaymentReservationListDto companyPaymentReservationListDto : companyPaymentReservationListDtos) {
			payment = new Payment();

			String cpCode = companyPaymentReservationListDto.getCpCode();
			String ctName = companyPaymentReservationListDto.getCtName();

			// 원화가치 가져오기
			int wonPrice = CommonUtil.wonPriceGet();
			log.info("wonPrice : "+wonPrice);

			// 월 평균 개인정보 수 계산하기
			PaymentPrivacyCountMonthAverageDto paymentPrivacyCountMonthAverageDto =
					paymentPrivacyCountRepository.findByMonthPrivacyCount(cpCode, ctName, firstDayOfLastMonth, lastDayOfLastMonth);
//			log.info("월 평균 개인정보 수 : "+paymentPrivacyCountMonthAverageDto);

			// PaymentPayDto 결제금액 Dto
//			PaymentPayDto paymentPayDto = new PaymentPayDto();
//			log.info("paymentPayDto : "+paymentPayDto);

			int payAmount;

			// AWS RDS 클라우드 금액 호출(아웃바운드 트래픽?)
			int awsRDSCloud = 0;
			log.info("awsRDSCloud : "+awsRDSCloud);


			// AWS S3 금액 호출
			int awsS3Cloud = 0;
			log.info("awsS3Cloud : "+awsS3Cloud);


			// AWS KMS 금액 호출
			double awsKMSClound = awsKmsHistoryService.findByMonthKmsPrice(cpCode, yyyymm); // 당월 호출 금액 구하기
			log.info("awsKMSClound : "+awsKMSClound);

			int payCloudAmount;
			payCloudAmount = (int) Math.round((awsRDSCloud + awsS3Cloud + awsKMSClound)* wonPrice);

			// 서비스 금액 호출
			int payServiceAmount = 0;
			if(companyPaymentReservationListDto.getCpiPayType().equals("0") && !companyPaymentReservationListDto.getCpiValidStart().isBefore(localDate)) {
				payServiceAmount = CommonUtil.kokonutMonthPrice(paymentPrivacyCountMonthAverageDto.getMonthAverageCount());

				// 만약 구독해지한 기업일 경우 부분 결제
				if(companyPaymentReservationListDto.getSubscribeCheck().equals("1")) {
					LocalDateTime cpSubscribeDate = companyPaymentReservationListDto.getCpSubscribeDate();

					LocalDate dateToCheckWithoutTime = cpSubscribeDate.toLocalDate();

					LocalDate cpiValidStart = companyPaymentReservationListDto.getCpiValidStart();
					log.info("kokonutPay 호출 - 해지날짜 : " + cpiValidStart);

					long dateCount;

					// cpiValidStart와 cpSubscribeDate가 같은 년도와 월인지 확인
					if (cpiValidStart.getYear() == dateToCheckWithoutTime.getYear() && cpiValidStart.getMonth() == dateToCheckWithoutTime.getMonth()) {
						dateCount = ChronoUnit.DAYS.between(dateToCheckWithoutTime, cpiValidStart);
					} else {
						dateCount = ChronoUnit.DAYS.between(dateToCheckWithoutTime, localDate);
					}

					payServiceAmount = CommonUtil.calculateUsedAmount(payServiceAmount, localDate, (int)dateCount);
					log.info("kokonutPay 호출 - 해지후 중간결제 금액 : " + payServiceAmount);
				}
			}

			// 이메일이용 금액 호출
			int payEmailAmount = emailService.emailSendMonthPrice(cpCode, yyyymm); // 당월 호출 금액 구하기
			log.info("payEmailAmount : "+payEmailAmount);

			// 결제 할 금액
			payAmount = payCloudAmount + payServiceAmount + payEmailAmount;
			payAmount = (payAmount / 10) * 10; // 1원 단위 0으로 수정

			log.info("결제 할 금액 : "+payAmount);

			if(payAmount > 100 && !companyPaymentReservationListDto.getCpiValidStart().isBefore(localDate)) {
				String orderId = keyGenerateService.keyGenerate("kn_payment", cpCode+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")), "KokonutSystem");
				log.info("orderId : "+orderId);

				PaymentReservationResultDto paymentReservationResultDto = bootPayService.kokonutReservationPayment(
						orderId, payAmount, "월간 사용료 예약결제", companyPaymentReservationListDto.getCpiBillingKey(), payDayTime);

				if(paymentReservationResultDto != null) {
					// 결제 내역 저장
					payment.setPayOrderid(orderId);
					payment.setCpCode(cpCode);
					payment.setPayAmount(payAmount); // 총 결제금액
					payment.setPayCloudAmount(payCloudAmount); // AWS 결제금액
					payment.setPayServiceAmount(payServiceAmount); // 서비스 결제금액
					payment.setPayEmailAmount(payEmailAmount); // 이메일 결제금액
					payment.setPayState("2");
					payment.setPayMethod("0");
					payment.setPayPrivacyCount(paymentPrivacyCountMonthAverageDto.getMonthAverageCount());
					payment.setPayBillingStartDate(paymentPrivacyCountMonthAverageDto.getLowDate());
					payment.setPayBillingEndDate(paymentPrivacyCountMonthAverageDto.getBigDate());
					payment.setPayReserveId(paymentReservationResultDto.getPayReserveId());
					payment.setPayReserveExecuteDate(paymentReservationResultDto.getPayReserveExecuteDate());
					paymentList.add(payment);
				}
			}

			if(companyPaymentReservationListDto.getSubscribeCheck().equals("1")) {
				// 구독해지한 기업이므로 빌링값 삭제처리
				Optional<CompanyPayment> optionalCompanyPayment = companyPaymentRepository.findCompanyPaymentByCpiIdAndCpCode(companyPaymentReservationListDto.getCpiId(), cpCode);
				if(optionalCompanyPayment.isPresent()) {
					companyPaymentList.add(optionalCompanyPayment.get());
					Optional<CompanyPaymentInfo> optionalCompanyPaymentInfo = companyPaymentInfoRepository.findCompanyPaymentInfoByCpiId(optionalCompanyPayment.get().getCpiId());
					optionalCompanyPaymentInfo.ifPresent(companyPaymentInfoList::add);
				}
			}

		}

		paymentRepository.saveAll(paymentList);

		companyPaymentRepository.deleteAll(companyPaymentList);
		companyPaymentInfoRepository.deleteAll(companyPaymentInfoList);

		// *숙제*
		// 알림메일 전송하기


	}

	// 매달 오후 12시 30분 시작
	// 결제 예약결제 확인
	@Transactional
	public void kokonutCheck() throws Exception {
		log.info("kokonutCheck 호출");

		// 결제 예약중 그리고 자동결제인 항목 리스트 호출하기
		// payReserveId 값을 통해 예약결제 상태를 조회한다.
		List<Payment> paymentList = paymentRepository.findPaymentByPayStateAndPayMethodOrderByCpCodeDesc("2", "0");
//		log.info("paymentList : "+paymentList);

		List<Payment> updatePaymentList = new ArrayList<>();
		List<PaymentError> newPaymentErrorList = new ArrayList<>();
		PaymentError paymentError;
		if(paymentList.size() != 0) {
			for(Payment payment : paymentList) {
				if(!payment.getPayReserveId().equals("test")) { // 테스트 가데이터는 제외

					String payReserveId = payment.getPayReserveId();
//					log.info("payReserveId : "+payReserveId);

					PaymentReservationSearchDto paymentReservationSearchDto = bootPayService.kokonutReservationCheck(payReserveId);
//					log.info("paymentReservationSearchDto : "+paymentReservationSearchDto);

					if(paymentReservationSearchDto != null) {
						Integer status = paymentReservationSearchDto.getStatus();
						if(status != 0) {
							// 결제완료 또는 결제실패할 경우
							if(status == -1) {
								payment.setPayState("0");

								paymentError = new PaymentError();
								paymentError.setPayId(payment.getPayId());
								paymentError.setPeState("0");
								paymentError.setPeCount(0);
								paymentError.setInsert_date(LocalDateTime.now());
								newPaymentErrorList.add(paymentError);

							} else if(status == 1) {
								payment.setPayState("1");
							}
							payment.setPayReserveStartedDate(paymentReservationSearchDto.getPayReserveStartedDate());
							payment.setPayReserveFinishedDate(paymentReservationSearchDto.getPayReserveFinishedDate());
							payment.setPayReceiptid(paymentReservationSearchDto.getPayReceiptid());

							updatePaymentList.add(payment);
						}
					}
				}
			}

			paymentRepository.saveAll(updatePaymentList);
			paymentErrorRepository.saveAll(newPaymentErrorList);
		}

	}

	// 매일 새벽 2시 시작
	// 결제에러건 결제처리
	@Transactional
	public void kokonutPayError(LocalDate localDate) throws Exception {
		log.info("kokonutPayError 호출");

		String cpCode = "";
		CompanyPaymentSearchDto companyPaymentSearchDto = null;
		List<Payment> paymentList = paymentRepository.findPaymentByPayStateAndPayMethodOrderByCpCodeDesc("0", "0");

		List<Payment> updatePaymentList = new ArrayList<>();
		List<PaymentError> updatePaymentErrorList = new ArrayList<>();

		if(paymentList.size() != 0) {
			for(Payment payment : paymentList) {

				Optional<PaymentError> optionalPaymentError = paymentErrorRepository.findPaymentErrorByPayIdAndPeState(payment.getPayId(), "0");
				if(optionalPaymentError.isPresent()) {

					if(!Objects.equals(cpCode, payment.getCpCode())) {
						cpCode = payment.getCpCode();
						companyPaymentSearchDto = companyPaymentRepository.findByPaymentSearch(cpCode);
					}

					if(companyPaymentSearchDto != null) {
						Integer payAmount = payment.getPayAmount();
						String orderId = keyGenerateService.keyGenerate("kn_payment", cpCode+localDate.format(DateTimeFormatter.ofPattern("yyyyMM")), "KokonutSystem");

						// 결제오류건 결제처리
						String receiptId = bootPayService.kokonutPayment(companyPaymentSearchDto.getCpiBillingKey(), orderId, payAmount,
								"결제오류건 재결제 처리", companyPaymentSearchDto.getKnName(), companyPaymentSearchDto.getKnPhoneNumber());

						if(!receiptId.equals("")) {
							payment.setPayState("1");
							payment.setPayReceiptid(receiptId);
							payment.setModify_date(LocalDateTime.now());

							optionalPaymentError.get().setPeState("1");
						} else {
							optionalPaymentError.get().setPeCount(optionalPaymentError.get().getPeCount()+1);

							// *숙제*
							// 메일 알림보내기 -> 결제오류 통보

						}
						optionalPaymentError.get().setModify_date(LocalDateTime.now());

						updatePaymentList.add(payment);
						updatePaymentErrorList.add(optionalPaymentError.get());
					} else {
						log.error("결제할 빌링키가 존재하지 않습니다. cpCode : "+cpCode);
					}
				} else {
					log.error("결제 오류건 정보없음 payId : "+payment.getPayId());
				}
			}

			paymentRepository.saveAll(updatePaymentList);
			paymentErrorRepository.saveAll(updatePaymentErrorList);
		}
	}

}
