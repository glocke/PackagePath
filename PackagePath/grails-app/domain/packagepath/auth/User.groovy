package packagepath.auth

import packagepath.Profile

class User {	
	
	transient springSecurityService
	
	String username
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	
	static hasOne = [profile: Profile]
	static hasMany = [oauthIds: OauthId]
	
	static constraints = {
        username blank: false, unique: true
        profile nullable: true
    }

    static mapping = {
        table '`user`'
    }
	
	User(String userName, boolean enabled){
		this.username = userName
		this.enabled = enabled
	}

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }
}
