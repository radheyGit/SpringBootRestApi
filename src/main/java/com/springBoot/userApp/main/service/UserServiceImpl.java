package com.springBoot.userApp.main.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.springBoot.userApp.main.constant.AppConstants;
import com.springBoot.userApp.main.entity.UserEntity;
import com.springBoot.userApp.main.exception.FileMissingException;
import com.springBoot.userApp.main.exception.UserManagementAppModuleException;
import com.springBoot.userApp.main.model.UserModel;
import com.springBoot.userApp.main.repository.UserRepository;
import com.springBoot.userApp.main.utils.PwdUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;

	private static String generateRandomPazzword(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(AppConstants.CANDIDATE_CHARS.charAt(random.nextInt(AppConstants.CANDIDATE_CHARS.length())));
		}
		return sb.toString();
	}

	/**
	 * @author Radhey
	 * @return boolean
	 **/
	@Override
	public boolean saveData(UserModel userModel) throws Exception {
		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(userModel, user);
		String pazzword = generateRandomPazzword(6);
		String encryptPwd = PwdUtils.encryptPwd(pazzword);
		user.setActiveSW("1");
		user.setUserPwd(encryptPwd);
		repository.save(user);
		if (user.getUserId() != null && user != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<UserModel> searchUser(String searchKey) throws UserManagementAppModuleException {
		List<UserModel> userModel =null;
		List<UserEntity> userEntity = repository.findAll(searchKey);
		userModel = new ArrayList<>();
		for (UserEntity entity : userEntity) {
			UserModel model = new UserModel();
			BeanUtils.copyProperties(entity, model);
			userModel.add(model);
		}
		if(userModel.isEmpty()) {
			throw new FileMissingException("No data Found "+searchKey);
		}
		return userModel;
	}

	@Override
	public UserModel getUser(Integer userId) throws UserManagementAppModuleException {
		UserModel userModel =null;
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(userId);
		Example<UserEntity> example = Example.of(userEntity);
		Optional<UserEntity> user = repository.findOne(example);
		if (user.isPresent()) {
			userEntity = user.get();
			userModel = new UserModel();
			BeanUtils.copyProperties(userEntity, userModel);
			//return userModel;
		}
		if(userModel==null) {
			throw new FileMissingException("Enter Valid User ID "+userId);
		}
			return userModel;
	}

	@Override
	public UserModel editUser(Integer userId, UserModel userModel) throws UserManagementAppModuleException {
		UserModel userModelUpdted=null;
		Optional<UserEntity> user = repository.findById(userId);
		if(user.isPresent()) {
			UserEntity userEntity = user.get();
			userEntity.setCityName(userModel.getCityName());
			userEntity.setCountryName(userModel.getCountryName());
			userEntity.setDob(userModel.getDob());
			userEntity.setFirstName(userModel.getFirstName());
			userEntity.setGender(userModel.getGender());
			userEntity.setStateName(userModel.getStateName());
			userEntity.setUserEmail(userModel.getUserEmail());
			userEntity.setLastName(userModel.getLastName());
			userEntity.setUserMobile(userModel.getUserMobile());
			userEntity.setPinCode(userModel.getPinCode());
			userEntity.setDoj(userModel.getDoj());
			
			userModelUpdted=new UserModel(); 
			UserEntity updated = repository.save(userEntity);
			BeanUtils.copyProperties(updated, userModelUpdted);
			//return userModelUpdted;
		}
		if(userModelUpdted==null) {
			throw new FileMissingException("User Not Found "+userId);
		}
		return userModelUpdted;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		Optional<UserEntity> user = repository.findById(userId);
		if(user.isPresent()) {
			repository.delete(user.get());
			return true;
		}
		return false;
	}

	@Override
	public boolean softDeleteUserById(Integer userId) {
		Optional<UserEntity> user = repository.findById(userId);
		if(user.isPresent()) {
			UserEntity userEntity = user.get();
			userEntity.setActiveSW("0");
			repository.save(userEntity);
			return true;
		}
		return false;
	}

	@Override
	public List<UserModel> sortUserByDob() throws UserManagementAppModuleException {
		List<UserModel> userModel =null;
		List<UserEntity> userEnties = repository.findAll();
		List<UserEntity> collect = userEnties.stream().sorted(Comparator.comparing(UserEntity::getDob))
											 .collect(Collectors.toList());
		userModel = new ArrayList<>();
		for (UserEntity entity : collect) {
			UserModel model = new UserModel();
			BeanUtils.copyProperties(entity, model);
			userModel.add(model);
		}
		if(userModel.isEmpty()) {
			throw new FileMissingException("No data Found ");
		}
		return userModel;
	}

	@Override
	public List<UserModel> sortUserByDoj() throws UserManagementAppModuleException {
		List<UserModel> userModel =null;
		List<UserEntity> userEnties = repository.findAll();
		List<UserEntity> collect = userEnties.stream().sorted(Comparator.comparing(UserEntity::getDoj))
											 .collect(Collectors.toList());
		userModel = new ArrayList<>();
		for (UserEntity entity : collect) {
			UserModel model = new UserModel();
			BeanUtils.copyProperties(entity, model);
			userModel.add(model);
		}
		if(userModel.isEmpty()) {
			throw new FileMissingException("No data Found ");
		}
		return userModel;
	}
	
	

}
