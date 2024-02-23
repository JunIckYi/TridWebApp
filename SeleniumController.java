package com.sele.admin.controller;

import java.io.IOException;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.sele.admin.service.SeleniumService;
import com.sele.admin.vo.SeleniumVO;


import lombok.Setter;

@Controller
public class SeleniumController {
	
	@Setter(onMethod_ = @Autowired)
	public SeleniumService seleniumService;
	
	@PostMapping(value="/selenium")
	 public String searchTrid(SeleniumVO selvo, Model model) throws IOException {
		
		String root = "index";
        try {
            Map<String, List<SeleniumVO>> dataMap = seleniumService.ChromeStart(selvo, model);
            System.out.println("Received SeleniumVO: " + selvo);
            
            List<SeleniumVO> svoList = dataMap.get("svo");
            List<SeleniumVO> sbvoList = dataMap.get("sbvo");
            model.addAttribute("svo", svoList);
            model.addAttribute("sbvo", sbvoList);
            
         // sbvoList가 비어있는지 확인
            if (sbvoList == null || sbvoList.isEmpty()) {
                root = "nodata";
            }else {
            	root = "client/bacodeView";
            }
            
        } catch (Exception e) {
        	 e.printStackTrace(); // 로그에 예외 스택 트레이스 추가
             throw new RuntimeException("Error in searchTrid: " + e.getMessage(), e);

        }
        return root;
        
    }
	
	@PostMapping(value="/searchApproval") 
	public String searchApproval(SeleniumVO selvo, Model model) throws IOException {
		  try {
	            Map<String, List<SeleniumVO>> dataMap = seleniumService.searchApproval(selvo, model);
	            System.out.println("Received SeleniumVO: " + selvo);
	            
	            List<SeleniumVO> svoList = dataMap.get("svo");
	            List<SeleniumVO> sbvoList = dataMap.get("sbvo");
	            model.addAttribute("svo", svoList);
	            model.addAttribute("sbvo", sbvoList);


	        } catch (Exception e) {
	        	 e.printStackTrace(); // 로그에 예외 스택 트레이스 추가
	             throw new RuntimeException("Error in searchApproval: " + e.getMessage(), e);
	        }
		  
	        return "client/bacodeView";
	}

}
