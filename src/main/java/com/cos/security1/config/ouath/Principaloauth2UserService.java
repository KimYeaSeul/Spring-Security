package com.cos.security1.config.ouath;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.auth.provider.FacebookUserInfo;
import com.cos.security1.auth.provider.GoogleUserInfo;
import com.cos.security1.auth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

// 후처리 해줌
@Service
public class Principaloauth2UserService extends DefaultOAuth2UserService{
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	
	// 구글로 부터 받은 userRequest 데이터에 대한 후처리되는함수
	// 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println(userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인가능 -> google
		System.out.println(userRequest.getAccessToken().getTokenValue()); 
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인완료 -> code return(OAuth-Client Library) -> AccessToken 요청
		// userRequest정보 -> loadUser함수호출 -> 구글로부터 회원 프로필을 받아줌.
		System.out.println(oauth2User.getAttributes());
		
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글로그인요청.");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북요청.");
			oAuth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		}else {
			System.out.println("구글과 페이스북만 지원함.");
		}
		// getAttrubute 정보를 토대로 회원가입 진행.
//		String provider = userRequest.getClientRegistration().getRegistrationId(); //google, facebook
		String provider = oAuth2UserInfo.getProvider(); //google, facebook
//		String providerId = oauth2User.getAttribute("sub");
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider+"_"+providerId;
		String password = bCryptPasswordEncoder.encode("겟인데어");
//		String email = oauth2User.getAttribute("email");
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			System.out.println("최초 로그인 회원가입 진행");
			userEntity = User.builder().username(username) 
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		} else {
			System.out.println("로그인을 이미 한 적이 있습니다.");
		}
		 
		// OAuthentication Object 안에 세션정보로 들어감. 
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}
