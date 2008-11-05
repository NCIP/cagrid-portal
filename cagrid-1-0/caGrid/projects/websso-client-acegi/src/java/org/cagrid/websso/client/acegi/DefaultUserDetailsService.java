package org.cagrid.websso.client.acegi;

public class DefaultUserDetailsService extends BaseUserDetailsService {

	@Override
	protected WebSSOUser loadUser(String userName) {
		DefaultGrantedAuthority [] grantedAuthorities=new DefaultGrantedAuthority[]{new DefaultGrantedAuthority("ROLE_DEFAULT")};
		WebSSOUser user=new WebSSOUser(userName, "default",true,true,true,true,grantedAuthorities);
		return user;
	}
}
