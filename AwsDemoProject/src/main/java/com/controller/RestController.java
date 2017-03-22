package com.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bp.AmazonBp;

@Controller
public class RestController {
	private static Logger LOGGER = Logger
			.getLogger(RestController.class);
	
	@Autowired
	AmazonBp amazonBp;
	
	@RequestMapping("/gets3Json")
	public @ResponseBody String accountList(Model model) throws Exception {
		return amazonBp.saveJson();
	}
	
	@RequestMapping("/getData")
	public @ResponseBody String accountListe() {
		return "hello raji";
	}
}
