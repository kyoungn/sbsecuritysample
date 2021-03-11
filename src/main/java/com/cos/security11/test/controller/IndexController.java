package com.cos.security11.test.controller;


import java.util.Collections;
import java.util.Optional;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.cos.security11.model.User;
import com.cos.security11.model.UserForm;
import com.cos.security11.repository.UserRepo;
import com.cos.security11.security.PrincipalDetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController {

	private final UserRepo userRepo;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/")
//	public ModelAndView index(Authentication auth, Model model) {
	public String index(Authentication auth, Model model) {
				
//		ModelAndView model = new ModelAndView();
//		model.setViewName("index");
		
		String name = "?";
		if(auth != null) {
			Optional.ofNullable(auth.getAuthorities()).orElse(Collections.emptyList())
			.stream()
			.forEach(a -> log.info(" role {}", a.getAuthority()));
			
			PrincipalDetail detail = (PrincipalDetail) auth.getPrincipal();
			name = detail.getUsername();
		}
		model.addAttribute("name", name);
		
//		return model;
		return "index";
	}
	
	@GetMapping("/user")
	public ModelAndView user() {
		return new ModelAndView("user");
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	

	@GetMapping("/login")
	public  ModelAndView loginForm() {
		return new ModelAndView("loginForm");
	}
	
	@GetMapping("/joinForm")
	public ModelAndView joinForm() {
		return new ModelAndView("joinForm");
	}
	

	@PostMapping("/join")
	public RedirectView joinProc(@ModelAttribute UserForm user1) {
		log.info(" UserForm :: {}", user1);
		
		userRepo.save(
					User.builder()
							.username(user1.getUsername())
							.email(user1.getEmail())
							.password(bCryptPasswordEncoder.encode(user1.getPassword()))
							.role("ROLE_USER")
							.build()
				);
		
		return new RedirectView("/login");
	}
		
	@GetMapping("/denied")
	public @ResponseBody String expired() {
		return "권한이 없습니다.";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보.";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 정보.";
	}
}
