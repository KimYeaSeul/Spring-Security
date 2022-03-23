package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

//@Repository 라는 어노테이션이 없어도 ioc가 됨. jpaRepository를 상속했기 때문에 자동으로 Bean등록이 됨.
public interface UserRepository extends JpaRepository<User, Integer>{

}
