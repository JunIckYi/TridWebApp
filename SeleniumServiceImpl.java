package com.sele.admin.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.sele.admin.vo.SeleniumVO;


@Service
public class SeleniumServiceImpl implements SeleniumService {
	
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; //드라이버 ID
	//public static final String WEB_DRIVER_PATH = "/usr/local/bin/chromedriver"; //linux용 드라이버 경로
	//public static final String WEB_DRIVER_PATH ="C:\\Users\\wnslr\\git\\TridWebApp\\seleniumTest-2\\src\\main\\webapp\\resources\\chromedriver.exe";
	public static final String WEB_DRIVER_PATH ="C:\\chromedriver.exe";
	
    public Map<String, List<SeleniumVO>> ChromeStart(SeleniumVO selvo,Model model) {
        Map<String, List<SeleniumVO>> dataMap = new HashMap<>();

        try {
        
			
        	System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        	System.out.println("[spring] : chrome driver path :" + WEB_DRIVER_PATH);
			
			
			ChromeOptions options = new ChromeOptions();
			//options.setBinary("/usr/bin/google-chrome");   //ubuntu 셋업시 필요
			options.addArguments("--headless");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--start-maximized"); 
			options.addArguments("--disable-default-apps");
			options.addArguments("--disable-popup-blocking");
			options.addArguments("--remote-allow-origins=*");
			options.addArguments("--disable-gpu");
			options.addArguments("--blink-settings=imagesEnabled=false");  
			System.out.println("[spring] : Chrome options settings complete");
			
			//WebDriver 객체 생성
			ChromeDriver driver = new ChromeDriver(options);
			System.out.println("[spring] : Launch Chrome browser");
			//WebDriverWait wait = new WebDri크롬 브라우저 실행verWait(driver, Duration.ofSeconds(10)); // 10초 대기
			//사용예시 By.id("detail_src_btn")에 대한 제어
			//wait.until(ExpectedConditions.presenceOfElementLocated(By.id("detail_src_btn")));
			
			String url = "https://ksmart.ksnet.co.kr/login/loginInit.do";
			driver.get(url);
			System.out.println("[spring] : Go to loginInit page");
			
			
			WebElement id = driver.findElement(By.name("user_id"));
			id.sendKeys("");
			System.out.println("[spring] : ID entered");
			WebElement pw = driver.findElement(By.name("user_pswd"));
			pw.sendKeys("");
			System.out.println("[spring] : Password entered");
			WebElement btn =  driver.findElement(By.id("doLogin"));
			btn.click();
			System.out.println("[spring] : Login completed");
			try {Thread.sleep(1000);} catch (InterruptedException e) {}
			
			
			url = "https://ksmart.ksnet.co.kr/credit/tran/tranListInit.do?menu_id=MM00000017";
			driver.get(url);
			System.out.println("[spring] : Go to the card transaction page.");
			
			// Wait for the 'detail_src_btn' button to become clickable
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as necessary
			btn = wait.until(ExpectedConditions.elementToBeClickable(By.id("detail_src_btn")));
			
			btn = driver.findElement(By.id("detail_src_btn"));
			System.out.println("[spring] : details button clicking Completed ");
			btn.click();
			
			//String trid = selvo.getTrid(); // 주어진 숫자 문자열
			String fulltrid = selvo.getTrid(); 
			String trid = fulltrid.substring(3, 25);
			System.out.println("[spring] : trid : "+trid);
			String storeNoStr = trid.substring(4, 7); // 4부터 7번째 문자까지 추출
			int storeNo = Integer.parseInt(storeNoStr); // 문자열을 int로 변환
			System.out.println("[spring] : store number : "+storeNo);
			String posNoStr = trid.substring(7, 11); // 7부터 11번째 문자까지 추출
			int posNo = Integer.parseInt(posNoStr); // 문자열을 int로 변환
			System.out.println("[spring] : pos number : "+posNo);
			String recieptNoStr = trid.substring(11, 16); // 11부터 16번째 문자까지 추출
			int recieptNo = Integer.parseInt(recieptNoStr); // 문자열을 int로 변환
			System.out.println("[spring] : receipt number : "+recieptNo);
			String paymentDayStr = trid.substring(16, 22); // 11부터 16번째 문자까지 추출
			int paymentDay = Integer.parseInt(paymentDayStr); // 문자열을 int로 변환
			System.out.println("[spring] : date : "+paymentDay);
			 try {
				// 문자열을 날짜 포맷에 맞게 변환
		        SimpleDateFormat inputFormat = new SimpleDateFormat("yyMMdd");
		        Date paymentDate = inputFormat.parse(paymentDayStr);

	        } catch (ParseException e) {
	            // ParseException 예외 처리
	            e.printStackTrace();
	            System.err.println("[spring] : 날짜 형식이 잘못되었습니다.");
	        }
			
			//년도 데이터 추출
	        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyMMdd");
	        LocalDate paymentDate = LocalDate.parse(paymentDayStr, inputFormatter);
	        int currentYear = LocalDate.now().getYear();
			int year = paymentDate.getYear()-(currentYear-11);
			
			//월 데이터 추출
	        int month = paymentDate.getMonthValue(); // 월은 1부터 12까지의 값

			//일 데이터 추출
	        int day = paymentDate.getDayOfMonth();
//	        System.out.println("추출된 일: " + day);

	        
	        
	        // 지정한 년도와 월의 첫 날을 생성
	        LocalDate firstDayOfMonth = LocalDate.of(paymentDate.getYear(), month, 1);
//	        System.out.println("firstDayOfMonth : " + firstDayOfMonth);
	        // 첫 날로부터 해당 달의 첫 번째 요일을 찾음 (1: 일요일, 2: 월요일, ..., 7: 토요일)
	        DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();
//	        System.out.println("firstDayOfWeek 는?: " + firstDayOfWeek);
	        int WeekValue = 0;
	        
	        switch (firstDayOfWeek) {
	        case SUNDAY:
	        	WeekValue = 1;
	            break;
	        case MONDAY:
	        	WeekValue = 2;
	            break;
	        case TUESDAY:
	        	WeekValue = 3;
	            break;
	        case WEDNESDAY:
	        	WeekValue = 4;
	            break;
	        case THURSDAY:
	        	WeekValue = 5;
	            break;
	        case FRIDAY:
	        	WeekValue = 6;
	            break;
	        case SATURDAY:
	        	WeekValue = 7;
	            break;
	        default:
	        	WeekValue = -1; // 예외 처리
	    }


	        // 현재 날짜로부터 지정한 날짜까지의 차이를 계산하여 주차 계산
	        int weekNumber = day + WeekValue-1;

	        int resultRow;

	        if (weekNumber < 8) {
	        	resultRow = 1;
	        } else if (weekNumber < 15) {
	        	resultRow = 2;
	        } else if (weekNumber < 22) {
	        	resultRow = 3;
	        } else if (weekNumber < 29) {
	        	resultRow = 4;
	        } else if (weekNumber < 36) {
	        	resultRow = 5;
	        } else if (weekNumber < 44) {
	        	resultRow = 6;
	        } else {
	        	resultRow = 0;
	        }
	       
	        
	        
	        
	     // LocalDate 객체를 사용하여 요일 구하기
	        DayOfWeek dayOfWeek = paymentDate.getDayOfWeek();
	        int resultCol = dayOfWeek.getValue(); // 1: 일요일, 2: 월요일, ..., 7: 토요일

	        switch (dayOfWeek) {
	            case SUNDAY:
	            	resultCol = 1;
	                break;
	            case MONDAY:
	            	resultCol = 2;
	                break;
	            case TUESDAY:
	            	resultCol = 3;
	                break;
	            case WEDNESDAY:
	            	resultCol = 4;
	                break;
	            case THURSDAY:
	            	resultCol = 5;
	                break;
	            case FRIDAY:
	            	resultCol = 6;
	                break;
	            case SATURDAY:
	            	resultCol = 7;
	                break;
	            default:
	            	resultCol = -1; // 예외 처리
	        }


	        
	        
	        
			/////////////////////// 켈린더에 추출 한 데이터 입력 /////////////////////
			//from 켈린더 버튼 클릭
			driver.findElement(By.xpath("//*[@id='src_date_from_to']/img[1]")).click();
			//from year 세팅   
			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[1]/option["+year+"]")).click();
			//from month 세팅
			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[2]/option[" + month + "]")).click();
			//from day 세팅
			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr[" + resultRow + "]/td[" + resultCol + "]/a")).click();
			System.out.println("[spring] : calender - start date");
			
			//to 켈린더 버튼 클릭
			driver.findElement(By.xpath("//*[@id='src_date_from_to']/img[2]")).click();
			//to year 세팅
			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[1]/option")).click();
			//to month 세팅
			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[2]/option[1]")).click();
			//to day 세팅
			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr[" + resultRow + "]/td[" + resultCol + "]/a")).click();
			System.out.println("[spring] : calender - end date");
			
			//input TRID
			String trid22 = trid.substring(2, 22);
			

			driver.findElement(By.id("remark_1")).sendKeys(trid22);
			System.out.println("[spring] : trid input completed");
			
			//CLICK Search butten
			driver.findElement(By.id("search_btn")).click();
			System.out.println("[spring] : search button click");

			try {Thread.sleep(1000);} catch (InterruptedException e) {}
			
			
			
			// 
			
			 // 웹 페이지에서 데이터 추출
	        WebElement gubunCntElement = driver.findElement(By.id("total_cnt"));
	        int gubunCount = Integer.parseInt(gubunCntElement.getText());
	        int gubunCnt = 0;
	        
	        for(int i = 1 ; i <= gubunCount; i++) {
	        	WebElement gubunElement2 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[4]"));	//gubun
	        	String getGubun = gubunElement2.getText();
                if(getGubun.equals("승인")) {
                	++gubunCnt;
                }
	        }
	        //System.out.println("총 거래건수는? " + gubunCount + " 건입니다.");
	        
			//System.out.println("승인건수는? " + gubunCnt + " 건입니다.");
			
			
			
			
			if(gubunCnt == 1) {
				WebElement approval = driver.findElement(By.xpath("//*[@id=\'1\']/td[6]")); //approval
				String approvalNO = approval.getText();
				WebElement cardNo = driver.findElement(By.xpath("//*[@id=\'1\']/td[10]")); //cardnumber
				String cardNumber = cardNo.getText();

				
				LocalDate after90day = paymentDate.plusDays(90);
				String newFormattedDate = after90day.format(inputFormatter);

				
				int afterYear = after90day.getYear()-2012;
				//월 데이터 추출
		        int afterMonth = after90day.getMonthValue(); // 월은 1부터 12까지의 값

				//일 데이터 추출
		        int afterDay = after90day.getDayOfMonth();

		        
//				//로드시간 대기
				try {Thread.sleep(1000);} catch (InterruptedException e) {}
		        
		        driver.findElement(By.xpath("//*[@id='controlBtn']/button[1]")).click();  //'RESET
		        driver.findElement(By.xpath("//*[@id='searchContents']/table/tbody/tr/td[2]/div/table/tbody/tr/td[5]/div/button[2]")).click();       //TODAY BUTTEN
		        

				//from 켈린더 버튼 클릭
				driver.findElement(By.xpath("//*[@id='src_date_from_to']/img[1]")).click();
				//from year 세팅   
				driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[1]/option["+year+"]")).click();
				//from month 세팅
				driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[2]/option[" + month + "]")).click();
				//from day 세팅
				driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr[" + resultRow + "]/td[" + resultCol + "]/a")).click();
		        
				
				driver.findElement(By.id("approval_no")).sendKeys(approvalNO);
				
				driver.findElement(By.id("card_no")).sendKeys(cardNumber);
		        
				//CLICK Search butten
				driver.findElement(By.id("search_btn")).click();
				System.out.println("[spring] : researching completed");
				
			}
			
			

			
			 // 웹 페이지에서 데이터 추출
	        WebElement totalCntElement = driver.findElement(By.id("total_cnt"));
	        int rowCount = Integer.parseInt(totalCntElement.getText());


	        // 데이터를 모델에 추가
	        List<SeleniumVO> list = new ArrayList<>();
	        List<SeleniumVO> bacodeList = new ArrayList<>();
	        // 숫자만 추출하는 정규 표현식
	        String regex = "\\D+"; // 숫자가 아닌 문자를 나타내는 정규 표현식
	        int AmountSum = 0;
	        SeleniumVO sbvo = new SeleniumVO();
	        
	        for (int i = 1; i <= rowCount; i++) {
	        	 	SeleniumVO svo = new SeleniumVO();
	                WebElement cellElement1 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[3]"));	//type
	                svo.setType(cellElement1.getText());
	                WebElement cellElement2 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[4]"));	//gubun
	                svo.setGubun(cellElement2.getText());
	                WebElement cellElement3 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[5]"));	//date
	                svo.setDate(cellElement3.getText());
	                WebElement cellElement4 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[6]"));	//approval
	                svo.setApproval(cellElement4.getText());
	                WebElement cellElement5 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[7]"));	//van
	                svo.setVan(cellElement5.getText());	
	                WebElement cellElement6 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[8]"));	//amount
	                String stAmount =  cellElement6.getText();
	                String fixStAmount = stAmount.replaceAll(regex, "");
	                int fixIntAmount = Integer.parseInt(fixStAmount);
	                if(svo.getGubun().equals("취소")) {
	                	fixIntAmount = fixIntAmount *-1;
	                	System.out.println("[spring] : 취소금액 있음 : " + fixIntAmount);
	                }
	                AmountSum += fixIntAmount;
	                svo.setAmount(Integer.parseInt(fixStAmount));
	                
	                WebElement cellElement7 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[9]"));	//halbu
	                svo.setHalbu(cellElement7.getText());
	                WebElement cellElement8 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[10]"));	//cardNumber
	                svo.setCardNumber(cellElement8.getText());
	                WebElement cellElement9 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[11]"));	//cardCompany
	                svo.setCardCompany(cellElement9.getText());
	                WebElement cellElement10 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[12]"));	//posNumber
	                svo.setPosNumber(cellElement10.getText());
	                WebElement cellElement11 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[13]"));	//recieptNumber
	                svo.setRecieptNumber(cellElement11.getText());
	                WebElement cellElement12 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[14]"));	//trid
	                svo.setTrid(cellElement12.getText());
	               
	                
	                	
	                if(svo.getGubun().equals("승인")) {
	                    // cellElement3에서 숫자만 추출하여 sbvo에 설정
	                    String cellElement3Text = cellElement3.getText();
	                    String fixdate = cellElement3Text.replaceAll(regex, "");
	                    sbvo.setDate(fixdate); // 숫자만 포함된 문자열을 설정
	                    sbvo.setApproval(cellElement4.getText());
	                	sbvo.setVan("999"+cellElement5.getText());
	                	
	                    String cellElement6Text = cellElement6.getText();
	                    String fixAmount = cellElement6Text.replaceAll(regex, "");
	                    
	                    if(gubunCnt == 1) {
	                    	sbvo.setAmount(AmountSum);
	                    } else {
	                    	sbvo.setAmount(Integer.parseInt(fixAmount));
	                    }
	                	
	                	sbvo.setHalbu(cellElement7.getText());
	                	bacodeList.add(sbvo);
	                }
	                
	                list.add(svo);  
	                	
	                	

	                

	        }
	        
	        model.addAttribute("svo", list);
	        model.addAttribute("sbvo", bacodeList);
	        System.out.println("[spring] : seleniumServiceimpl funtion completed");
	        
			try {
				//드라이버가 null이 아니라면
				if(driver != null) {
					//드라이버 연결 종료
					driver.close(); //드라이버 연결 해제
					//프로세스 종료
					driver.quit();
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}

            dataMap.put("svo", list);
            dataMap.put("sbvo", bacodeList);
            
            if (driver != null) {
                driver.quit();
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("[spring] : dataMap type return");
        return dataMap;
    }
    
    
    
    
    
    public Map<String, List<SeleniumVO>> searchApproval(SeleniumVO selvo,Model model) {
    	  Map<String, List<SeleniumVO>> dataMap = new HashMap<>();

          try {
          	
  			
  			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
  			
  		
  			ChromeOptions options = new ChromeOptions();
  			options.addArguments("--headless");
  			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
  			options.addArguments("--start-maximized"); 
  			options.addArguments("--disable-default-apps");
  			options.addArguments("--disable-popup-blocking");
  			options.addArguments("--remote-allow-origins=*");
  			options.addArguments("--disable-gpu");
			options.addArguments("--blink-settings=imagesEnabled=false");  
  			
  			
  			//WebDriver 객체 생성
  			ChromeDriver driver = new ChromeDriver(options);
  			
  			
  			String url = "https://ksmart.ksnet.co.kr/login/loginInit.do";
  			driver.get(url);
  			
  			
  			
  			WebElement id = driver.findElement(By.name("user_id"));
  			id.sendKeys("");
  			
  			WebElement pw = driver.findElement(By.name("user_pswd"));
  			pw.sendKeys("");
  			
  			WebElement btn =  driver.findElement(By.id("doLogin"));
  			btn.click();

  			try {Thread.sleep(1000);} catch (InterruptedException e) {}
  			
  			
  			url = "https://ksmart.ksnet.co.kr/credit/tran/tranListInit.do?menu_id=MM00000017";
  			driver.get(url);
  			
  			
			// Wait for the 'detail_src_btn' button to become clickable
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust the timeout as necessary
			btn = wait.until(ExpectedConditions.elementToBeClickable(By.id("detail_src_btn")));
			
			btn = driver.findElement(By.id("detail_src_btn"));
			System.out.println("상세 버튼 클릭 완료");
			btn.click();
  			
  			String fulltrid = selvo.getTrid(); // 주어진 숫자 문자열
  			System.out.println(fulltrid);
  			String trid = "99"+fulltrid;
  			
  			String storeNoStr = trid.substring(4, 7); // 4부터 7번째 문자까지 추출
  			int storeNo = Integer.parseInt(storeNoStr); // 문자열을 int로 변환
  			System.out.println(storeNo);
  			String posNoStr = trid.substring(7, 11); // 7부터 11번째 문자까지 추출
  			int posNo = Integer.parseInt(posNoStr); // 문자열을 int로 변환
  			System.out.println(posNo);
  			String recieptNoStr = trid.substring(11, 16); // 11부터 16번째 문자까지 추출
  			int recieptNo = Integer.parseInt(recieptNoStr); // 문자열을 int로 변환
  			System.out.println(recieptNo);
  			String paymentDayStr = trid.substring(16, 22); // 11부터 16번째 문자까지 추출
  			int paymentDay = Integer.parseInt(paymentDayStr); // 문자열을 int로 변환
  			System.out.println(paymentDay);
  			 try {
  				// 문자열을 날짜 포맷에 맞게 변환
  		        SimpleDateFormat inputFormat = new SimpleDateFormat("yyMMdd");
  		        Date paymentDate = inputFormat.parse(paymentDayStr);

  	        } catch (ParseException e) {
  	            // ParseException 예외 처리
  	            e.printStackTrace();
  	            System.err.println("날짜 형식이 잘못되었습니다.");
  	        }
  			
  			//년도 데이터 추출
  	        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyMMdd");
  	        LocalDate paymentDate = LocalDate.parse(paymentDayStr, inputFormatter);
  	        int currentYear = LocalDate.now().getYear();
			int year = paymentDate.getYear()-(currentYear-11);
  			
  			//월 데이터 추출
  	        int month = paymentDate.getMonthValue(); // 월은 1부터 12까지의 값

  			//일 데이터 추출
  	        int day = paymentDate.getDayOfMonth();
//  	        System.out.println("추출된 일: " + day);

  	        
  	        
  	        // 지정한 년도와 월의 첫 날을 생성
  	        LocalDate firstDayOfMonth = LocalDate.of(paymentDate.getYear(), month, 1);
//  	        System.out.println("firstDayOfMonth : " + firstDayOfMonth);
  	        // 첫 날로부터 해당 달의 첫 번째 요일을 찾음 (1: 일요일, 2: 월요일, ..., 7: 토요일)
  	        DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();
//  	        System.out.println("firstDayOfWeek 는?: " + firstDayOfWeek);
  	        int WeekValue = 0;
  	        
  	        switch (firstDayOfWeek) {
  	        case SUNDAY:
  	        	WeekValue = 1;
  	            break;
  	        case MONDAY:
  	        	WeekValue = 2;
  	            break;
  	        case TUESDAY:
  	        	WeekValue = 3;
  	            break;
  	        case WEDNESDAY:
  	        	WeekValue = 4;
  	            break;
  	        case THURSDAY:
  	        	WeekValue = 5;
  	            break;
  	        case FRIDAY:
  	        	WeekValue = 6;
  	            break;
  	        case SATURDAY:
  	        	WeekValue = 7;
  	            break;
  	        default:
  	        	WeekValue = -1; // 예외 처리
  	    }


  	        // 현재 날짜로부터 지정한 날짜까지의 차이를 계산하여 주차 계산
  	        int weekNumber = day + WeekValue-1;

  	        int resultRow;

  	        if (weekNumber < 8) {
  	        	resultRow = 1;
  	        } else if (weekNumber < 15) {
  	        	resultRow = 2;
  	        } else if (weekNumber < 22) {
  	        	resultRow = 3;
  	        } else if (weekNumber < 29) {
  	        	resultRow = 4;
  	        } else if (weekNumber < 36) {
  	        	resultRow = 5;
  	        } else if (weekNumber < 44) {
  	        	resultRow = 6;
  	        } else {
  	        	resultRow = 0;
  	        }
  	       
  	        
  	        
  	        
  	     // LocalDate 객체를 사용하여 요일 구하기
  	        DayOfWeek dayOfWeek = paymentDate.getDayOfWeek();
  	        int resultCol = dayOfWeek.getValue(); // 1: 일요일, 2: 월요일, ..., 7: 토요일

  	        switch (dayOfWeek) {
  	            case SUNDAY:
  	            	resultCol = 1;
  	                break;
  	            case MONDAY:
  	            	resultCol = 2;
  	                break;
  	            case TUESDAY:
  	            	resultCol = 3;
  	                break;
  	            case WEDNESDAY:
  	            	resultCol = 4;
  	                break;
  	            case THURSDAY:
  	            	resultCol = 5;
  	                break;
  	            case FRIDAY:
  	            	resultCol = 6;
  	                break;
  	            case SATURDAY:
  	            	resultCol = 7;
  	                break;
  	            default:
  	            	resultCol = -1; // 예외 처리
  	        }


  	        
  	        
  	        
  			/////////////////////// 켈린더에 추출 한 데이터 입력 /////////////////////
  			//from 켈린더 버튼 클릭
  			driver.findElement(By.xpath("//*[@id='src_date_from_to']/img[1]")).click();
  			//from year 세팅   
  			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[1]/option["+year+"]")).click();
  			//from month 세팅
  			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[2]/option[" + month + "]")).click();
  			//from day 세팅
  			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr[" + resultRow + "]/td[" + resultCol + "]/a")).click();
  			
  			
  			//to 켈린더 버튼 클릭
  			driver.findElement(By.xpath("//*[@id='src_date_from_to']/img[2]")).click();
  			//to year 세팅
  			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[1]/option")).click();
  			//to month 세팅
  			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[2]/option[1]")).click();
  			
  			
  			//to day 세팅
  			driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr[" + resultRow + "]/td[" + resultCol + "]/a")).click();
  			
			/*
			 * //input TRID //String trid22 = trid.substring(2, 22); String appro =
			 * selvo.getApproval(); // 주어진 숫자 문자열
			 * 
			 * driver.findElement(By.id("remark_1")).sendKeys(appro);
			 * 
			 * 
			 * //CLICK Search butten driver.findElement(By.id("search_btn")).click();
			 * 
			 * 
			 * try {Thread.sleep(1000);} catch (InterruptedException e) {}
			 */
  			
  			
			/*
			 * //
			 * 
			 * // 웹 페이지에서 데이터 추출 WebElement gubunCntElement =
			 * driver.findElement(By.id("total_cnt")); int gubunCount =
			 * Integer.parseInt(gubunCntElement.getText()); int gubunCnt = 0;
			 * 
			 * for(int i = 1 ; i <= gubunCount; i++) { WebElement gubunElement2 =
			 * driver.findElement(By.xpath("//*[@id='" + i + "']/td[4]")); //gubun String
			 * getGubun = gubunElement2.getText(); if(getGubun.equals("승인")) { ++gubunCnt; }
			 * } System.out.println("총 거래건수는? " + gubunCount + " 건입니다.");
			 * 
			 * System.out.println("승인건수는? " + gubunCnt + " 건입니다.");
			 */
  			
  			
  			
  			
  			
				/*
				 * WebElement approval = driver.findElement(By.xpath("//*[@id=\'1\']/td[6]"));
				 * //approval String approvalNO = approval.getText(); WebElement cardNo =
				 * driver.findElement(By.xpath("//*[@id=\'1\']/td[10]")); //cardnumber String
				 * cardNumber = cardNo.getText();
				 */

  				
  				LocalDate after90day = paymentDate.plusDays(90);
  				String newFormattedDate = after90day.format(inputFormatter);

  				
  				int afterYear = after90day.getYear()-2012;
  				//월 데이터 추출
  		        int afterMonth = after90day.getMonthValue(); // 월은 1부터 12까지의 값

  				//일 데이터 추출
  		        int afterDay = after90day.getDayOfMonth();

  		        
//  				//로드시간 대기
  				try {Thread.sleep(1000);} catch (InterruptedException e) {}
  		        
  		        driver.findElement(By.xpath("//*[@id='controlBtn']/button[1]")).click();  //'RESET
  		        driver.findElement(By.xpath("//*[@id='searchContents']/table/tbody/tr/td[2]/div/table/tbody/tr/td[5]/div/button[2]")).click();       //TODAY BUTTEN
  		        

  				//from 켈린더 버튼 클릭
  				driver.findElement(By.xpath("//*[@id='src_date_from_to']/img[1]")).click();
  				//from year 세팅   
  				driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[1]/option["+year+"]")).click();
  				//from month 세팅
  				driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div[1]/div/select[2]/option[" + month + "]")).click();
  				//from day 세팅
  				driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr[" + resultRow + "]/td[" + resultCol + "]/a")).click();
  		        
  				
				/*
				 * driver.findElement(By.id("approval_no")).sendKeys(approvalNO);
				 * 
				 * driver.findElement(By.id("card_no")).sendKeys(cardNumber);
				 * 
				 * //CLICK Search butten
				 */  				
  	  			String appro = selvo.getApproval(); 
  	  			String cardN = selvo.getCardNumber();
  	  			driver.findElement(By.id("approval_no")).sendKeys(appro);
  	  			driver.findElement(By.id("card_no")).sendKeys(cardN);
  				driver.findElement(By.id("search_btn")).click();
  				
  				try {Thread.sleep(1000);} catch (InterruptedException e) {}
  			
  			
  			

  			
  			 // 웹 페이지에서 데이터 추출
  	        WebElement totalCntElement = driver.findElement(By.id("total_cnt"));
  	        int rowCount = Integer.parseInt(totalCntElement.getText());


  	        // 데이터를 모델에 추가
  	        List<SeleniumVO> list = new ArrayList<>();
  	        List<SeleniumVO> bacodeList = new ArrayList<>();
  	        // 숫자만 추출하는 정규 표현식
  	        String regex = "\\D+"; // 숫자가 아닌 문자를 나타내는 정규 표현식
  	        int AmountSum = 0;
  	        SeleniumVO sbvo = new SeleniumVO();
  	        
  	        for (int i = 1; i <= rowCount; i++) {
  	        	 	SeleniumVO svo = new SeleniumVO();
  	                WebElement cellElement1 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[3]"));	//type
  	                svo.setType(cellElement1.getText());
  	                WebElement cellElement2 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[4]"));	//gubun
  	                svo.setGubun(cellElement2.getText());
  	                WebElement cellElement3 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[5]"));	//date
  	                svo.setDate(cellElement3.getText());
  	                WebElement cellElement4 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[6]"));	//approval
  	                svo.setApproval(cellElement4.getText());
  	                WebElement cellElement5 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[7]"));	//van
  	                svo.setVan(cellElement5.getText());	
  	                WebElement cellElement6 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[8]"));	//amount
  	                String stAmount =  cellElement6.getText();
  	                String fixStAmount = stAmount.replaceAll(regex, "");
  	                int fixIntAmount = Integer.parseInt(fixStAmount);
  	                if(svo.getGubun().equals("취소")) {
  	                	fixIntAmount = fixIntAmount *-1;
  	                	System.out.println(fixIntAmount);
  	                }
  	                AmountSum += fixIntAmount;
  	                svo.setAmount(Integer.parseInt(fixStAmount));
  	                
  	                WebElement cellElement7 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[9]"));	//halbu
  	                svo.setHalbu(cellElement7.getText());
  	                WebElement cellElement8 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[10]"));	//cardNumber
  	                svo.setCardNumber(cellElement8.getText());
  	                WebElement cellElement9 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[11]"));	//cardCompany
  	                svo.setCardCompany(cellElement9.getText());
  	                WebElement cellElement10 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[12]"));	//posNumber
  	                svo.setPosNumber(cellElement10.getText());
  	                WebElement cellElement11 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[13]"));	//recieptNumber
  	                svo.setRecieptNumber(cellElement11.getText());
  	                WebElement cellElement12 = driver.findElement(By.xpath("//*[@id='" + i + "']/td[14]"));	//trid
  	                svo.setTrid(cellElement12.getText());
  	               
  	                
  	                	
  	                if(svo.getGubun().equals("승인")) {
  	                    // cellElement3에서 숫자만 추출하여 sbvo에 설정
  	                    String cellElement3Text = cellElement3.getText();
  	                    String fixdate = cellElement3Text.replaceAll(regex, "");
  	                    sbvo.setDate(fixdate); // 숫자만 포함된 문자열을 설정
  	                    sbvo.setApproval(cellElement4.getText());
  	                	sbvo.setVan("999"+cellElement5.getText());
  	                	
  	                    String cellElement6Text = cellElement6.getText();
  	                    String fixAmount = cellElement6Text.replaceAll(regex, "");
  	                    
  	                    
  	                    	sbvo.setAmount(AmountSum);
  	                    
  	                	
  	                	sbvo.setHalbu(cellElement7.getText());
  	                	bacodeList.add(sbvo);
  	                }
  	                
  	                list.add(svo);  
  	                	
  	                	

  	                

  	        }
  	        
  	        model.addAttribute("svo", list);
  	        model.addAttribute("sbvo", bacodeList);
  	        
  			try {
  				//드라이버가 null이 아니라면
  				if(driver != null) {
  					//드라이버 연결 종료
  					driver.close(); //드라이버 연결 해제
  					//프로세스 종료
  					driver.quit();
  				}
  			} catch (Exception e) {
  				throw new RuntimeException(e.getMessage());
  			}

              dataMap.put("svo", list);
              dataMap.put("sbvo", bacodeList);
              
              if (driver != null) {
                  driver.quit();
              }

          } catch (Exception e) {
              throw new RuntimeException(e.getMessage());
          }

          return dataMap;
    }

}
