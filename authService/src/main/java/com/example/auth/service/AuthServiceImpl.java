package com.example.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	
	@Override
	public String register(String name, String email, String password, String role) {
		if(userRepository.existsByEmail(email)) {
			throw new RuntimeException("User already exists");
		}
		
		User user=new User();
		user.setFullName(name);
		user.setEmail(email);
		user.setHashedPassword(encoder.encode(password));
		user.setRole(role);
		
		userRepository.save(user);
		
		return "User Registered Successfully";
	}

	@Override
	public String login(String email, String password) {
		
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		
		 if (!user.isActive()) {
		        throw new RuntimeException("Account is deactivated");
		    }
		
		if(!encoder.matches(password, user.getHashedPassword())){
			throw new RuntimeException("Invalid Password");
		}
		
		return jwtUtil.generateToken(user.getEmail(), user.getRole());
	}

	@Override
	public User getUserById(Integer id) {
		return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public User updateProfile(Integer id, String name, String phone) {
		User user = getUserById(id);

        user.setFullName(name);
        user.setPhone(phone);

        return userRepository.save(user);
	}

	@Override
	public void changePassword(Integer id, String newPassword) {
		User user = getUserById(id);

        user.setHashedPassword(encoder.encode(newPassword));

        userRepository.save(user);
		
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public void deactivateAccount(Integer id) {
		User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setActive(false);   //  deactivate user

	    userRepository.save(user);
		
	}
}
