package com.spring.batch.demo.batch;

import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.batch.demo.entity.Users;
import com.spring.batch.demo.repository.UsersRepository;


@Component
public class Processor implements ItemProcessor<Users, Users> {

	@Autowired
	private UsersRepository userRepo;

	@Override
	public Users process(Users user) throws Exception {
		Optional<Users> userFromDb = userRepo.findById(user.getUserId());
		if(userFromDb.isPresent()) {
			user.setName(user.getName().toUpperCase());
			user.setDept(user.getDept().toUpperCase());
			user.setAccount(user.getAccount().add(userFromDb.get().getAccount()));
		}
		return user;
	}

}
