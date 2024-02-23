package com.sele.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.sele.admin.vo.SeleniumVO;

public interface SeleniumService {
	
	 public Map<String, List<SeleniumVO>> ChromeStart(SeleniumVO selvo,Model model);
	 public Map<String, List<SeleniumVO>> searchApproval(SeleniumVO selvo,Model model); 

}
