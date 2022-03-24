package com.cos.security1.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.model.User;

// 시큐리티가 /login을 낚아채서 로그인을 진행시킨다.
// 진행이 완료가 되면 시큐리티 session을 만들어 준다. (Security ContextHolder라는 키 값에 session 정보 저장)
// session에 들어가는 정보는 Authentication 객체로 type이 정해져 있다.
// Authentication 안에는 User정보가 있어야 됨.
// User object type = UserDetails type object

// Security Session => Authentication => UserDetails(PrincipalDetails)
public class PrincipalDetails implements UserDetails{

	private User user; //콤포지션
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		//1년동안 로그인 안하면 휴면회원으로 변경
		return true;
	}

}
